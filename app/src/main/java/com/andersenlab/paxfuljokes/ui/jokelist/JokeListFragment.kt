package com.andersenlab.paxfuljokes.ui.jokelist

import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.andersenlab.paxfuljokes.MainActivity
import com.andersenlab.paxfuljokes.R
import com.andersenlab.paxfuljokes.model.dto.Value
import com.andersenlab.paxfuljokes.model.local.JokeDatabase
import com.andersenlab.paxfuljokes.model.local.PrefManager
import com.andersenlab.paxfuljokes.model.remote.ApiService
import com.andersenlab.paxfuljokes.ui.common.MyJokesAdapter
import com.andersenlab.paxfuljokes.ui.jokelist.adapter.JokeListAdapter
import com.andersenlab.paxfuljokes.ui.jokelist.viewmodel.JokeListModelFactory
import com.andersenlab.paxfuljokes.ui.jokelist.viewmodel.JokeListViewModel
import com.squareup.seismic.ShakeDetector
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_joke_list.*
import org.koin.android.ext.android.get

class JokeListFragment : Fragment() {
    private lateinit var prefManager: PrefManager
    private lateinit var jokeListViewModel: JokeListViewModel
    private lateinit var shakeDetector: ShakeDetector
    private lateinit var sensorManager: SensorManager

    private val apiService: ApiService = get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let {
            val jokeDao = JokeDatabase.invoke(it).jokeDao()
            jokeListViewModel =
                ViewModelProviders.of(
                    this,
                    JokeListModelFactory(
                        apiService,
                        jokeDao
                    )
                ).get(JokeListViewModel::class.java)
        }

        sensorManager = requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector {
            if (prefManager.isOffline) {
                showRandomJoke()
            } else {
                showJokeList()
            }
        }
        return inflater.inflate(R.layout.fragment_joke_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start(sensorManager)
    }

    override fun onPause() {
        super.onPause()
        shakeDetector.stop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            prefManager = PrefManager(it.applicationContext)
                .also {
                    (activity as MainActivity).toolbar.title =
                        getString(R.string.char_jokes, it.firstName)
                }
        }
        initRecycler()
    }

    private fun initRecycler() {
        context?.let { jokeListRecycler.layoutManager = LinearLayoutManager(it) }

        if (prefManager.isOffline) {
            jokeListRecycler.adapter = MyJokesAdapter(
                prefManager
            ).apply {
                deleteLiveData.observe(viewLifecycleOwner, Observer {
                    deleteFromDatabase(it)
                    clear()
                })
            }
            jokeListViewModel.randomValue.observe(viewLifecycleOwner, Observer {
                (jokeListRecycler.adapter as MyJokesAdapter).submitList(it)
            })
        } else {
            jokeListRecycler.adapter = JokeListAdapter().apply {
                likeLiveData.observe(viewLifecycleOwner, Observer {
                    addJokeToDatabase(it)
                })
                shareLiveDate.observe(viewLifecycleOwner, Observer {
                    shareJoke(it)
                })
            }
            showJokeList()
        }
    }

    private fun addJokeToDatabase(value: Value) {
        val new = prefManager.replaceNameWithPlaceholder(value.joke)
        jokeListViewModel.addValueToDatabase(Value(joke = new))
    }

    private fun showJokeList() {
        showLoading()
        jokeListViewModel.getJokeList(
            prefManager.firstNameNullable,
            prefManager.lastNameNullable
        ) {
            hideLoading()
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
            .observe(this, Observer { jokeList ->
                hideLoading()
                (jokeListRecycler.adapter as JokeListAdapter).submitList(jokeList.value)
            })
    }

    private fun showRandomJoke() {
        jokeListViewModel.getRandomValue()
    }

    private fun shareJoke(value: Value) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val shareMessage = value.joke
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, null))
        } catch (e: Exception) {
            context?.applicationContext?.let {
                Toast.makeText(it, getString(R.string.toast_share), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun deleteFromDatabase(value: Value) = jokeListViewModel.deleteValueToDatabase(value)

    private fun showLoading() {
        progressCircular.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressCircular.visibility = View.GONE
    }
}
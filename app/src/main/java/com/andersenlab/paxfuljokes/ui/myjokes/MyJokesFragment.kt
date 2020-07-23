package com.andersenlab.paxfuljokes.ui.myjokes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andersenlab.paxfuljokes.R
import com.andersenlab.paxfuljokes.model.dto.Value
import com.andersenlab.paxfuljokes.model.local.JokeDatabase
import com.andersenlab.paxfuljokes.model.local.PrefManager
import com.andersenlab.paxfuljokes.ui.common.MyJokesAdapter
import com.andersenlab.paxfuljokes.ui.myjokes.viewmodel.MyJokesModelFactory
import com.andersenlab.paxfuljokes.ui.myjokes.viewmodel.MyJokesViewModel
import kotlinx.android.synthetic.main.alert_my_joke.view.*
import kotlinx.android.synthetic.main.fragment_my_jokes.*

class MyJokesFragment : Fragment() {

    private lateinit var myJokesViewModel: MyJokesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            val jokeDao = JokeDatabase.invoke(it).jokeDao()
            val prefManager =
                PrefManager(it.applicationContext)
            myJokesViewModel =
                ViewModelProviders.of(
                    this,
                    MyJokesModelFactory(
                        jokeDao,
                        prefManager
                    )
                )
                    .get(MyJokesViewModel::class.java)
        }
        return inflater.inflate(R.layout.fragment_my_jokes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initRecycler()
        showJokeList()
    }

    private fun initUi() {
        fab.setOnClickListener {
            showDialog()
        }

        myJokesRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.visibility == View.VISIBLE) {
                    fab.hide()
                } else if (dy < 0 && fab.visibility != View.VISIBLE) {
                    fab.show()
                }
            }
        })
    }

    private fun initRecycler() {
        myJokesRecycler.layoutManager = LinearLayoutManager(requireContext())
        activity?.let { activity ->
            myJokesRecycler.adapter =
                MyJokesAdapter(
                    PrefManager(
                        activity
                    )
                )
                    .apply {
                        deleteLiveData.observe(viewLifecycleOwner, Observer {
                            deleteFromDatabase(it)
                        })
                    }
        }

    }

    private fun deleteFromDatabase(value: Value) = myJokesViewModel.deleteValueToDatabase(value)

    private fun showJokeList() {
        myJokesViewModel.valueList.observe(viewLifecycleOwner, Observer {
            (myJokesRecycler.adapter as MyJokesAdapter).submitList(it)
        })
    }

    private fun showDialog() {
        val layout = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_my_joke, view as ViewGroup?, false)
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.alert_title))
            setView(layout)
            setPositiveButton(R.string.alert_save) { _, _ ->
                val input = layout.rootView.alertEditText.text.toString().trim()
                myJokesViewModel.handlePositive(input)
            }
            setNegativeButton(R.string.alert_cancel) { dialog, _ ->
                dialog.cancel()
            }
        }.show()

    }


}
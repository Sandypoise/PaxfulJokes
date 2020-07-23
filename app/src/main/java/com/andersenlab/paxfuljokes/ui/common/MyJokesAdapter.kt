package com.andersenlab.paxfuljokes.ui.common

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andersenlab.paxfuljokes.R
import com.andersenlab.paxfuljokes.model.dto.Value
import com.andersenlab.paxfuljokes.model.local.PrefManager
import kotlinx.android.synthetic.main.holder_my_joke.view.*

class MyJokesAdapter(private val prefManager: PrefManager) :
    ListAdapter<Value, MyJokesAdapter.MyJokesViewHolder>(
        Value.diffCallback
    ) {

    private val _deleteLiveData = MutableLiveData<Value>()

    val deleteLiveData: LiveData<Value> = _deleteLiveData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyJokesViewHolder {
        val holder = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_my_joke, parent, false)
        return MyJokesViewHolder(
            holder
        )
    }

    override fun onBindViewHolder(holder: MyJokesViewHolder, position: Int) {
        val item = currentList[position] as Value

        holder.itemView.apply {
            val firstName = prefManager.firstName
            val lastName = prefManager.lastName

            myJokeTextView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String.format(
                    Html.fromHtml(item.joke, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                    firstName,
                    lastName
                )
            } else {
                String.format(Html.fromHtml(item.joke).toString(), firstName, lastName)
            }

            myJokeDelete.setOnClickListener {
                _deleteLiveData.value = item
            }
        }
    }

    fun clear() {
        submitList(listOf())
    }

    class MyJokesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
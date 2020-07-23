package com.andersenlab.paxfuljokes.ui.jokelist.adapter

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
import kotlinx.android.synthetic.main.holder_joke_list.view.*

class JokeListAdapter :
    ListAdapter<Value, JokeListAdapter.JokeListViewHolder>(
        Value.diffCallback
    ) {
    private val _likeLiveData = MutableLiveData<Value>()
    private val _shareLiveData = MutableLiveData<Value>()

    val likeLiveData: LiveData<Value> = _likeLiveData
    val shareLiveDate: LiveData<Value> = _shareLiveData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeListViewHolder {
        val holder = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_joke_list, parent, false)
        return JokeListViewHolder(holder)
    }

    override fun onBindViewHolder(holder: JokeListViewHolder, position: Int) {
        val item = currentList[position] as Value
        holder.itemView.apply {
            jokeListTextView.text =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(item.joke, HtmlCompat.FROM_HTML_MODE_LEGACY)
                } else {
                    Html.fromHtml(item.joke)
                }

            jokeListShare.setOnClickListener {
                _shareLiveData.value = item
            }
            jokeListLike.setOnClickListener {
                _likeLiveData.value = item
            }
        }
    }

    class JokeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
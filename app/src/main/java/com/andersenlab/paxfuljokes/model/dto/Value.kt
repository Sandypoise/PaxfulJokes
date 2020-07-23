package com.andersenlab.paxfuljokes.model.dto

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = arrayOf("joke"), unique = true)])
data class Value(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "joke") val joke: String
) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Value>() {

            override fun areItemsTheSame(oldItem: Value, newItem: Value): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Value, newItem: Value): Boolean {
                return oldItem.joke == newItem.joke
            }
        }
    }
}
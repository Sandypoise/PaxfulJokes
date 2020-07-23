package com.andersenlab.paxfuljokes.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andersenlab.paxfuljokes.model.dto.Value
import com.andersenlab.paxfuljokes.model.local.JokeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class DatabaseViewModel(private val jokeDao: JokeDao) : ViewModel() {

    fun addValueToDatabase(value: Value) {
        viewModelScope.launch(Dispatchers.IO) {
            jokeDao.insert(value)
        }
    }

    fun deleteValueToDatabase(value: Value) {
        viewModelScope.launch(Dispatchers.IO) {
            jokeDao.delete(value)
        }
    }
}
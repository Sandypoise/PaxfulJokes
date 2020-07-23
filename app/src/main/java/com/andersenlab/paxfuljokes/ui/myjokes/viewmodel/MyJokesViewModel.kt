package com.andersenlab.paxfuljokes.ui.myjokes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.andersenlab.paxfuljokes.model.dto.Value
import com.andersenlab.paxfuljokes.model.local.JokeDao
import com.andersenlab.paxfuljokes.model.local.PrefManager
import com.andersenlab.paxfuljokes.ui.common.DatabaseViewModel

class MyJokesViewModel(private val jokeDao: JokeDao, private val prefManager: PrefManager) :
    DatabaseViewModel(jokeDao) {

    val valueList: LiveData<List<Value>> = jokeDao.getJokes().switchMap { element ->
        liveData {
            emit(element)
        }
    }

    fun handlePositive(input: String) {
        if (input.isNotEmpty()) {
            val safetyString = prefManager.replaceNameWithPlaceholder(input)
            addValueToDatabase(
                Value(
                    joke = safetyString
                )
            )
        }
    }
}
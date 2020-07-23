package com.andersenlab.paxfuljokes.ui.jokelist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.andersenlab.paxfuljokes.model.dto.Joke
import com.andersenlab.paxfuljokes.model.dto.Value
import com.andersenlab.paxfuljokes.model.local.JokeDao
import com.andersenlab.paxfuljokes.model.remote.ApiService
import com.andersenlab.paxfuljokes.ui.common.DatabaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JokeListViewModel(private val api: ApiService, private val jokeDao: JokeDao) :
    DatabaseViewModel(jokeDao) {

    private val _randomValue = MutableLiveData<List<Value>>()

    val randomValue: LiveData<List<Value>> = _randomValue

    fun getRandomValue() {
        viewModelScope.launch(Dispatchers.IO) {
            _randomValue.postValue(jokeDao.getRandom())
        }
    }

    fun getJokeList(
        first: String? = null,
        last: String? = null,
        onError: (e: Exception) -> Unit = {}
    ): LiveData<Joke> {
        return liveData(Dispatchers.IO) {
            try {
                emit(api.getJokes(first, last))
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
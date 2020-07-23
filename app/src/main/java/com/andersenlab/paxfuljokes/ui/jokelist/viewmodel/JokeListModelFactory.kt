package com.andersenlab.paxfuljokes.ui.jokelist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andersenlab.paxfuljokes.model.local.JokeDao
import com.andersenlab.paxfuljokes.model.remote.ApiService

class JokeListModelFactory(private val api: ApiService, private val jokeDao: JokeDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return JokeListViewModel(
            api,
            jokeDao
        ) as T
    }
}
package com.andersenlab.paxfuljokes.ui.myjokes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andersenlab.paxfuljokes.model.local.JokeDao
import com.andersenlab.paxfuljokes.model.local.PrefManager

class MyJokesModelFactory(private val jokeDao: JokeDao, private val prefManager: PrefManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyJokesViewModel(
            jokeDao,
            prefManager
        ) as T
    }
}
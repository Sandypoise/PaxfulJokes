package com.andersenlab.paxfuljokes.ui.jokelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.andersenlab.paxfuljokes.model.dto.Joke
import com.andersenlab.paxfuljokes.model.local.JokeDao
import com.andersenlab.paxfuljokes.model.remote.ApiService
import com.andersenlab.paxfuljokes.ui.jokelist.viewmodel.JokeListViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class JokeListViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Mock
    private lateinit var observer: Observer<Joke>

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var jokeDao: JokeDao

    private lateinit var jokeListViewModel: JokeListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        jokeListViewModel =
            JokeListViewModel(
                apiService,
                jokeDao
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun testGetJokes() {
        runBlocking {
            launch(Dispatchers.Main) {
                jokeListViewModel.getJokeList("Nick", "Fury").observeForever(observer)
                delay(20)
                verify(observer).onChanged(null)
            }
        }
    }


}
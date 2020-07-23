package com.andersenlab.paxfuljokes.model.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andersenlab.paxfuljokes.model.dto.Value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class JokeDatabaseTest {

    private lateinit var jokeDao: JokeDao
    private lateinit var db: JokeDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, JokeDatabase::class.java
        ).build()
        jokeDao = db.jokeDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDeleteOneItem() {
        val value = Value(key = 33, joke = "Insert joke")
        jokeDao.insert(value)
        jokeDao.delete(value)
        val jokes = jokeDao.getJokes().getOrAwaitValue()
        assertThat(jokes.size, equalTo(0))
    }

    @Test
    @Throws(Exception::class)
    fun getRandomFromSingleItemBase() {
        val value = Value(joke = "Test joke")
        jokeDao.insert(value)
        val randomList = jokeDao.getRandom()
        assertThat(randomList.size, equalTo(1))
        val valueItem = randomList[0]
        assertThat(valueItem.joke, equalTo(value.joke))
    }


    private fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            observeForever(observer)
        }

        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }

}
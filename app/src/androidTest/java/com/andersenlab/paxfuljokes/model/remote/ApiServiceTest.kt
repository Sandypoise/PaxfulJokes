package com.andersenlab.paxfuljokes.model.remote

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class ApiServiceTest {

    private var mockWebServer = MockWebServer()

    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.close()
    }

    @Test
    @Throws(Exception::class)
    fun getJokesListTest() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(mockJson())

        mockWebServer.enqueue(response)
        runBlocking {
            val defaultJoke = apiService.getJokes(null, null)
            assertThat(defaultJoke.type, equalTo("success"))
            val value = defaultJoke.value!!
            assertThat(value.size, equalTo(2))
            assertThat(value[0].joke, containsString("Chuck Norris"))

        }
    }

    private fun mockJson(): String =
        getInstrumentation().targetContext.assets.open("response.json").bufferedReader()
            .use { it.readText() }
}
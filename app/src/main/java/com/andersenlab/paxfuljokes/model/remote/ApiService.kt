package com.andersenlab.paxfuljokes.model.remote

import com.andersenlab.paxfuljokes.model.dto.Joke
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("jokes")
    suspend fun getJokes(
        @Query("firstName") firstName: String?,
        @Query("lastName") lastName: String?
    ): Joke
}
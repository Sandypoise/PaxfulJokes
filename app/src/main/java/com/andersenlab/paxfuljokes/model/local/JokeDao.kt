package com.andersenlab.paxfuljokes.model.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andersenlab.paxfuljokes.model.dto.Value

@Dao
interface JokeDao {

    @Query("SELECT * FROM value")
    fun getJokes(): LiveData<List<Value>>

    @Query("SELECT * FROM value ORDER BY RANDOM() LIMIT 1")
    fun getRandom() : List<Value>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(value: Value)

    @Delete
    fun delete(value: Value)
}
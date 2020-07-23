package com.andersenlab.paxfuljokes.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andersenlab.paxfuljokes.model.dto.Value

private const val DATABASE_NAME = "joke_list"

@Database(entities = [Value::class], version = 1)
abstract class JokeDatabase : RoomDatabase() {

    abstract fun jokeDao(): JokeDao

    companion object {
        @Volatile
        private var instance: JokeDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            JokeDatabase::class.java, DATABASE_NAME
        ).build()
    }

}
package com.andersenlab.paxfuljokes

import android.app.Application
import com.andersenlab.paxfuljokes.model.remote.ApiService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(retrofitModule)
        }
    }

    private var retrofitModule = module {

        fun provideRetrofit(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.icndb.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }

        single { provideRetrofit() }
    }

}
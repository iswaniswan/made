package com.iswan.main.core.di

import com.iswan.main.core.BuildConfig
import com.iswan.main.core.data.source.remote.network.ApiService
import com.iswan.main.core.data.source.remote.network.Config
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideInterceptor(): OkHttpClient {
        val hostname = Config.HOSTNAME
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/iWoTtFo714hioRKSAzri5CEgQagcR06xGqVkRWcbh5Y=")
            .add(hostname, "sha256/M2pS3X16LmTc0kWi6ZbauzFyI46xRfQ3lN6NWxhNAM8=")
            .add(hostname, "sha256/Qfuuqw8z0Q5XohqInWAnnMLnrnSiyxXN2247iG4HzV0=")
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)

        if (BuildConfig.DEBUG) {
            okHttpClient
                .addInterceptor(HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideApiService(interceptor: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(Config.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(interceptor)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDispatchers(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}
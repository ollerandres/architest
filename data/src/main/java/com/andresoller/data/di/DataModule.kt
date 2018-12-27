package com.andresoller.data.di

import com.andresoller.data.remote.RemoteRepository
import com.andresoller.data.remote.RemoteRepositoryImpl
import com.andresoller.mlsearch.data.remote.ApiClient
import dagger.Module
import dagger.Provides
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    val BASE_URL = "http://jsonplaceholder.typicode.com/"

    @Provides
    @Singleton
    fun provideApiClient(): ApiClient {
        val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

        return retrofit.create(ApiClient::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(apiClient: ApiClient): RemoteRepository {
        return RemoteRepositoryImpl(apiClient)
    }
}
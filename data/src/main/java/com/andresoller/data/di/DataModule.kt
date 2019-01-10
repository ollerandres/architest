package com.andresoller.data.di

import android.content.Context
import com.andresoller.data.Repository
import com.andresoller.data.local.AppDatabase
import com.andresoller.data.local.LocalRepositoryImpl
import com.andresoller.data.remote.ApiClient
import com.andresoller.data.remote.RemoteRepositoryImpl
import com.andresoller.data.repositories.PostsRepository
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
    fun provideRemoteRepository(apiClient: ApiClient): Repository {
        return RemoteRepositoryImpl(apiClient)
    }

    @Provides
    @Singleton
    fun provideLocalRepository(appDatabase: AppDatabase): Repository {
        return LocalRepositoryImpl(appDatabase)
    }

    @Provides
    @Singleton
    fun providePostsRepository(remoteRepositoryImpl: RemoteRepositoryImpl, localRepositoryImpl: LocalRepositoryImpl): PostsRepository {
        return PostsRepository(remoteRepositoryImpl, localRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }
}
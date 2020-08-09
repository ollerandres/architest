package com.andresoller.data.di

import com.andresoller.data.mappers.postdetail.PostDetailMapperImpl
import com.andresoller.data.mappers.posts.PostMapperImpl
import com.andresoller.data.remote.ApiClient
import com.andresoller.data.remote.RemoteRepositoryImpl
import com.andresoller.data.remote.errors.ErrorHandlerImpl
import com.andresoller.data.remote.errors.SafeFunctionExecutor
import com.andresoller.data.remote.errors.SafeFunctionExecutorImpl
import com.andresoller.domain.error.ErrorHandler
import com.andresoller.domain.repositories.RemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataModule {

    val BASE_URL = "http://jsonplaceholder.typicode.com/"

    @Provides
    @Singleton
    fun provideErrorHandler(): ErrorHandler {
        return ErrorHandlerImpl()
    }

    @Provides
    @Singleton
    fun providePostMapper(): com.andresoller.data.mappers.posts.PostMapper {
        return PostMapperImpl()
    }

    @Provides
    @Singleton
    fun providePostDetailMapperImpl(): com.andresoller.data.mappers.postdetail.PostDetailMapper {
        return PostDetailMapperImpl()
    }

    @Provides
    @Singleton
    fun provideSafeFunctionExecutor(errorHandler: ErrorHandler): SafeFunctionExecutor {
        return SafeFunctionExecutorImpl(errorHandler)
    }

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
    fun provideRemoteRepository(
            apiClient: ApiClient,
            safeFunctionExecutor: SafeFunctionExecutor,
            postMapper: PostMapperImpl,
            postDetailMapper: PostDetailMapperImpl
    ): RemoteRepository {
        return RemoteRepositoryImpl(apiClient, safeFunctionExecutor, postMapper, postDetailMapper)
    }
}
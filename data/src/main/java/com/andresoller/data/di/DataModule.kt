package com.andresoller.data.di

import com.andresoller.data.remote.ApiClient
import com.andresoller.data.remote.RemoteRepository
import com.andresoller.data.remote.RemoteRepositoryImpl
import com.andresoller.device.NetworkUtils
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    private val BASE_URL = "http://jsonplaceholder.typicode.com/"
    private val cacheSize = (5 * 1024 * 1024).toLong()
    private val maxStale = 60 * 60 * 24 * 30
    private val maxAge = 60

    @Provides
    @Singleton
    fun provideApiClient(networkUtils: NetworkUtils): ApiClient {
        val client = OkHttpClient().newBuilder()
                .cache(Cache(networkUtils.getCacheDir(), cacheSize))
                .addInterceptor { chain ->
                    var request = chain.request()
                    if (!networkUtils.isNetworkAvailable()) {
                        request = request.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                                .removeHeader("Pragma")
                                .build()
                    }
                    chain.proceed(request)
                }.addNetworkInterceptor { chain ->
                    val response = chain.proceed(chain.request())
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=$maxAge")
                            .removeHeader("Pragma")
                            .build()
                }
                .build()

        val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()

        return retrofit.create(ApiClient::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(apiClient: ApiClient): RemoteRepository {
        return RemoteRepositoryImpl(apiClient)
    }
}
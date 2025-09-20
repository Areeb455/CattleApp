package com.cattlelabs.cattleapp.di

import android.content.Context
import com.cattlelabs.cattleapp.BuildConfig
import com.cattlelabs.cattleapp.data.Prefs
import com.cattlelabs.cattleapp.data.remote.CattleApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit // <-- IMPORT THIS
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePrefs(@ApplicationContext context: Context): Prefs = Prefs(context)

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {

        // Logging interceptor
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS) // For establishing a connection
            .readTimeout(90, TimeUnit.SECONDS)    // For reading a response
            .writeTimeout(90, TimeUnit.SECONDS)   // For writing a request (uploading)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): CattleApiService =
        retrofit.create(CattleApiService::class.java)
}
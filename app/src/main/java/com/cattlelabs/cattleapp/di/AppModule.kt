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
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A custom interceptor to automatically add the "Accept-Language" header
 * to every API request, based on the user's saved preference.
 */
class LanguageInterceptor @Inject constructor(
    private val prefs: Prefs
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val languageCode = prefs.getUserLanguage()

        val newRequest = originalRequest.newBuilder()
            .header("accept-language", languageCode)
            .build()

        return chain.proceed(newRequest)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePrefs(@ApplicationContext context: Context): Prefs = Prefs(context)

    @Singleton
    @Provides
    fun provideLanguageInterceptor(prefs: Prefs): LanguageInterceptor {
        return LanguageInterceptor(prefs)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(languageInterceptor: LanguageInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(languageInterceptor) // ✅ The language interceptor is now properly injected
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient) // This client now includes the language interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): CattleApiService =
        retrofit.create(CattleApiService::class.java)
}
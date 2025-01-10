package com.helisur.helisurapp.data.di

import com.google.gson.GsonBuilder
import com.helisur.helisurapp.domain.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(Constants.URLS.URL)
            .client( okhttpClient(""))
            .addConverterFactory(GsonConverterFactory.create(gson))
            //.addCallAdapterFactory(ResultCallAdapterFactory())
            .build()
    }

    //  @Inject
    //   lateinit var tokenManager: TokenManager

    class AuthInterceptor(token: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request().newBuilder()
            //       val token = tokenManager.getToken()
            //      requestBuilder.addHeader("Authorization", "Bearer $token")
            //  .header("Authorization","Bearer " + token).build();
            return chain.proceed(requestBuilder.build())
        }
    }


    private fun okhttpClient(token: String): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            //.addInterceptor(AuthInterceptor(token))
            .build()
    }

}
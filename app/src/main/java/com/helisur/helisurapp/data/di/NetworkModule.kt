package com.helisur.helisurapp.data.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.helisur.helisurapp.data.cloud.aeronaves.apis.AeronavesApiClient
import com.helisur.helisurapp.data.cloud.formatos.apis.FormatosApiClient
import com.helisur.helisurapp.data.cloud.usuario.apis.UsuarioApiClient
import com.helisur.helisurapp.data.cloud.usuario.model.parameter.LoginCloudParameter
import com.helisur.helisurapp.domain.util.Constants
import com.helisur.helisurapp.domain.util.SessionUserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    class AuthInterceptor(context: Context) : Interceptor {
        private val sessionManager = SessionUserManager(context)

        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request().newBuilder()
            sessionManager.getToken()?.let {
                requestBuilder.addHeader(
                    "Authorization", " Bearer $it"
                )
            }
            return chain.proceed(requestBuilder.build())
        }
    }

    class AuthenticateApi(context: Context) : Authenticator {
        private val sessionManager = SessionUserManager(context)
        override fun authenticate(route: Route?, response: Response): Request? {
            val apiClient = Retrofit.Builder().baseUrl(Constants.URLS.URL) // api base url
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(UsuarioApiClient::class.java)
            return runBlocking {
                // call login api again for getting accessToken in runBlocking so that it will wait until api response come
                val apiResponse = apiClient.obtieneTokenCloud(
                    LoginCloudParameter(
                        sessionManager.getUser()!!, sessionManager.getPass()!!
                    )
                )
                if (apiResponse.isSuccessful) {
                    val accessToken = apiResponse.body()!!.token
                    accessToken?.let {
                        sessionManager.saveAuthToken(accessToken)
                    }
                    response.request.newBuilder().addHeader(
                        "Authorization", " Bearer $accessToken"
                    ).build()
                } else {
                    null
                }
            }
        }
    }


    /**
     * Provides BaseUrl as string
     */
    @Singleton
    @Provides
    fun provideBaseURL(): String {
        return Constants.URLS.URL
    }

    /**
     * Provides LoggingInterceptor for api information
     */
    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    /**
     * Provides Auth interceptor for access token
     */
    @Singleton
    @Provides
    fun provideAuthInterceptor(@ApplicationContext context: Context): AuthInterceptor {
        return AuthInterceptor(context)
    }

    /**
     * Provides AuthenticateApi to check api is authenticate or not
     */
    @Singleton
    @Provides
    fun provideAuthenticateApi(@ApplicationContext context: Context): AuthenticateApi {
        return AuthenticateApi(context)
    }

    /**
     * Provides custom OkkHttp
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        authenticateApi: AuthenticateApi
    ): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()

        okHttpClient.callTimeout(60, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS)
        okHttpClient.readTimeout(60, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(60, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        okHttpClient.addInterceptor(authInterceptor)
        okHttpClient.authenticator(authenticateApi)
        okHttpClient.build()
        return okHttpClient.build()
    }

    /**
     * Provides converter factory for retrofit
     */
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    /**
     * Provides ApiServices client for Retrofit
     */
    @Singleton
    @Provides
    fun provideRetrofitClient(
        baseUrl: String, okHttpClient: OkHttpClient, converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(converterFactory).build()
    }


    @Singleton
    @Provides
    fun provideUsuarioApiService(retrofit: Retrofit): UsuarioApiClient =
        retrofit.create(UsuarioApiClient::class.java)


    @Singleton
    @Provides
    fun provideAeronavesApiService(retrofit: Retrofit): AeronavesApiClient =
        retrofit.create(AeronavesApiClient::class.java)


    @Singleton
    @Provides
    fun provideFormatosApiService(retrofit: Retrofit): FormatosApiClient =
        retrofit.create(FormatosApiClient::class.java)


}
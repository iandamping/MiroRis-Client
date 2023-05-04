package com.spe.miroris.di

import android.content.Context
import com.spe.miroris.BuildConfig
import com.spe.miroris.core.data.dataSource.remote.rest.ApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.DecryptInterceptor
import com.spe.miroris.core.data.dataSource.remote.rest.EncryptedApiInterface
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant
import com.spe.miroris.di.qualifier.DefaultApiInterfaceAnnotation
import com.spe.miroris.di.qualifier.DefaultOkHttpClientAnnotation
import com.spe.miroris.di.qualifier.EncryptedApiInterfaceAnnotation
import com.spe.miroris.di.qualifier.EncryptedOkHttpClientAnnotation
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    @DefaultOkHttpClientAnnotation
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        val builder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .cache(cache)


        return builder.apply {
            addInterceptor(logInterceptor)
        }.build()
    }

    @Singleton
    @Provides
    @EncryptedOkHttpClientAnnotation
    fun provideEncryptedOkHttpClient(interceptor: DecryptInterceptor, cache: Cache): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        val builder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .cache(cache)

        val decryptInterceptor = Interceptor {
            interceptor.intercept(it)
        }

        return builder.apply {
            addInterceptor(logInterceptor)
            addInterceptor(decryptInterceptor)
        }.build()
    }

    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache {
        return Cache(context.cacheDir, NetworkConstant.cacheSize)
    }

    @Provides
    @Singleton
    @DefaultApiInterfaceAnnotation
    fun provideRetrofit(
        @DefaultApiInterfaceAnnotation okHttpClient: OkHttpClient,
        moshi: Moshi
    ): ApiInterface = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(NetworkConstant.BASE_URL)
        .build()
        .create(ApiInterface::class.java)

    @Provides
    @Singleton
    @EncryptedApiInterfaceAnnotation
    fun provideEncryptedApiInterface(
        @EncryptedOkHttpClientAnnotation okHttpClient: OkHttpClient,
        moshi: Moshi
    ): EncryptedApiInterface = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(NetworkConstant.BASE_URL)
        .build()
        .create(EncryptedApiInterface::class.java)
}
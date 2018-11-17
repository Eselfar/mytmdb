package com.remiboulier.mytmdb.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.remiboulier.mytmdb.BuildConfig
import com.remiboulier.mytmdb.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Remi BOULIER on 17/11/2018.
 * email: boulier.r.job@gmail.com
 */

fun provideRetrofitClient(baseUrl: String, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory
                    .create(GsonBuilder()
                            // Allow the serialization of enums used as keys.
                            // Used to serialize ResidentialAddressType for example.
                            // See: https://github.com/google/gson/issues/473
                            .enableComplexMapKeySerialization()
                            .create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build();
}

fun provideOkHttpClient(context: Context): OkHttpClient {
    //Enable HTTP logging to logCat on DEBUG builds only
    val logging = HttpLoggingInterceptor()
    logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    return OkHttpClient.Builder()
            .connectTimeout(Constants.Network.TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
            .writeTimeout(Constants.Network.TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
            .readTimeout(Constants.Network.TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
            .addInterceptor(ConnectivityInterceptor(context))
            .addInterceptor(logging)
            .build()
}


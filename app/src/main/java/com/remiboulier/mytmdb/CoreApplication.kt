package com.remiboulier.mytmdb

import android.app.Application
import com.remiboulier.mytmdb.network.TMDbApi
import com.remiboulier.mytmdb.util.TMBdApiConstants
import com.remiboulier.mytmdb.util.provideOkHttpClient
import com.remiboulier.mytmdb.util.provideRetrofitClient

/**
 * Created by Remi BOULIER on 17/11/2018.
 * email: boulier.r.job@gmail.com
 */

class CoreApplication : Application() {

    lateinit var tmDbApi: TMDbApi

    override fun onCreate() {
        super.onCreate()

        val okHttpClient = provideOkHttpClient(this)
        tmDbApi = provideRetrofitClient(TMBdApiConstants.BASE_URL, okHttpClient)
                .create(TMDbApi::class.java)
    }
}
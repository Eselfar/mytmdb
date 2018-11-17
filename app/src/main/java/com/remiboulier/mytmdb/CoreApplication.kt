package com.remiboulier.mytmdb

import android.app.Application
import com.remiboulier.mytmdb.network.TMDbService

/**
 * Created by Remi BOULIER on 17/11/2018.
 * email: boulier.r.job@gmail.com
 */

class CoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        TMDbService.init(this, Constants.TMBdApi.BASE_URL)
    }
}
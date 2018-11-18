package com.remiboulier.mytmdb

/**
 * Created by Remi BOULIER on 17/11/2018.
 * email: boulier.r.job@gmail.com
 */

class Constants {

    companion object {
        const val GRID_COLUMNS = 3
        const val PAGE_SIZE = 20
    }

    class Network {

        companion object {
            const val TIMEOUT_IN_SECONDS = 20
        }
    }

    class TMBdApi {

        companion object {
            const val BASE_URL = "https://api.themoviedb.org/3/"
            const val KEY = "f41ca18f0599d432194cea5214a7f2bc"
        }
    }
}
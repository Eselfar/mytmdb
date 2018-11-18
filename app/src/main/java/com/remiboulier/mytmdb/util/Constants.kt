package com.remiboulier.mytmdb.util

import com.remiboulier.mytmdb.BuildConfig

/**
 * Created by Remi BOULIER on 17/11/2018.
 * email: boulier.r.job@gmail.com
 */

interface Constants {

    companion object {
        const val GRID_COLUMNS = 3
        const val PAGE_SIZE = 20
    }

    interface Network {
        companion object {
            const val TIMEOUT_IN_SECONDS = 20
        }
    }

    interface TMBdApi {
        companion object {
            const val BASE_URL = "https://api.themoviedb.org/3/"
            const val KEY = "f41ca18f0599d432194cea5214a7f2bc"
            const val BASE_URL_IMG = "https://image.tmdb.org/t/p/"
        }
    }

    interface Extra {
        companion object {
            const val MOVIE_ID = BuildConfig.APPLICATION_ID + ".MOVIE_ID"
        }
    }
}

enum class PosterSize(val size: String) {
    // Available sizes are "w92", "w154", "w185", "w342", "w500", "w780", or "original"
    SMALL("w92"),
    MEDIUM("w185"),
    LARGE("w500"),
    EXTRA_LARGE("w780")
}
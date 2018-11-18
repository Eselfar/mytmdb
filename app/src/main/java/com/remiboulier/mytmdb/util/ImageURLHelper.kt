package com.remiboulier.mytmdb.util

/**
 * Created by Remi BOULIER on 18/11/2018.
 * email: boulier.r.job@gmail.com
 */

class ImageURLHelper {

    companion object {
        fun getPosterUrl(path: String?) =
                if (path != null) Constants.TMBdApi.BASE_URL_IMG + PosterSize.MEDIUM.size + path else null

        fun getBackdropUrl(path: String?) =
                if (path != null) Constants.TMBdApi.BASE_URL_IMG + PosterSize.EXTRA_LARGE.size + path else null
    }
}
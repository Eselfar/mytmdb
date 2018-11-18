package com.remiboulier.mytmdb.extension

import com.remiboulier.mytmdb.util.PosterSize
import com.remiboulier.mytmdb.util.TMBdApiConstants

/**
 * Created by Remi BOULIER on 18/11/2018.
 * email: boulier.r.job@gmail.com
 */

fun String.getPosterUrl() = TMBdApiConstants.BASE_URL_IMG + PosterSize.MEDIUM.size + this

fun String.getBackdropUrl() = TMBdApiConstants.BASE_URL_IMG + PosterSize.EXTRA_LARGE.size + this


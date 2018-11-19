package com.remiboulier.mytmdb.extension

import com.remiboulier.mytmdb.util.PosterSize
import com.remiboulier.mytmdb.util.TMBdApiConstants
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Remi BOULIER on 19/11/2018.
 * email: boulier.r.job@gmail.com
 */
class StringExtensionsKtTest {

    @Test
    fun getPosterUrl_return_expected_value() {
        val partUrl = "/421337.jpg"

        val ref = TMBdApiConstants.BASE_URL_IMG + PosterSize.MEDIUM.size + partUrl

        assertEquals(ref, partUrl.getPosterUrl())
    }

    @Test
    fun getBackdropUrl_return_expected_value() {
        val partUrl = "/421337.jpg"

        val ref = TMBdApiConstants.BASE_URL_IMG + PosterSize.EXTRA_LARGE.size + partUrl

        assertEquals(ref, partUrl.getBackdropUrl())
    }
}
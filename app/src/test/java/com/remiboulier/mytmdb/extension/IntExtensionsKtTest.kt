package com.remiboulier.mytmdb.extension

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Remi BOULIER on 19/11/2018.
 * email: boulier.r.job@gmail.com
 */
class IntExtensionsKtTest {

    @Test
    fun toHourTime_min_only_lower_than_10() {
        val ref = "1 min"
        val toTest = 1

        assertEquals(ref, toTest.toHourTime())
    }

    @Test
    fun toHourTime_min_only_greater_than_10() {
        val ref = "49 min"
        val toTest = 49

        assertEquals(ref, toTest.toHourTime())
    }


    @Test
    fun toHourTime_hour_only_lower_than_10() {
        val ref = "2h00"
        val toTest = 2 * 60

        assertEquals(ref, toTest.toHourTime())
    }

    @Test
    fun toHourTime_hour_only_greater_than_10() {
        val ref = "12h00"
        val toTest = 12 * 60

        assertEquals(ref, toTest.toHourTime())
    }

    @Test
    fun toHourTime_hour_with_min_lower_than_10() {
        val ref = "2h01"
        val toTest = 2 * 60 + 1

        assertEquals(ref, toTest.toHourTime())
    }

    @Test
    fun toHourTime_hour_with_min_greater_than_10() {
        val ref = "2h42"
        val toTest = 2 * 60 + 42

        assertEquals(ref, toTest.toHourTime())
    }
}
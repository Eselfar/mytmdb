package com.remiboulier.mytmdb.extension

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Created by Remi BOULIER on 19/11/2018.
 * email: boulier.r.job@gmail.com
 */
class DateExtensionKtTest {

    @Test
    fun displayDate_has_valid_display() {
        val cal = Calendar.getInstance().also {
            it.set(2018, Calendar.FEBRUARY, 25)
        }
        val date = Date(cal.timeInMillis)

        val dateToCheck = "25 Feb 2018"
        val dateString = date.toReadableDate()

        assertEquals(dateToCheck, dateString)
    }
}
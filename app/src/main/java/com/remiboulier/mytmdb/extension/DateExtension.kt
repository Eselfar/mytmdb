package com.remiboulier.mytmdb.extension

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Remi BOULIER on 18/11/2018.
 * email: boulier.r.job@gmail.com
 */

fun Date.displayDate(): String {
    val df = SimpleDateFormat("dd MMM yyyy", Locale.UK)

    return df.format(this)
}
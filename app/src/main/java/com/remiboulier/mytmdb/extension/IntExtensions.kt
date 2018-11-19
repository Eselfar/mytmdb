package com.remiboulier.mytmdb.extension

/**
 * Created by Remi BOULIER on 18/11/2018.
 * email: boulier.r.job@gmail.com
 */

fun Int.toHourTime(): String {
    val h = this / 60
    val m = this.rem(60)

    return if (h > 0)
        String.format("%1\$dh%2$02d", h, m)
    else
        String.format("%1\$d min", m)
}
package com.remiboulier.mytmdb.extension

import android.content.Context
import com.remiboulier.mytmdb.R
import com.remiboulier.mytmdb.network.models.Genre

/**
 * Created by Remi BOULIER on 18/11/2018.
 * email: boulier.r.job@gmail.com
 */

fun List<Genre>.generateGenresString(context: Context): String {
    val builder = StringBuilder()
    for ((index, genre) in this.withIndex()) {
        builder.append(if (index == 0) "" else ", ").append(genre.name)
    }
    return context.getString(R.string.genres_list, builder)
}
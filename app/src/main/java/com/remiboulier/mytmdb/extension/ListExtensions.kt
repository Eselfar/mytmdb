package com.remiboulier.mytmdb.extension

import android.content.Context
import com.remiboulier.mytmdb.R
import com.remiboulier.mytmdb.network.models.Genre

/**
 * Created by Remi BOULIER on 18/11/2018.
 * email: boulier.r.job@gmail.com
 */

fun List<Genre>.generateGenresString(context: Context): String =
        context.getString(R.string.genres_list, genresToString(this))

fun genresToString(genres: List<Genre>): String {
    val builder = StringBuilder()
    for ((index, genre) in genres.withIndex()) {
        builder.append(if (index == 0) "" else ", ").append(genre.name)
    }
    return builder.toString()
}
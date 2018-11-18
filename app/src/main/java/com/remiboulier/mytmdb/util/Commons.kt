package com.remiboulier.mytmdb.util

import android.app.Activity
import android.content.Intent
import com.remiboulier.mytmdb.ui.details.MovieDetailsActivity

/**
 * Created by Remi BOULIER on 18/11/2018.
 * email: boulier.r.job@gmail.com
 */


fun goToMovieDetails(activity: Activity, movieId: Int) {
    val intent = Intent(activity, MovieDetailsActivity::class.java)
    intent.putExtra(Constants.Extra.MOVIE_ID, movieId)
    activity.startActivity(intent)
}
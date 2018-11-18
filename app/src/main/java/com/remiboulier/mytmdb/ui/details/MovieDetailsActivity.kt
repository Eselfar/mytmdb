package com.remiboulier.mytmdb.ui.details

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.remiboulier.mytmdb.R
import com.remiboulier.mytmdb.network.TMDbApi
import com.remiboulier.mytmdb.network.models.MovieDetails
import com.remiboulier.mytmdb.util.Constants
import com.remiboulier.mytmdb.util.GlideApp
import com.remiboulier.mytmdb.util.GlideRequests
import com.remiboulier.mytmdb.util.PosterSize
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailsActivity : AppCompatActivity() {

    lateinit var glideApp: GlideRequests

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        glideApp = GlideApp.with(this)

        TMDbApi.api.getMovieDetails(338952, Constants.TMBdApi.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateUI,
                        { t -> t.printStackTrace() })
    }

    fun updateUI(movie: MovieDetails) = with(movie) {
        uploadMovieImage(backdropPath, PosterSize.EXTRA_LARGE, detailsBackdrop)
        uploadMovieImage(posterPath, PosterSize.MEDIUM, detailsPoster, R.drawable.img_poster_empty)
        detailsTitle.text = title
        detailsReleaseDate.text = releaseDate
        detailsRunningTime.text = "2h50"// TODO: runtime
        detailsGenres.text = "Genres" // TODO
        detailsStatus.setText(status!!.resId)
        detailsOverview.text = overview
    }

    fun uploadMovieImage(partialUrl: String?, size: PosterSize, target: ImageView, @DrawableRes placeholder: Int? = null) {
        if (partialUrl != null) {
            val url = Constants.TMBdApi.BASE_URL_IMG + size.size + partialUrl
            val loaded = glideApp.load(url)
            if (placeholder != null)
                loaded.placeholder(placeholder)
            loaded.into(target);
        }
    }
}

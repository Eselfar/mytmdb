package com.remiboulier.mytmdb.ui.details

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import com.remiboulier.mytmdb.R
import com.remiboulier.mytmdb.network.TMDbApi
import com.remiboulier.mytmdb.network.models.BelongToCollection
import com.remiboulier.mytmdb.network.models.Collection
import com.remiboulier.mytmdb.network.models.MovieDetails
import com.remiboulier.mytmdb.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailsActivity : AppCompatActivity() {

    lateinit var glideApp: GlideRequests
    private lateinit var adapter: MovieDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val id = intent.getIntExtra(Constants.Extra.MOVIE_ID, 0)

        glideApp = GlideApp.with(this)
        adapter = MovieDetailsAdapter(mutableListOf(), glideApp) { movieId -> goToMovieDetails(this, movieId) }

        detailsCollectionTitle.visibility = View.GONE
        detailsCollectionRecycler.visibility = View.GONE
        detailsCollectionRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        detailsCollectionRecycler.adapter = adapter

        TMDbApi.api.getMovieDetails(id, Constants.TMBdApi.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { movie ->
                            if (movie.belongsToCollection != null) {
                                getCollection(movie.belongsToCollection)
                            }
                            updateMainUI(movie)
                        },
                        { t -> t.printStackTrace() })
    }

    private fun getCollection(btc: BelongToCollection) {
        TMDbApi.api.getCollection(btc.id!!, Constants.TMBdApi.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateCollectionUI,
                        { t -> t.printStackTrace() })
    }

    private fun updateCollectionUI(collection: Collection) {
        detailsCollectionTitle.visibility = View.VISIBLE
        detailsCollectionRecycler.visibility = View.VISIBLE
        adapter.update(collection.parts)
    }

    fun updateMainUI(movie: MovieDetails) = with(movie) {
        uploadMovieImage(ImageURLHelper.getBackdropUrl(backdropPath), detailsBackdrop)
        uploadMovieImage(ImageURLHelper.getPosterUrl(posterPath), detailsPoster, R.drawable.img_poster_empty)
        detailsTitle.text = title
        detailsReleaseDate.text = releaseDate
        detailsRunningTime.text = "2h50"// TODO: runtime
        detailsGenres.text = "Genres" // TODO
        detailsStatus.setText(status!!.resId)
        detailsOverview.text = overview
    }

    fun uploadMovieImage(url: String?, target: ImageView, @DrawableRes placeholder: Int? = null) {
        if (url != null) {
            val loaded = glideApp.load(url)
            if (placeholder != null)
                loaded.placeholder(placeholder)
            loaded.into(target);
        }
    }
}

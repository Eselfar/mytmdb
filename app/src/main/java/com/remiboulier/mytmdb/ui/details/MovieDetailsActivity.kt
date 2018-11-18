package com.remiboulier.mytmdb.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.remiboulier.mytmdb.CoreApplication
import com.remiboulier.mytmdb.R
import com.remiboulier.mytmdb.extension.*
import com.remiboulier.mytmdb.network.TMDbApi
import com.remiboulier.mytmdb.network.models.BelongToCollection
import com.remiboulier.mytmdb.network.models.Collection
import com.remiboulier.mytmdb.network.models.MovieDetails
import com.remiboulier.mytmdb.util.ExtraConstants
import com.remiboulier.mytmdb.util.GlideApp
import com.remiboulier.mytmdb.util.GlideRequests
import com.remiboulier.mytmdb.util.TMBdApiConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var glideApp: GlideRequests
    private lateinit var adapter: MovieDetailsAdapter
    private lateinit var tmDbApi: TMDbApi

    companion object {
        fun newIntent(context: Context, movieId: Int) =
                Intent(context, MovieDetailsActivity::class.java)
                        .also {
                            it.putExtra(ExtraConstants.MOVIE_ID, movieId)
                        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra(ExtraConstants.MOVIE_ID, 0)

        tmDbApi = (application as CoreApplication).tmDbApi
        glideApp = GlideApp.with(this)
        adapter = MovieDetailsAdapter(mutableListOf(), glideApp)
        { movieId -> startActivity(MovieDetailsActivity.newIntent(this, movieId)) }

        detailsCollectionTitle.visibility = View.GONE
        detailsCollectionRecycler.visibility = View.GONE
        detailsCollectionRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        detailsCollectionRecycler.adapter = adapter

        tmDbApi.getMovieDetails(id, TMBdApiConstants.KEY)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getCollection(btc: BelongToCollection) {
        tmDbApi.getCollection(btc.id!!, TMBdApiConstants.KEY)
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
        uploadMovieImage(backdropPath?.getBackdropUrl(), detailsBackdrop)
        uploadMovieImage(posterPath?.getPosterUrl(), detailsPoster, R.drawable.img_poster_empty)
        detailsTitle.text = title
        detailsReleaseDate.text = releaseDate?.displayDate()
        detailsRunningTime.text = runtime?.displayTime()
        detailsGenres.text = genres?.generateGenresString(this@MovieDetailsActivity)
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

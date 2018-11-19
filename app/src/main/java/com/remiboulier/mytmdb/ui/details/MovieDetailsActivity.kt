package com.remiboulier.mytmdb.ui.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
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
import com.remiboulier.mytmdb.network.models.Collection
import com.remiboulier.mytmdb.network.models.MovieDetails
import com.remiboulier.mytmdb.util.ExtraConstants
import com.remiboulier.mytmdb.util.GlideApp
import com.remiboulier.mytmdb.util.GlideRequests
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailsViewModel

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

        viewModel = getViewModel((application as CoreApplication).tmDbApi)

        val glideApp = GlideApp.with(this)
        initUI()
        initAdapter(glideApp)

        viewModel.movieLiveData.observe(this, Observer { updateMainUI(it!!, glideApp) })
        viewModel.collectionLiveData.observe(this, Observer { updateCollectionUI(it!!) })

        viewModel.requestMovieDetails(intent.getIntExtra(ExtraConstants.MOVIE_ID, 0))
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    fun getViewModel(tmDbApi: TMDbApi): MovieDetailsViewModel =
            ViewModelProviders.of(this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MovieDetailsViewModel(tmDbApi) as T
                }
            })[MovieDetailsViewModel::class.java]

    fun initAdapter(glideApp: GlideRequests) {
        val adapter = MovieDetailsAdapter(mutableListOf(), glideApp)
        { movieId -> startActivity(MovieDetailsActivity.newIntent(this, movieId)) }

        detailsCollectionRecycler.adapter = adapter
    }

    fun initUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailsCollectionTitle.visibility = View.GONE
        detailsCollectionRecycler.visibility = View.GONE
        detailsCollectionRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun updateCollectionUI(collection: Collection) {
        detailsCollectionTitle.visibility = View.VISIBLE
        detailsCollectionRecycler.visibility = View.VISIBLE
        (detailsCollectionRecycler.adapter as MovieDetailsAdapter).update(collection.parts)
    }

    fun updateMainUI(movie: MovieDetails,
                     glideApp: GlideRequests) = with(movie) {
        displayImage(backdropPath?.getBackdropUrl(), detailsBackdrop, glideApp)
        displayImage(posterPath?.getPosterUrl(), detailsPoster, glideApp, R.drawable.img_poster_empty)
        detailsTitle.text = title
        detailsReleaseDate.text = releaseDate?.displayDate()
        detailsRunningTime.text = runtime?.displayTime()
        detailsGenres.text = genres?.generateGenresString(this@MovieDetailsActivity)
        detailsStatus.setText(status!!.resId)
        detailsOverview.text = overview
    }

    fun displayImage(url: String?,
                     target: ImageView,
                     glideApp: GlideRequests,
                     @DrawableRes placeholder: Int? = null) {
        if (url != null) {
            val loaded = glideApp.load(url)
            if (placeholder != null)
                loaded.placeholder(placeholder)
            loaded.into(target);
        }
    }
}

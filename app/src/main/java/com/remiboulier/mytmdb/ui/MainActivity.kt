package com.remiboulier.mytmdb.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.remiboulier.mytmdb.CoreApplication
import com.remiboulier.mytmdb.R
import com.remiboulier.mytmdb.network.models.NPMovie
import com.remiboulier.mytmdb.network.repository.InMemoryByPageKeyRepository
import com.remiboulier.mytmdb.ui.details.MovieDetailsActivity
import com.remiboulier.mytmdb.util.GlideApp
import com.remiboulier.mytmdb.util.NowPlayingConstants
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: NowPlayingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeRecycler.layoutManager = GridLayoutManager(this, NowPlayingConstants.GRID_COLUMNS)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        homeRecycler.addItemDecoration(GridSpacingItemDecoration(NowPlayingConstants.GRID_COLUMNS, spacingInPixels, true))

        viewModel = getViewModel()
        initAdapter()
        viewModel.showSubreddit()
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = NowPlayingAdapter(glide,
                { movieId -> startActivity(MovieDetailsActivity.newIntent(this, movieId)) },
                { viewModel.retry() })

        homeRecycler.adapter = adapter

        viewModel.results.observe(this, Observer<PagedList<NPMovie>> {
            adapter.submitList(it)
        })
        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    private fun getViewModel(): NowPlayingViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val executor = Executors.newFixedThreadPool(5)
                val tmDbApi = (application as CoreApplication).tmDbApi
                val repo = InMemoryByPageKeyRepository(tmDbApi, executor)
                @Suppress("UNCHECKED_CAST")
                return NowPlayingViewModel(repo) as T
            }
        })[NowPlayingViewModel::class.java]
    }
}

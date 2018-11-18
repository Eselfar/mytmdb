package com.remiboulier.mytmdb

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.remiboulier.mytmdb.network.TMDbApi
import com.remiboulier.mytmdb.network.models.NPMovie
import com.remiboulier.mytmdb.repository.InMemoryByPageKeyRepository
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: NowPlayingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeRecycler.layoutManager = GridLayoutManager(this, Constants.GRID_COLUMNS)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        homeRecycler.addItemDecoration(GridSpacingItemDecoration(Constants.GRID_COLUMNS, spacingInPixels, true))

        viewModel = getViewModel()
        initAdapter()
        viewModel.showSubreddit()
    }


    private fun initAdapter() {
//        val glide = GlideApp.with(this) // TODO: Required later
        val adapter = MoviesAdapter() {
            viewModel.retry()
        }
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
                val repo = InMemoryByPageKeyRepository(TMDbApi.api, executor)
                @Suppress("UNCHECKED_CAST")
                return NowPlayingViewModel(repo) as T
            }
        })[NowPlayingViewModel::class.java]
    }
}

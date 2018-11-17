package com.remiboulier.mytmdb

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.remiboulier.mytmdb.network.TMDbService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeRecycler.layoutManager = GridLayoutManager(this, Constants.GRID_COLUMNS)
        homeRecycler.adapter = MoviesAdapter(mutableListOf())
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        homeRecycler.addItemDecoration(GridSpacingItemDecoration(Constants.GRID_COLUMNS, spacingInPixels, true))

        TMDbService.service
                .getNowPlaying(1, Constants.TMBdApi.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { np ->
                            np.results?.let { (homeRecycler.adapter as MoviesAdapter).update(it) }
                            // TODO: Use the result
                            Log.d("MainActivity", "Success")
                        },
                        { t -> t.printStackTrace() })
    }
}

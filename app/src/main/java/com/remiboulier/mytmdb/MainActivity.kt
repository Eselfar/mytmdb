package com.remiboulier.mytmdb

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.remiboulier.mytmdb.network.TMDbService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        homeRecycler.layoutManager = GridLayoutManager(this, 4)
//        homeRecycler.adapter = MoviesAdapter()

        TMDbService.get().getNowPlaying(1, Constants.TMBdApi.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _ -> Log.d("MainActivity", "Success") },
                        { t -> t.printStackTrace() })
    }
}

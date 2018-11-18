package com.remiboulier.mytmdb.network.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.remiboulier.mytmdb.network.TMDbApi
import com.remiboulier.mytmdb.network.models.NPMovie
import java.util.concurrent.Executor

/**
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI.
 */
class ResultDataSourceFactory(
        private val tmdbApi: TMDbApi,
        private val retryExecutor: Executor) : DataSource.Factory<Int, NPMovie>() {

    val sourceLiveData = MutableLiveData<PageKeyedNowPlayingDataSource>()

    override fun create(): DataSource<Int, NPMovie> =
            PageKeyedNowPlayingDataSource(tmdbApi, retryExecutor)
                    .also { sourceLiveData.postValue(it) }
}

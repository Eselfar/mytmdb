package com.remiboulier.mytmdb.network.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.remiboulier.mytmdb.network.TMDbApi
import com.remiboulier.mytmdb.network.models.NPMovie
import com.remiboulier.mytmdb.network.models.NowPlaying
import com.remiboulier.mytmdb.util.TMBdApiConstants
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

/**
 * Created by Remi BOULIER on 17/11/2018.
 * email: boulier.r.job@gmail.com
 */

class PageKeyedNowPlayingDataSource(
        private val tmDbApi: TMDbApi,
        private val retryExecutor: Executor) : PageKeyedDataSource<Int, NPMovie>() {


    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        retry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
        retry = null
    }

    override fun loadInitial(params: LoadInitialParams<Int>,
                             callback: LoadInitialCallback<Int, NPMovie>) {

        val request = tmDbApi.getNowPlaying(apiKey = TMBdApiConstants.KEY)
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            val data = request.execute().body()
            val items = data?.npMovies?.map { it } ?: emptyList()
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            val nextKey = generateNextKey(data?.page, data?.totalPages)
            callback.onResult(items, null, nextKey)

        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }

    }

    fun generateNextKey(page: Int?, totalPages: Int?): Int? =
            if (page != null && totalPages != null && page < totalPages)
                page + 1
            else null

    override fun loadAfter(params: LoadParams<Int>,
                           callback: LoadCallback<Int, NPMovie>) {

        networkState.postValue(NetworkState.LOADING)
        tmDbApi.getNowPlaying(
                params.key,
                TMBdApiConstants.KEY).enqueue(
                object : retrofit2.Callback<NowPlaying> {
                    override fun onFailure(call: Call<NowPlaying>, t: Throwable) {
                        retry = {
                            loadAfter(params, callback)
                        }
                        // TODO: Improve error handling
                        networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                    }

                    override fun onResponse(
                            call: Call<NowPlaying>,
                            response: Response<NowPlaying>) {

                        if (response.isSuccessful) {
                            val data = response.body()
                            val items = data?.npMovies?.map { it } ?: emptyList()
                            retry = null
                            val nextKey = generateNextKey(data?.page, data?.totalPages)
                            callback.onResult(items, nextKey)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            retry = {
                                loadAfter(params, callback)
                            }
                            networkState.postValue(
                                    NetworkState.error("error code: ${response.code()}"))
                        }
                    }
                }
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NPMovie>) {
        // ignored, since we only ever append to our initial load
    }
}

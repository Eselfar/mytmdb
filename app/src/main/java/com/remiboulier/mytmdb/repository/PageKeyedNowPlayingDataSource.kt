package com.remiboulier.mytmdb.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.remiboulier.mytmdb.Constants
import com.remiboulier.mytmdb.network.TMDbService
import com.remiboulier.mytmdb.network.models.NowPlaying
import com.remiboulier.mytmdb.network.models.Result
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

/**
 * Created by Remi BOULIER on 17/11/2018.
 * email: boulier.r.job@gmail.com
 */

class PageKeyedNowPlayingDataSource(
        private val tmDbService: TMDbService,
        private val retryExecutor: Executor) : PageKeyedDataSource<Int, Result>() {


    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>,
                             callback: LoadInitialCallback<Int, Result>) {
        val request = tmDbService.getNowPlaying(apiKey = Constants.TMBdApi.KEY)
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            val response = request.execute()
            val data = response.body()
            val items = data?.results?.map { it } ?: emptyList()
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
                           callback: LoadCallback<Int, Result>) {

        networkState.postValue(NetworkState.LOADING)
        tmDbService.getNowPlaying(
                params.key,
                Constants.TMBdApi.KEY).enqueue(
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
                            val items = data?.results?.map { it } ?: emptyList()
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {
        // ignored, since we only ever append to our initial load
    }
}

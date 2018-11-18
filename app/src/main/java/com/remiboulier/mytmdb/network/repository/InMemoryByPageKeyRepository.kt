package com.remiboulier.mytmdb.network.repository

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.support.annotation.MainThread
import com.remiboulier.mytmdb.network.TMDbApi
import com.remiboulier.mytmdb.network.models.NPMovie
import java.util.concurrent.Executor

/**
 * Repository implementation that returns a Listing that loads data directly from network by using
 * the previous / next page keys returned in the query.
 */
class InMemoryByPageKeyRepository(private val tmDbApi: TMDbApi,
                                  private val networkExecutor: Executor) : ResultsRepository {
    @MainThread
    override fun nowPlayingResults(pageSize: Int): Listing<NPMovie> {
        val sourceFactory = ResultDataSourceFactory(tmDbApi, networkExecutor)

        val myPagingConfig = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setPrefetchDistance(pageSize * 3)
                .setEnablePlaceholders(true)
                .build()

        // We use LivePagedListBuilder, we could also use toLiveData Kotlin extension function here
        val livePagedList =
                LivePagedListBuilder(sourceFactory, myPagingConfig)
                        .setFetchExecutor(networkExecutor)
                        .build()

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Listing(
                pagedList = livePagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                },
                retry = {
                    sourceFactory.sourceLiveData.value?.retryAllFailed()
                },
                refresh = {
                    sourceFactory.sourceLiveData.value?.invalidate()
                },
                refreshState = refreshState
        )
    }
}


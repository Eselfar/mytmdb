/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.remiboulier.mytmdb.repository

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


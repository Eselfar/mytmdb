package com.remiboulier.mytmdb.ui.details

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.remiboulier.mytmdb.network.TMDbApi
import com.remiboulier.mytmdb.network.models.BelongToCollection
import com.remiboulier.mytmdb.network.models.Collection
import com.remiboulier.mytmdb.network.models.MovieDetails
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * Created by Remi BOULIER on 19/11/2018.
 * email: boulier.r.job@gmail.com
 */
@RunWith(org.mockito.junit.MockitoJUnitRunner::class)
class MovieDetailsViewModelTest {

    companion object {

        /**
         * Prevent an error when trying to access AndroidSchedulers.mainThread()
         *
         *
         * See https://stackoverflow.com/a/43356315/1827254
         * and https://medium.com/@peter.tackage/overriding-rxandroid-schedulers-in-rxjava-2-5561b3d14212
         * for more information
         */
        @BeforeClass
        @JvmStatic
        fun setUpRxSchedulers() {
            val immediate = object : Scheduler() {
                override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                    // this prevents StackOverflowErrors when scheduling with a delay
                    return super.scheduleDirect(run, 0, unit)
                }

                override fun createWorker(): Scheduler.Worker {
                    return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
                }
            }

            RxJavaPlugins.setInitIoSchedulerHandler { immediate }
            RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
            RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
            RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
        }
    }

    @Mock
    lateinit var tmDbApi: TMDbApi

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun requestMovieDetails_get_movie_details() {
        val movieDetails = Mockito.mock(MovieDetails::class.java)
        Mockito.`when`(tmDbApi.getMovieDetails(anyInt(), anyString()))
                .thenReturn(Observable.just(movieDetails))

        val model = MovieDetailsViewModel(tmDbApi)
        model.requestMovieDetails(0)

        assertEquals(movieDetails, model.movieLiveData.value)
    }

    @Test
    fun getCollection_get_collection() {
        val collection = Mockito.mock(Collection::class.java)
        Mockito.`when`(tmDbApi.getCollection(anyInt(), anyString()))
                .thenReturn(Observable.just(collection))

        val model = MovieDetailsViewModel(tmDbApi)
        model.getCollection(mock(BelongToCollection::class.java))

        assertEquals(collection, model.collectionLiveData.value)
    }
}
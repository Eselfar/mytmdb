package com.remiboulier.mytmdb.ui.details

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.remiboulier.mytmdb.network.TMDbApi
import com.remiboulier.mytmdb.network.models.BelongToCollection
import com.remiboulier.mytmdb.network.models.Collection
import com.remiboulier.mytmdb.network.models.MovieDetails
import com.remiboulier.mytmdb.util.TMBdApiConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Remi BOULIER on 18/11/2018.
 * email: boulier.r.job@gmail.com
 */

class MovieDetailsViewModel(private val tmDbApi: TMDbApi) : ViewModel() {

    val movieLiveData = MutableLiveData<MovieDetails>()
    val collectionLiveData = MutableLiveData<Collection>()

    fun requestMovieDetails(movieId: Int) {
        tmDbApi.getMovieDetails(movieId, TMBdApiConstants.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { movie ->
                            if (movie.belongsToCollection != null) {
                                getCollection(movie.belongsToCollection)
                            }
                            movieLiveData.postValue(movie)
                        },
                        { t -> t.printStackTrace() })
    }

    fun getCollection(btc: BelongToCollection) {
        tmDbApi.getCollection(btc.id!!, TMBdApiConstants.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        collectionLiveData::postValue,
                        { t -> t.printStackTrace() })
    }
}
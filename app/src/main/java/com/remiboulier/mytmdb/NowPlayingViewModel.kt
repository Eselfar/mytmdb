package com.remiboulier.mytmdb

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.map
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import com.remiboulier.mytmdb.repository.ResultsRepository

/**
 * Created by Remi BOULIER on 17/11/2018.
 * email: boulier.r.job@gmail.com
 */

class NowPlayingViewModel(private val repository: ResultsRepository) : ViewModel() {

    private val resultsLiveData = MutableLiveData<String>()
    private val repoResult = map(resultsLiveData) {
        repository.nowPlayingResults(Constants.PAGE_SIZE)
    }

    val results = switchMap(repoResult, { it.pagedList })!!
    val networkState = switchMap(repoResult, { it.networkState })!!
    val refreshState = switchMap(repoResult, { it.refreshState })!!

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }

    fun showSubreddit(): Boolean {
        resultsLiveData.value = "test"
        return true
    }
}
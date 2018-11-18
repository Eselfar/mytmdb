package com.remiboulier.mytmdb.repository

import com.remiboulier.mytmdb.network.models.Result

interface ResultsRepository {

    fun nowPlayingResults(pageSize: Int): Listing<Result>
}

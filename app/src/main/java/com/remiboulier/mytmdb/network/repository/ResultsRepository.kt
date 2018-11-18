package com.remiboulier.mytmdb.network.repository

import com.remiboulier.mytmdb.network.models.NPMovie

interface ResultsRepository {

    fun nowPlayingResults(pageSize: Int): Listing<NPMovie>
}

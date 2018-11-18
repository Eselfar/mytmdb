package com.remiboulier.mytmdb.repository

import com.remiboulier.mytmdb.network.models.NPMovie

interface ResultsRepository {

    fun nowPlayingResults(pageSize: Int): Listing<NPMovie>
}

package com.remiboulier.mytmdb.network.models

import com.google.gson.annotations.SerializedName

data class NowPlaying(
        @SerializedName("page") val page: Int? = null,
        @SerializedName("results") val npMovies: List<NPMovie>? = null,
        @SerializedName("total_results") val totalResults: Int? = null,
        @SerializedName("total_pages") val totalPages: Int? = null
)
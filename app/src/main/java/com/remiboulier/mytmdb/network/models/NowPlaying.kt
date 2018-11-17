package com.remiboulier.mytmdb.network.models

import com.google.gson.annotations.SerializedName

data class NowPlaying(
        @SerializedName("page") var page: Int?,
        @SerializedName("results") val results: List<Result>?,
        @SerializedName("total_results") val totalResults: Int?,
        @SerializedName("total_pages") val totalPages: Int?
)
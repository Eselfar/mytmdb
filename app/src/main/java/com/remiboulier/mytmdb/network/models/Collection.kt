package com.remiboulier.mytmdb.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Remi BOULIER on 18/11/2018.
 * email: boulier.r.job@gmail.com
 */
data class Collection(
        @SerializedName("id") val id: Int? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("overview") val overview: String? = null,
        @SerializedName("poster_path") val posterPath: String? = null,
        @SerializedName("backdrop_path") val backdropPath: String? = null,
        @SerializedName("parts") val parts: List<Part>? = null
)

data class Part(
        @SerializedName("adult") val adult: Boolean? = null,
        @SerializedName("backdrop_path") val backdropPath: String? = null,
        @SerializedName("genre_ids") val genreIds: List<Int>? = null,
        @SerializedName("id") val id: Int? = null,
        @SerializedName("original_language") val originalLanguage: String? = null,
        @SerializedName("original_title") val originalTitle: String? = null,
        @SerializedName("overview") val overview: String? = null,
        @SerializedName("release_date") val releaseDate: String? = null,
        @SerializedName("poster_path") val posterPath: String? = null,
        @SerializedName("popularity") val popularity: Double? = null,
        @SerializedName("title") val title: String? = null,
        @SerializedName("video") val video: Boolean? = null,
        @SerializedName("vote_average") val voteAverage: Double? = null,
        @SerializedName("vote_count") val voteCount: Int? = null
)
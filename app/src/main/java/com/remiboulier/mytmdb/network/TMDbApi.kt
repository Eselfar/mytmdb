package com.remiboulier.mytmdb.network

import com.remiboulier.mytmdb.network.models.Collection
import com.remiboulier.mytmdb.network.models.MovieDetails
import com.remiboulier.mytmdb.network.models.NowPlaying
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Remi BOULIER on 17/11/2018.
 * email: boulier.r.job@gmail.com
 */

interface TMDbApi {

    @GET("movie/now_playing")
    fun getNowPlaying(@Query("page") page: Int = 1,
                      @Query("api_key") apiKey: String): Call<NowPlaying>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: Int,
                        @Query("api_key") apiKey: String): Observable<MovieDetails>

    @GET("collection/{collection_id}")
    fun getCollection(@Path("collection_id") collectionId: Int,
                      @Query("api_key") apiKey: String): Observable<Collection>
}
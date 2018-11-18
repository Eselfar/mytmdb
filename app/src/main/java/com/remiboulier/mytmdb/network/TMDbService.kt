package com.remiboulier.mytmdb.network

import android.content.Context
import com.remiboulier.mytmdb.network.models.NowPlaying
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Remi BOULIER on 17/11/2018.
 * email: boulier.r.job@gmail.com
 */

interface TMDbService {

    companion object {
        lateinit var service: TMDbService

        fun init(context: Context, baseUrl: String) {
            val okHttpClient = provideOkHttpClient(context)

            service = provideRetrofitClient(baseUrl, okHttpClient).create(TMDbService::class.java)
        }
    }

    @GET("movie/now_playing")
    fun getNowPlaying(@Query("page") page: Int = 1,
                      @Query("api_key") apiKey: String): Call<NowPlaying>

}
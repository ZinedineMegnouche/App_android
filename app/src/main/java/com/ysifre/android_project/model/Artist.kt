package com.ysifre.android_project.model

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Artists(
    val artists : List<Artist>
)

data class Artist (
    val idArtist: String,
    val strArtist: String,
    val strGenre: String,
    val strBiographyEN: String,
    val strCountry: String,
    val strArtistThumb: String
)

interface GetArtistByIdAPI{
    @GET("artist.php")
    fun findArtistInfos(@Query("i")id: Int) : Call<Artists>?
}

interface GetArtistByNameAPI{
    @GET("search.php")
    fun findArtistInfosByName(@Query("s")name: String) : Call<Artists>?
}

object GetArtistByIdNetwork{
    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GetArtistByIdAPI::class.java)

    suspend fun findArtist(id: Int): Artists? {
        return api.findArtistInfos(id)?.await()
    }
}

object GetArtistByNameNetwork{
    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GetArtistByNameAPI::class.java)

    suspend fun findArtistByName(name: String): Artists? {
        return api.findArtistInfosByName(name)?.await()
    }
}
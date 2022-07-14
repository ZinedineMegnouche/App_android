package com.ysifre.android_project

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ysifre.android_project.model.GetTrendingTracksAPI
import com.ysifre.android_project.model.Tracks
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Albums(
    val album: List<Album>,
    val trending: List<Album>
)

data class Album (
    val idAlbum: String,
    val strAlbum: String,
    val strAlbumStripped: String,
    val strArtist: String,
    val strArtistThumb: String,
    val intChartPlace: String,
    val idArtist: String,
    val strAlbumThumb: String
)

interface GetAlbumByIDAPI{
    @GET("album.php")
     fun findAlbum(@Query("i")id: Int) : Deferred<Albums>
}

interface GetTrendingAlbumsAPI{
    @GET("trending.php")
    fun findAlbumsTrend(@Query("country") country: String, @Query("type") type: String, @Query("format") format: String) : Call<Albums>
}

object GetTrendingAlbumsNetwork{
    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GetTrendingAlbumsAPI::class.java)

    suspend fun findTrendingAlbums(country: String, type: String, format: String): Albums {
        return api.findAlbumsTrend(country, type, format).await()
    }
}

object GetAlbumIdNetwork{
    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GetAlbumByIDAPI::class.java)

    suspend fun findAlbumById(id: Int): Albums {
        return api.findAlbum(id).await()
    }
}
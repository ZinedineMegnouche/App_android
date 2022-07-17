package com.ysifre.android_project.model

import android.net.Uri
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Tracks(
    val trending: List<Track>,
    val track: List<Track>
)

data class Track(
    val idTrend: String,
    val intChartPlace: String,
    val idArtist: String,
    val idAlbum: String,
    val strArtistMBID: String,
    val strAlbumMBID: String,
    val strArtist: String,
    val strAlbum: String,
    val strTrack: String,
    val strTrackThumb: String,
    val strCountry: String,
    val strType: String,
)

interface GetTrendingTracksAPI{
    @GET("trending.php")
    fun findTracksTrend(@Query("country") country: String, @Query("type") type: String, @Query("format") format: String) : Call<Tracks>
}

interface GetPopularTracksAPI{
    @GET("track-top10.php")
    fun findPopular(@Query("s")name: String) : Call<Tracks>
}

interface GetTracksByAlbumIdAPI{
    @GET("track.php")
    fun findTracks(@Query("m")id: Int) : Call<Tracks>
}

object GetTrendingTracksNetwork{
    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GetTrendingTracksAPI::class.java)

    suspend fun findTrendingTracks(country: String, type: String, format: String): Tracks {
        return api.findTracksTrend(country, type, format).await()
    }
}

object GetPopularTracksNetwork{
    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GetPopularTracksAPI::class.java)

    suspend fun findPopularTracks(name: String): Tracks {
        return api.findPopular(name).await()
    }
}

object GetTrackByAlbumIdNetwork{
    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GetTracksByAlbumIdAPI::class.java)

    suspend fun findPopularTracks(albumId: Int): Tracks {
        return api.findTracks(albumId).await()
    }
}
package com.ysifre.android_project

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ysifre.android_project.model.Artists
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
    val strDescriptionEN: String,
    val strDescriptionFR: String,
    val strDescriptionES: String,
    val strAlbum: String,
    val strAlbumStripped: String,
    val strArtist: String,
    val intYearReleased: String,
    val strArtistThumb: String,
    val intChartPlace: String,
    val idArtist: String,
    val strAlbumThumb: String,
    val intScore: String,
    val intScoreVotes: String
)

interface GetAlbumByArtistIDAPI{
    @GET("album.php")
     fun findAlbum(@Query("i")id: Int) : Deferred<Albums>
}

interface GetAlbumByIdAPI{
    @GET("album.php")
    fun findAlbumById(@Query("m")id: Int) : Deferred<Albums>
}

interface GetTrendingAlbumsAPI{
    @GET("trending.php")
    fun findAlbumsTrend(@Query("country") country: String, @Query("type") type: String, @Query("format") format: String) : Call<Albums>
}

interface GetDiscographyAPI{
    @GET("searchalbum.php")
    fun findDiscography(@Query("s")name: String) : Call<Albums>
}

interface GetAlbumDetailsFromArtistAndAlbumNamesAPI{
    @GET("searchalbum.php")
    fun findAlbumWithArtistAndAlbum(@Query("s")artistName: String, @Query("a") albumName: String) : Call<Albums>
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
        .create(GetAlbumByArtistIDAPI::class.java)

    suspend fun findAlbumByArtistId(id: Int): Albums {
        return api.findAlbum(id).await()
    }
}

object GetAlbumByIdNetwork{
    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GetAlbumByIdAPI::class.java)

    suspend fun findAlbumById(id: Int): Albums {
        return api.findAlbumById(id).await()
    }
}

object GetDiscographyNetwork{
    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GetDiscographyAPI::class.java)

    suspend fun getDiscographyByName(name: String): Albums {
        return api.findDiscography(name)?.await()
    }
}

object GetAlbumsFromArtistAndAblbumNamesNetwork{
    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GetAlbumDetailsFromArtistAndAlbumNamesAPI::class.java)

    suspend fun getAlbumsInfosWithArtistAndAlbum(artistName: String, albumName: String): Albums {
        return api.findAlbumWithArtistAndAlbum(artistName, albumName)?.await()
    }
}
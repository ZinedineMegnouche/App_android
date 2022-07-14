package com.ysifre.android_project.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

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
    fun findAlbum(@Query("i")id: String) : Call<Artist>?
}
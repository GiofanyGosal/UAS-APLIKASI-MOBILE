package com.example.aplikasifilm.api

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class FilmGenre (
    val id: Int,
    val name: String
)

data class MovieDetail (
    val title: String,
    @Json(name = "overview") val description: String,
    @Json(name = "poster_path") val posterPath: String,
    @Json(name = "original_title") val originalTitle: String,
    @Json(name = "vote_average") val averageVote: Double,
    @Json(name = "runtime") val movieDuration: Int, // in minute(s)
    @Json(name = "tagline") val tagline: String,
    val genres: List<FilmGenre>,
)

interface FilmDetailService {
    @GET("{type}/{film_id}")
    suspend fun getFilmDetail(
        @Path("type") filmType: String,
        @Path("film_id") filmId: Int,
        @Query("api_key") apiKey: String,
    ): MovieDetail
}
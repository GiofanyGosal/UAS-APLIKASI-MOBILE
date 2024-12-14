package com.example.aplikasifilm.api

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class TopRatedMovieResult (
    val id: Int,
    val title: String,
    @Json(name = "overview") val description: String,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "vote_average") val voteAverage: Double
)

data class TopRatedMovieResultPage (
    @Json(name = "page") val pageNum: Int,
    val results: List<TopRatedMovieResult>
)

interface TopRatedMovieService {
    @GET("{type}/top_rated")
    suspend fun getPopularFilm(
        @Path("type") filmType: String,
        @Query("api_key") apiKey: String,
    ): TopRatedMovieResultPage
}
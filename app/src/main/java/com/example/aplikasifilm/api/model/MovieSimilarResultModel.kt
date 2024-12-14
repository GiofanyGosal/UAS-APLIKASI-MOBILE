package com.example.aplikasifilm.api.model

import com.squareup.moshi.Json

data class MovieSimilarResult (
    val id: Int,
    val title: String,
    @Json(name = "overview") val description: String,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "vote_average") val voteAverage: Double
)

data class MovieSimilarResultModel(
    @Json(name = "page") val pageNum: Int,
    val results: List<MovieSimilarResult>
)

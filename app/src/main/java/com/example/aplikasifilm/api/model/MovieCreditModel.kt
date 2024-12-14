package com.example.aplikasifilm.api.model

import com.squareup.moshi.Json

data class MovieCastInfo (
    val name: String,
    @Json(name = "profile_path") val profileImagePath: String?,
    @Json(name = "character") val characterName: String,
)

data class MovieCreditModel(
    @Json(name = "id") val movieId: Int,
    @Json(name = "cast") val casts: List<MovieCastInfo>
)

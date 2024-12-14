package com.example.aplikasifilm.api.model

import com.squareup.moshi.Json

data class Moviekeyword (
    val id: Int,
    val name: String
)

data class MovieKeywordResult(
    @Json(name = "id") val movieId: Int,
    val keywords: List<Moviekeyword>,
)

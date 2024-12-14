package com.example.aplikasifilm.api

import retrofit2.http.GET
import retrofit2.http.Query

interface MovieSearchService {
    @GET("https://api.themoviedb.org/3/search/movie")
    suspend fun getMovieSearch(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
    ): MovieResultPage
}
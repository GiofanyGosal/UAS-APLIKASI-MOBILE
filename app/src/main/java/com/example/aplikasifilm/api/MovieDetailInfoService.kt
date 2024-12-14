package com.example.aplikasifilm.api

import com.example.aplikasifilm.api.model.MovieCreditModel
import com.example.aplikasifilm.api.model.MovieKeywordResult
import com.example.aplikasifilm.api.model.MovieRecommendationModel
import com.example.aplikasifilm.api.model.MovieSimilarResultModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieDetailInfoService {
    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendation(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): MovieRecommendationModel

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): MovieSimilarResultModel


    @GET("movie/{movie_id}/images")
    suspend fun getMovieImage(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    )

    @GET("movie/{movie_id}/external_ids")
    suspend fun getExternalId(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    )

    @GET("movie/{movie_id}/keywords")
    suspend fun getMovieKeyword(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): MovieKeywordResult

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredit(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): MovieCreditModel
}
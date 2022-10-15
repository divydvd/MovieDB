package com.divy.moviedb

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("/3/movie/popular")
    suspend fun getPopularMovies(@Query("api_key") api_key: String): Response<PopularMoviesResponse>
}
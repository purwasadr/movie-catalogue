package com.alurwa.moviecatalogue.core.data.source.remote.network

import com.alurwa.moviecatalogue.BuildConfig
import com.alurwa.moviecatalogue.core.data.source.remote.response.MovieDetailResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.ListMovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("3/discover/movie?api_key=" + BuildConfig.API_KEY)
    suspend fun getAllMovies(): ListMovieResponse

    @GET("3/discover/movie?api_key=" + BuildConfig.API_KEY)
    suspend fun getDiscoverMovies(@Query("page")pagePos: Int): ListMovieResponse

    @GET("3/movie/{type}?api_key=" + BuildConfig.API_KEY)
    suspend fun getMovies(@Path("type")type: String, @Query("page")pagePos: Int): ListMovieResponse

    @GET("3/search/movie?api_key=${BuildConfig.API_KEY}")
    suspend fun searchMovies(@Query("query")query: String,  @Query("page")pagePos: Int): ListMovieResponse

    @GET("3/movie/{id}?api_key=" + BuildConfig.API_KEY + "&append_to_response=credits")
    suspend fun getDetailMovie(@Path("id")id: Int): MovieDetailResponse

    @GET("3/discover/tv?api_key=" + BuildConfig.API_KEY)
    suspend fun getAllTvShows()


}
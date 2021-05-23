package com.alurwa.moviecatalogue.core.data

import com.alurwa.moviecatalogue.core.data.source.remote.IRemoteDataSource
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiResponse
import com.alurwa.moviecatalogue.core.data.source.remote.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Purwa Shadr Al 'urwa on 22/05/2021
 */

class FakeRemoteDataSource() : IRemoteDataSource {

    private val filmDetail1 = FilmDetailResponse(
        id = 111,
        title = "Kimi no Nawa",
        overview = "Kimi no namaewa mitsuha-mitsuha",
        posterPath = "www.iniPathPoster.com",
        backdropPath = "www.iniBackDropPath.com",
        releaseDate = "19-04-2002",
        revenue = 1000000000,
        budget = 2000000000,
        runtime = 120,
        originalLanguage = "Japanese",
        status = "Released",
        voteAverage = 8.5,
        genres = listOf(
            GenreResponse(123, "Animation"),
            GenreResponse(122, "Romance")
        ),
        credits = CreditResponse(
            cast = listOf(
                CastResponse(
                    "Nao Toyamaka",
                    "www.profilePath.com",
                    "Mitsuha Miyamizu"
                ),
                CastResponse(
                    "Bocah mboh",
                    "www.profilePath2.com",
                    "Taki Kun"
                )
            )
        )
    )

    override fun getFilmDetail(id: Int) = flow<ApiResponse<FilmDetailResponse>> {
        val results = filmDetail1
        emit(ApiResponse.Success(results))
    }.flowOn(Dispatchers.Unconfined)

    override fun getTvDetail(id: Int) = flow<ApiResponse<TvDetailResponse>> {
        try {
            // val results = apiService.getTvDetail(id)
            // emit(ApiResponse.Success(results))

        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.Unconfined)
}
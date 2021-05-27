package com.alurwa.moviecatalogue.main

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.core.model.CarouselMenu
import com.alurwa.moviecatalogue.core.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IMovieCatalogueRepository
) : ViewModel() {

    var currentSort: MovieSortEnum? = null

    var currentMovieResult: Flow<PagingData<Movie>>? = null

    private var currentTvResult: Flow<PagingData<Movie>>? = null

    private var currentTvSort: MovieSortEnum? = null

    var listState: Parcelable? = null

    var chipState: Int? = null

    //  val filmMenu = repository.getFilmMenu()
  //  val _currentFilmMenu: MutableStateFlow<List<PagingData<Movie>>> = MutableStateFlow(emptyList())

  //  val currentFilmMenu: StateFlow<List<PagingData<Movie>>> get() = _currentFilmMenu

    val filmMenu = flow {
        val carouselList = listOf(
            CarouselMenu(
                "Discover",
                repository.getFilms(MovieSortEnum.DISCOVER)
                    .cachedIn(viewModelScope).first()
            ),
            CarouselMenu(
                "Popular",
                repository.getFilms(MovieSortEnum.POPULAR)
                    .cachedIn(viewModelScope).first()
            ),
            CarouselMenu(
                "Upcoming",
                repository.getFilms(MovieSortEnum.UPCOMING)
                    .cachedIn(viewModelScope).first()
            ),
            CarouselMenu(
                "Top Rating",
                repository.getFilms(MovieSortEnum.TOP_RATING)
                    .cachedIn(viewModelScope).first()
            )
        )
        Timber.d("Masuk Flow")

        emit(carouselList)
    }.shareIn(viewModelScope, SharingStarted.Lazily,1)

    val tvMenu = flow {
        val carouselList = listOf(
            CarouselMenu(
                "Discover",
            repository.getTvList(MovieSortEnum.DISCOVER)
                .cachedIn(viewModelScope).first()
            ),
            CarouselMenu(
                "Popular",
                repository.getTvList(MovieSortEnum.POPULAR)
                .cachedIn(viewModelScope).first()
            ),
            CarouselMenu(
                "Upcoming",
            repository.getTvList(MovieSortEnum.UPCOMING)
                .cachedIn(viewModelScope).first()
            ),
            CarouselMenu(
                "Top Rating",
            repository.getTvList(MovieSortEnum.TOP_RATING)
                .cachedIn(viewModelScope).first()
            )
        )
        Timber.d("Masuk Flow")

        emit(carouselList)
    }.shareIn(viewModelScope, SharingStarted.Lazily,1)

   /* fun getFilmMenu() =
        currentFilmMenu?.let {
            currentFilmMenu
        } ?: repository.getFilmMenu().also {
            currentFilmMenu = it
        }

    */

  /*  init {
        viewModelScope.launch {
            val aww = listOf(
                async {
                    repository.getFilms(MovieSortEnum.DISCOVER).cachedIn(viewModelScope).first()
                },
                async {
                    repository.getFilms(MovieSortEnum.POPULAR).cachedIn(viewModelScope).first()
                },
                async {
                    repository.getFilms(MovieSortEnum.TOP_RATING).cachedIn(viewModelScope).first()
                })

            val result = aww.awaitAll()

            _currentFilmMenu.value = result
        }
    }

   */

    suspend fun getFilmNestedVp(): List<PagingData<Movie>> {
        val aww = listOf(
            viewModelScope.async {
                repository.getFilms(MovieSortEnum.DISCOVER).cachedIn(viewModelScope).first()
            },
            viewModelScope.async {
                repository.getFilms(MovieSortEnum.TOP_RATING).cachedIn(viewModelScope).first()
            })

        val result = aww.awaitAll()

        return result
    }

    fun getFilm(sortEnum: MovieSortEnum): Flow<PagingData<Movie>> {
        val movieResult = currentMovieResult

        if (sortEnum == currentSort && movieResult != null) {
            return movieResult
        }

        currentSort = sortEnum

        val newResult = repository.getFilms(sortEnum).cachedIn(viewModelScope)

        currentMovieResult = newResult

        return newResult
    }

    fun getTv(sortEnum: MovieSortEnum): Flow<PagingData<Movie>> {
        val tvResult = currentTvResult

        if (sortEnum == currentTvSort && tvResult != null) {
            return tvResult
        }

        currentTvSort = sortEnum

        val newResult = repository.getTvList(sortEnum).cachedIn(viewModelScope)

        currentTvResult = newResult

        return newResult
    }
}
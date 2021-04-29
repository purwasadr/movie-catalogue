package com.alurwa.moviecatalogue.main

enum class MovieSortEnum(val code: String) {
    DISCOVER(code = "popularity.desc"),
    POPULAR(code = "popular"),
    UPCOMING(code = "upcoming"),
    TOP_RATING(code = "top_rated")
}

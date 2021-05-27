package com.alurwa.moviecatalogue.core.model

import androidx.paging.PagingData

/**
 * Created by Purwa Shadr Al 'urwa on 26/05/2021
 */

data class CarouselMenu(
    val title: String,
    val pagingData: PagingData<Movie>
) {
}
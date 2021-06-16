/*
 * MIT License
 *
 * Copyright (c) 2021 Purwa Shadr Al 'urwa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.alurwa.moviecatalogue.core.data.source.local

import com.alurwa.moviecatalogue.core.data.source.local.entity.FilmDetailEntity
import com.alurwa.moviecatalogue.core.data.source.local.entity.TvDetailEntity
import com.alurwa.moviecatalogue.core.data.source.local.room.FilmDetailDao
import com.alurwa.moviecatalogue.core.data.source.local.room.MovieCatalogueDatabase
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val filmDetailDao: FilmDetailDao,
    private val database: MovieCatalogueDatabase
) : ILocalDataSource {

    private val tvDetailDao = database.tvDetailDao()

    override fun getFilmDetail(id: Int) = filmDetailDao.getFilmDetail(id)

    override suspend fun insertFilmDetail(filmDetailEntity: FilmDetailEntity) {
        filmDetailDao.insertFilmDetail(filmDetailEntity)
    }

    override fun getTvDetail(id: Int) = tvDetailDao.getTvDetail(id)

    override suspend fun insertTvDetail(tvDetailEntity: TvDetailEntity) {
        tvDetailDao.insertTvDetail(tvDetailEntity)
    }
}

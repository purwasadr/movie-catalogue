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

package com.alurwa.moviecatalogue.core.data

import android.content.Context
import com.alurwa.moviecatalogue.core.data.source.local.ILocalDataSource
import com.alurwa.moviecatalogue.core.data.source.remote.IRemoteDataSource
import com.alurwa.moviecatalogue.core.data.source.remote.network.ApiService
import com.alurwa.moviecatalogue.utils.DataDummy
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class RepositoryTest {
    private lateinit var tasksRemoteDataSource: IRemoteDataSource
    private lateinit var tasksLocalDataSource: ILocalDataSource
    private lateinit var movieCatalogueRepository: IMovieCatalogueRepository
    private val context = mock(Context::class.java)
    private val apiService = mock(ApiService::class.java)

    @Before
    fun createRepository() {
        tasksRemoteDataSource = FakeRemoteDataSource()
        tasksLocalDataSource = FakeLocalDataSource()
        // Get a reference to the class under test
        movieCatalogueRepository = MovieCatalogueRepository(
            // TODO Dispatchers.Unconfined should be replaced with Dispatchers.Main
            //  this requires understanding more about coroutines + testing
            //  so we will keep this as Unconfined for now.
            tasksRemoteDataSource, tasksLocalDataSource, context, apiService
        )
    }

    @Test
    fun getFilmDetail() = runBlocking {
        val result = movieCatalogueRepository.getFilmDetail(111).toList()

        val data = result.get(1) as Resource.Success
        println(data.data.toString())

        assertThat(data.data, `is`(DataDummy.getFilmDetail()))
        println("swwweq")
    }
}

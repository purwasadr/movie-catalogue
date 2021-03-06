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

package com.alurwa.moviecatalogue.core.di

import com.alurwa.moviecatalogue.core.data.IMovieCatalogueRepository
import com.alurwa.moviecatalogue.core.data.MovieCatalogueRepository
import com.alurwa.moviecatalogue.core.data.source.local.ILocalDataSource
import com.alurwa.moviecatalogue.core.data.source.local.LocalDataSource
import com.alurwa.moviecatalogue.core.data.source.remote.IRemoteDataSource
import com.alurwa.moviecatalogue.core.data.source.remote.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun provideMovieCatalogueRepository(
        movieCatalogueRepository: MovieCatalogueRepository
    ): IMovieCatalogueRepository

    @Singleton
    @Binds
    abstract fun provideRemoteDataSource(
        remoteDataSource: RemoteDataSource
    ): IRemoteDataSource

    @Singleton
    @Binds
    abstract fun provideLocalDataSource(
        localDataSource: LocalDataSource
    ): ILocalDataSource
}

package com.alurwa.moviecatalogue.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alurwa.moviecatalogue.core.data.source.local.entity.TvDetailEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Purwa Shadr Al 'urwa on 22/05/2021
 */

@Dao
interface TvDetailDao {

    @Query("SELECT * FROM tv_detail WHERE id = :tvId")
    fun getTvDetail(tvId: Int): Flow<TvDetailEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvDetail(tvDetail: TvDetailEntity)

    @Query("DELETE FROM tv_detail")
    suspend fun deleteAllDetailTv()
}
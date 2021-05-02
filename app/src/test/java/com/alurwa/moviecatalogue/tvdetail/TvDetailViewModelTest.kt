package com.alurwa.moviecatalogue.tvdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alurwa.moviecatalogue.getOrAwaitValue
import com.alurwa.moviecatalogue.utils.FakeRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TvDetailViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getTvDetail() {

        val tvDetailViewModel = TvDetailViewModel(FakeRepository())

        tvDetailViewModel.getTvDetail(12)
        val value = tvDetailViewModel.tvDetail?.getOrAwaitValue()


        assertThat(value, not(nullValue()))
    }
}
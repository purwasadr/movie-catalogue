package com.alurwa.moviecatalogue.tvdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TvDetailViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getTvDetail() {

       /* val tvDetailViewModel = TvDetailViewModel(FakeRepository(), stateHandle = SavedStateHandle())

        tvDetailViewModel.tvDetail(12)
        val value = tvDetailViewModel.tvDetail?.getOrAwaitValue()


        assertThat(value, not(nullValue()))

        */
    }
}
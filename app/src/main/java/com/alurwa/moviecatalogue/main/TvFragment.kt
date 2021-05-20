package com.alurwa.moviecatalogue.main

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.alurwa.moviecatalogue.tvdetail.TvDetailActivity
import com.alurwa.moviecatalogue.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TvFragment : MovieFragmentAbstract() {

    companion object {
        const val TAG = "TvFragment"
    }

    override fun navigateToDetail(extraId: Int) {
        Intent(requireContext(), TvDetailActivity::class.java)
            .putExtra(Constants.EXTRA_ID, extraId)
            .also { requireContext().startActivity(it) }
    }

    override fun getMovies(sortEnum: MovieSortEnum) {
        lifecycleScope.launch {
            viewModel.getTv(sortEnum).collectLatest {
                adapter.submitData(it)
            }
        }
    }
}
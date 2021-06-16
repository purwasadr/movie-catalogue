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

package com.alurwa.moviecatalogue.main

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.moviecatalogue.core.adapter.MovieAdapter
import com.alurwa.moviecatalogue.core.model.CarouselMenu
import com.alurwa.moviecatalogue.databinding.FragmentMovieBinding
import com.alurwa.moviecatalogue.databinding.ListNestedCarouselItemBinding
import com.alurwa.moviecatalogue.utils.CommonUtil
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class MovieFragmentAbstract : Fragment() {
    private var _binding: FragmentMovieBinding? = null

    private val binding get() = _binding!!

    val viewModel by activityViewModels<MainViewModel>()

    var arrayAdapter: Array<MovieAdapter>? = null

    var arrayLoadState: Array<LoadState?> = arrayOf(null, null, null, null)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRetryBtn()
        getCarousels()
    }

    abstract fun navigateToDetail(extraId: Int)

    abstract fun navigateToList(which: Int)

    fun submitList(list: List<CarouselMenu>) {
        arrayAdapter = Array(list.size) { pos ->
            MovieAdapter(true) { id ->
                navigateToDetail(id)
            }.also { movieAdapter ->
                lifecycleScope.launch {
                    movieAdapter.submitData(list[pos].pagingData)
                }
            }
        }

        setupList(list)
    }

    // Fill the list of carousels with LinearLayout instead of using RecyclerView
    // because LinearLayout never recycling item view
    private fun setupList(list: List<CarouselMenu>) {
        arrayAdapter?.forEachIndexed { index, movieAdapter ->
            val view = ListNestedCarouselItemBinding.inflate(layoutInflater)

            view.txtTitle.text = list.get(index).title

            view.rcv.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            view.rcv.setHasFixedSize(true)

            GravitySnapHelper(Gravity.START).also { snapHelper ->
                snapHelper.snapToPadding = true
                snapHelper.maxFlingDistance = CommonUtil.dpToPx(requireContext(), 200)
            }.attachToRecyclerView(view.rcv)

            view.rcv.adapter = movieAdapter

            view.llHeader.setOnClickListener {
                navigateToList(index)
            }

            setupLoadingState(movieAdapter, index)

            binding.llList.addView(view.root)
        }
    }

    abstract fun getCarousels()

    // Handle Progress Bar
    private fun setupLoadingState(movieAdapter: MovieAdapter, index: Int) {
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // This happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                movieAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .collect {
                        arrayLoadState[index] = it.refresh

                        val isNotLoading = arrayLoadState.all { loadState ->
                            Timber.d((loadState is LoadState.NotLoading).toString())
                            loadState is LoadState.NotLoading
                        }

                        val isError = arrayLoadState.any { loadState ->
                            loadState is LoadState.Error
                        }

                        if (isError) {
                            binding.btnRetry.isVisible = true
                            binding.pb.isVisible = false
                            binding.nsvMovie.isVisible = false
                        } else if (isNotLoading) {
                            Timber.d("Boos balue")
                            binding.btnRetry.isVisible = false
                            binding.pb.isVisible = false
                            binding.nsvMovie.isVisible = true
                        } else {
                            binding.btnRetry.isVisible = false
                            binding.pb.isVisible = true
                            binding.nsvMovie.isVisible = false
                        }
                    }
            }
        }
    }

    private fun setupRetryBtn() {
        binding.btnRetry.setOnClickListener {
            arrayAdapter?.forEach {
                it.retry()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

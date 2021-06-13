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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.moviecatalogue.core.adapter.MovieAdapter
import com.alurwa.moviecatalogue.core.model.CarouselMenu
import com.alurwa.moviecatalogue.databinding.FragmentMovieBinding
import com.alurwa.moviecatalogue.databinding.ListNestedCarouselItemBinding
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class MovieFragmentAbstract : Fragment() {
    private var _binding: FragmentMovieBinding? = null

    private val binding get() = _binding!!

    val viewModel by activityViewModels<MainViewModel>()

    var arrayAdapter: Array<MovieAdapter>? = null

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

        setupAdapter()

        setupSwipeToRefresh()
    }

    abstract fun navigateToDetail(extraId: Int)

    abstract fun navigateToList(which: Int)

    private fun setupAdapter() {
        getCarousels()
    }

    fun submitList(list: List<CarouselMenu>) {
        arrayAdapter = Array(list.size) { pos ->
            MovieAdapter(true) { id ->
                navigateToDetail(id)
            }.also { movieAdapter ->
                lifecycleScope.launch {
                    delay(200)
                    movieAdapter.submitData(list[pos].pagingData)
                }
            }
        }

        setupList(list)
    }

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
            }.attachToRecyclerView(view.rcv)

            view.rcv.adapter = movieAdapter

            view.llHeader.setOnClickListener {
                navigateToList(index)
            }

            binding.llList.addView(view.root)
        }
    }

    abstract fun getCarousels()

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            arrayAdapter?.forEach {
                it.retry()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // mViewModel.listState = binding.rcvMovie.layoutManager?.onSaveInstanceState()
        // mViewModel.chipState = binding.chipGroupMovie.checkedChipId
        _binding = null
    }
}

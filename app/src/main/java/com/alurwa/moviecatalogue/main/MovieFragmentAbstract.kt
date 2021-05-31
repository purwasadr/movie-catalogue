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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.core.adapter.NestedMovieAdapter
import com.alurwa.moviecatalogue.core.common.SpacingDecoration
import com.alurwa.moviecatalogue.databinding.FragmentMovieBinding

abstract class MovieFragmentAbstract : Fragment() {
    private var _binding: FragmentMovieBinding? = null

    private val binding get() = _binding!!

    val viewModel by activityViewModels<MainViewModel>()

    val adapter by lazy {
        NestedMovieAdapter(
            lifecycleScope,
            { id ->
                navigateToDetail(id)
            }
        ) { which ->
            navigateToList(which)
        }
    }

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

        setupRecyclerView()

        setupSwipeToRefresh()
    }

    abstract fun navigateToDetail(extraId: Int)

    abstract fun navigateToList(which: Int)

    private fun setupAdapter() {
        /* lifecycleScope.launchWhenCreated {
             adapter.submitData(viewModel.getFilmNestedVp())
         }

         */

        getCarousels()
    }

    private fun setupRecyclerView() {

        with(binding) {
            rcvMovie.layoutManager = LinearLayoutManager(context)
            rcvMovie.setHasFixedSize(true)
            rcvMovie.addItemDecoration(
                SpacingDecoration(
                    12,
                    12,
                    RecyclerView.VERTICAL
                )
            )

            rcvMovie.adapter = adapter

            rcvMovie.recycledViewPool.setMaxRecycledViews(0, 0)
        }
    }

    abstract fun getCarousels()

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            /*   for (i in 0 until (adapter.itemCount - 1)) {
                   val viewHolder = binding.rcvMovie.layoutManager?.findViewByPosition(i)

                   viewHolder?.findViewById<TextView>(
                       R.id.txt_title
                   )?.text = "www"
                   (viewHolder?.findViewById<RecyclerView>(
                       R.id.rcv
                   )?.adapter as? MovieAdapter)?.retry()
               }

             */
            adapter.refreshMovie()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // mViewModel.listState = binding.rcvMovie.layoutManager?.onSaveInstanceState()
        // mViewModel.chipState = binding.chipGroupMovie.checkedChipId
        _binding = null
    }
}

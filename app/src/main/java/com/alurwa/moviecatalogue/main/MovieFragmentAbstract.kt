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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.moviecatalogue.core.adapter.NestedMovieAdapter
import com.alurwa.moviecatalogue.core.model.CarouselMenu
import com.alurwa.moviecatalogue.databinding.FragmentMovieBinding

abstract class MovieFragmentAbstract : Fragment() {
    private var _binding: FragmentMovieBinding? = null

    val binding get() = _binding!!

    val viewModel by activityViewModels<MainViewModel>()

    val adapter by lazy {
        NestedMovieAdapter(
            viewLifecycleOwner.lifecycleScope,
            viewLifecycleOwner.lifecycle,
            { navigateToDetail(it) },
            { navigateToList(it) }
        )
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
        setupCarouselsList()
        setupRetryBtn()
        setupLoadingState()
        getCarousels()
    }

    abstract fun navigateToDetail(extraId: Int)

    abstract fun navigateToList(which: Int)

    abstract fun setupLinearLayout(linearLayoutManager: LinearLayoutManager, index: Int)

    fun submitList(list: List<CarouselMenu>) {
        adapter.submitData(list)
    }

    private fun setupCarouselsList() {
        with(binding) {
            listCarousels.setHasFixedSize(true)
            listCarousels.layoutManager = LinearLayoutManager(requireContext())
            listCarousels.adapter = adapter
        }
    }

    abstract fun getCarousels()

    private fun setupLoadingState() {
        adapter.setOnError {
            binding.btnRetry.isVisible = true
            binding.pb.isVisible = false
            binding.listCarousels.isVisible = false
        }
        adapter.setOnNotLoading {
            binding.btnRetry.isVisible = false
            binding.pb.isVisible = false
            binding.listCarousels.isVisible = true
        }
        adapter.setOnLoading {
            binding.btnRetry.isVisible = false
            binding.pb.isVisible = true
            binding.listCarousels.isVisible = false
        }
    }

    private fun setupRetryBtn() {
        binding.btnRetry.setOnClickListener {
            adapter.retryMovie()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

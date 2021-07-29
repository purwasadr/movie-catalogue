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

package com.alurwa.moviecatalogue.core.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.core.model.CarouselMenu
import com.alurwa.moviecatalogue.databinding.ListNestedCarouselItemBinding
import com.alurwa.moviecatalogue.utils.CommonUtil
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import timber.log.Timber

class NestedMovieAdapter(
    private val scope: CoroutineScope,
    private val lifecycle: Lifecycle,
    private val onClickCallback: (position: Int) -> Unit,
    private val onClickHeaderCallback: (which: Int) -> Unit
) : RecyclerView.Adapter<NestedMovieAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    private lateinit var arrayAdapter: Array<MovieAdapter>

    private val carousels: MutableList<CarouselMenu> = mutableListOf()

    private val arrayLoadState = arrayListOf<LoadState?>(null, null, null, null)

    private var listenerOnError: (() -> Unit)? = null

    private var listenerOnFinish: (() -> Unit)? = null

    private var listenerOnLoading: (() -> Unit)? = null

    fun submitData(vpData: List<CarouselMenu>) {
        arrayAdapter = Array(vpData.size) {
            MovieAdapter() { pos ->
                onClickCallback.invoke(pos)
            }.also { movieAdapter ->
                setupLoadingState(movieAdapter, it)
            }
        }

        arrayAdapter.forEachIndexed { index, data ->
            scope.launch {
                data.submitData(vpData[index].pagingData)
            }
        }

        carousels.clear()
        carousels.addAll(vpData)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListNestedCarouselItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).also {
            it.list.setRecycledViewPool(viewPool)
            Timber.d("onCreateViewHolder")
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = carousels.size

    inner class ViewHolder(
        private val binding: ListNestedCarouselItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val list = binding.rcv

        fun bind(position: Int) {
            val adapter = arrayAdapter[position]

            val carouselMenu = carousels[position]

            with(binding) {

                txtTitle.text = carouselMenu.title

                rcv.layoutManager = LinearLayoutManager(
                    binding.root.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                ).also {
                    it.initialPrefetchItemCount = 4
                }

                rcv.setHasFixedSize(true)

                GravitySnapHelper(Gravity.START).also {
                    it.snapToPadding = true
                    it.maxFlingDistance = CommonUtil.dpToPx(binding.root.context, 200)
                }.attachToRecyclerView(rcv)

                rcv.adapter = adapter.withLoadStateFooter(
                    MovieLoadStateAdapter()
                )

                llHeader.setOnClickListener {
                    onClickHeaderCallback.invoke(position)
                }

                Timber.d("Bind ViewHolder")
            }
        }
    }

    fun retryMovie() {
        arrayAdapter.forEach {
            it.retry()
        }
    }

    fun setOnError(listener: () -> Unit) {
        listenerOnError = listener
    }

    fun setOnNotLoading(listener: () -> Unit) {
        listenerOnFinish = listener
    }

    fun setOnLoading(listener: () -> Unit) {
        listenerOnLoading = listener
    }

    private fun setupLoadingState(movieAdapter: MovieAdapter, index: Int) {
        scope.launch {
            // Launches a coroutine that collects items from a flow when the Activity
            // is at least started. It will automatically cancel when the activity is stopped and
            // start collecting again whenever it's started again.
            movieAdapter.loadStateFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
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

                    when {
                        isError -> {
                            listenerOnError?.invoke()
                         //   Timber.d("isError")
                        }
                        isNotLoading -> {
                            listenerOnFinish?.invoke()
                        //    Timber.d("isNotLoading")
                        }
                        else -> {
                            listenerOnLoading?.invoke()
                         //   Timber.d("Else in loadState")
                        }
                    }
                }
        }
    }
}

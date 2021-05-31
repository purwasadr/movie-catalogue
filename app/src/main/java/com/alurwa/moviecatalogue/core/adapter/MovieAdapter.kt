/**
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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.databinding.RcvItemMovieBinding

class MovieAdapter(
    private val isShowPoster: Boolean = true,
    private val onClickCallback: ((id: Int) -> Unit)?
) : PagingDataAdapter<Movie, MovieAdapter.ViewHolder>(COMPARATOR) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            VIEW_TYPE_NETWORK
        } else {
            VIEW_TYPE_MOVIE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RcvItemMovieBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(
        private val binding: RcvItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = getItem(position)

            binding.isShowPoster = isShowPoster
            with(binding) {
                movie = item

                cardRcvMovie.setOnClickListener {
                    if (item != null) {
                        onClickCallback?.invoke(item.id)
                    }
                }
            }
        }
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id
        }

        const val VIEW_TYPE_MOVIE = 100

        const val VIEW_TYPE_NETWORK = 101
    }
}

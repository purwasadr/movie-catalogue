package com.alurwa.moviecatalogue.core.common

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.databinding.RcvItemMovieBinding
import com.bumptech.glide.Glide

class MovieAdapter(
        private val isShowPoster: Boolean,
        private val onClickCallback: (id: Int) -> Unit
) : PagingDataAdapter<Movie, MovieAdapter.ViewHolder>(COMPARATOR) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            101
        } else {
            100
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
            val posterPath = item?.posterPath

            binding.isShowPoster = isShowPoster
            with(binding) {
                movie = item

                cardRcvMovie.setOnClickListener {
                    if (item != null) {
                        onClickCallback.invoke(item.id)
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
    }
}

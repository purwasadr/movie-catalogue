package com.alurwa.moviecatalogue.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.core.model.Movie
import com.alurwa.moviecatalogue.databinding.ListItemMovieBoxBinding

/**
 * Created by Purwa Shadr Al 'urwa on 26/05/2021
 */

class BoxMovieAdapter : PagingDataAdapter<Movie, BoxMovieAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemMovieBoxBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(
        private val binding: ListItemMovieBoxBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
           binding.movie = getItem(position)
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
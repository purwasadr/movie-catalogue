package com.alurwa.moviecatalogue.core.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.core.model.Cast
import com.alurwa.moviecatalogue.databinding.RcvItemCastBinding

class CastAdapter(private val castList: List<Cast>?) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                    RcvItemCastBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
            )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = castList?.size ?: 0

    inner class ViewHolder(binding: RcvItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }
}
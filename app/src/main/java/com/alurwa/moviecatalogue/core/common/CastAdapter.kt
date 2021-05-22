package com.alurwa.moviecatalogue.core.common

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.core.model.Cast
import com.alurwa.moviecatalogue.databinding.RcvItemCastBinding

class CastAdapter(
    private val castList: List<Cast>?
) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            RcvItemCastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = castList?.size ?: 0

    inner class ViewHolder(
        private val binding: RcvItemCastBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val cast = castList?.get(position)

            // FIXME: Margin in end of item still bad
            if (castList?.size == (position + 1)) {
                //  val cardCastParams = binding.cardCast.layoutParams as ViewGroup.MarginLayoutParams
                //   cardCastParams.marginEnd = 16.px
                binding.isLastItem = true
            } else {
                binding.isLastItem = false
            }

            binding.cast = cast
        }
    }
}
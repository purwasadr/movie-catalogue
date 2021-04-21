package com.alurwa.moviecatalogue.core.binding

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.alurwa.moviecatalogue.core.model.Genre
import com.alurwa.moviecatalogue.utils.NumberFormatUtil
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("imagePoster")
    fun imagePoster(imageView: ImageView, posterPath: String?) {
        if (!posterPath.isNullOrEmpty()) {
            Glide.with(imageView.context)
                    .load(Uri.parse("https://image.tmdb.org/t/p/w185$posterPath"))
                    .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("imageBackdrop")
    fun imageBackdrop(imageView: ImageView, backdropPath: String?) {
        if (!backdropPath.isNullOrEmpty()) {
            Glide.with(imageView.context)
                    .load(Uri.parse("https://image.tmdb.org/t/p/w500$backdropPath"))
                    .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("chipGenres")
    fun chipGenres(chipGroup: ChipGroup, genres: List<Genre>?) {
        genres?.forEach {
            val chip = Chip(chipGroup.context)

            chip.text = it.name
            chip.isCheckable = false

            chipGroup.addView(chip)
        }
    }

    @JvmStatic
    @BindingAdapter("txtYear")
    fun txtYear(txt: TextView, date: String?) {
        txt.text = date?.let {
            val dateArray = date.split("-")
            if (dateArray.isNotEmpty()) {
                dateArray[0]
            } else {
                "-"
            }
        } ?: ""

    }

    @JvmStatic
    @BindingAdapter("txtDate")
    fun txtDate(txt: TextView, date: String?) {
        txt.text = date?.let {
            val dateArray = date.split("-")
            if (dateArray.size == 3) {
                "${dateArray[2]}/${dateArray[1]}/${dateArray[0]}"
            } else {
                "-"
            }
        } ?: ""
    }

    @JvmStatic
    @BindingAdapter("title", "year")
    fun txtTitleWithYear(txt: TextView, title: String?, date: String?) {
        if (title != null && date != null) {
            val year = date.split("-").let { if (it.isNotEmpty()) " (${it[0]})" else "" }
            txt.text = "$title$year"

        }
    }

    @JvmStatic
    @BindingAdapter("txtPrice")
    fun txtPrice(txt: TextView, price: Int?) {
        if (price != null) {
            val result = if (price == 0) {
                "-"
            } else {
               "$" + NumberFormatUtil.withComma(price)
            }

            txt.text = result
        }
    }
}
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

import android.content.res.Resources
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.core.model.Cast
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

    @JvmStatic
    @BindingAdapter("loadProfileImage")
    fun loadProfileImage(imageView: ImageView, profilePath: String?) {
        if (!profilePath.isNullOrEmpty()) {
            Glide.with(imageView.context)
                .load(Uri.parse("https://image.tmdb.org/t/p/w185$profilePath"))
                .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("rcvCast")
    fun rcvCast(rcv: RecyclerView, castList: List<Cast>?) {
        if (!castList.isNullOrEmpty()) {
            val adapter = CastAdapter(castList)

            rcv.layoutManager = LinearLayoutManager(
                rcv.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rcv.setHasFixedSize(true)
            rcv.adapter = adapter
        }
    }

    @JvmStatic
    @BindingAdapter("layoutMarginEnd")
    fun layoutMarginEnd(view: View, end: Int) {
        val viewParams = view.layoutParams as ViewGroup.MarginLayoutParams
        viewParams.marginEnd = (end * Resources.getSystem().displayMetrics.density).toInt()
    }
}

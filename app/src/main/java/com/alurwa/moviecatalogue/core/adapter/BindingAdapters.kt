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

import android.net.Uri
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
    @BindingAdapter("imageLoader")
    fun imageLoader(imageView: ImageView, imagePath: String?) {
        if (!imagePath.isNullOrEmpty()) {
            Glide.with(imageView.context)
                .load(Uri.parse(imagePath))
                .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("chipGenres")
    fun chipGenres(chipGroup: ChipGroup, genres: List<Genre>?) {
        chipGroup.removeAllViews()
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
    @BindingAdapter("txtRuntime")
    fun txtRuntime(txt: TextView, runtime: Int?) {
        if (runtime != null && runtime != 0) {
            val ba = (runtime / 60)
            val result = if (runtime % 60 == 0 && runtime >= 60) {
                "$ba j "
            } else if (runtime < 60) {
                "$runtime min"
            } else {
                "$ba j " + (runtime % 60) + " min"
            }

            txt.text = result
        } else {
            txt.text = "-"
        }
    }
}

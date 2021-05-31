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

package com.alurwa.moviecatalogue.core.common

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Purwa Shadr Al 'urwa on 26/05/2021
 */

class SpacingDecoration(
    private val contentPaddingStartEnd: Int,
    private val itemPadding: Int,
    private val orientation: Int,
    private val inverted: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPadding = dpToPx(parent.context, this.itemPadding)
        val contentPaddingStartEnd = dpToPx(parent.context, this.contentPaddingStartEnd)

        val layoutManager: RecyclerView.LayoutManager = parent.layoutManager!!
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        val position = layoutParams.viewAdapterPosition
        val itemCount = layoutManager.itemCount

        if (position == RecyclerView.NO_POSITION || itemCount == 0) {
            return
        }

        if (orientation == RecyclerView.HORIZONTAL) {
            if (position == 0) {
                if (!inverted) {
                    outRect.left = contentPaddingStartEnd
                } else {
                    outRect.right = contentPaddingStartEnd
                }
            } else if (position > 0 && position < itemCount - 1) {
                if (!inverted) {
                    outRect.left = itemPadding
                } else {
                    outRect.right = itemPadding
                }
            } else if (position == itemCount - 1) {
                if (!inverted) {
                    outRect.left = itemPadding
                    outRect.right = contentPaddingStartEnd
                } else {
                    outRect.left = contentPaddingStartEnd
                    outRect.right = itemPadding
                }
            }
        } else {
            if (position == 0) {
                if (!inverted) {
                    outRect.top = contentPaddingStartEnd
                } else {
                    outRect.bottom = contentPaddingStartEnd
                }
            } else if (position > 0 && position < itemCount - 1) {
                if (!inverted) {
                    outRect.top = itemPadding
                } else {
                    outRect.bottom = itemPadding
                }
            } else if (position == itemCount - 1) {
                if (!inverted) {
                    outRect.bottom = contentPaddingStartEnd
                    outRect.top = itemPadding
                } else {
                    outRect.bottom = itemPadding
                    outRect.top = contentPaddingStartEnd
                }
            }
        }
    }

    private fun dpToPx(context: Context, dp: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),

            // Avoid using system resource
            context.resources.displayMetrics
        ).toInt()
}

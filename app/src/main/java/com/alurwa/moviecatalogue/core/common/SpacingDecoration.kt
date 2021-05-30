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
        outRect: Rect, view: View, parent: RecyclerView,
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
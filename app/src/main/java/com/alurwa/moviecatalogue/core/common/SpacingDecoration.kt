package com.alurwa.moviecatalogue.core.common

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Purwa Shadr Al 'urwa on 26/05/2021
 */

class SpacingDecoration(
    val paddingg: Int,
    private val orientation: Int,
    private val inverted: Boolean = false
) : RecyclerView.ItemDecoration() {

    private val padding = paddingg.dpToPx

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val layoutManager: RecyclerView.LayoutManager = parent.layoutManager!!
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        val position = layoutParams.viewAdapterPosition
        val itemCount = layoutManager.itemCount

        if (position == RecyclerView.NO_POSITION || itemCount == 0) {
            return
        }

        if (orientation == RecyclerView.HORIZONTAL) {
            if (position >= 0 && position < itemCount - 1) {
                if (!inverted) {
                    outRect.left = padding
                } else {
                    outRect.right = padding
                }
            } else if (position == itemCount - 1) {
                if (!inverted) {
                    outRect.left = padding
                    outRect.right = padding
                } else {
                    outRect.left = padding
                    outRect.right = padding
                }
            }
        } else {
            if (position >= 0 && position < itemCount - 1) {
                if (!inverted) {
                    outRect.top = padding
                } else {
                    outRect.bottom = padding
                    outRect.top = padding
                }
            } else if (position == itemCount - 1) {
                if (!inverted) {
                    outRect.bottom = padding
                } else {
                    outRect.bottom = padding
                    outRect.top = padding
                }
            }
        }
    }

    private val Int.dpToPx: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
}
package com.alurwa.moviecatalogue.core.common

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler


class AutoFitGridLayout(private val context: Context,
                        private var columnWidth: Int
                        ) : GridLayoutManager(context, 2) {
    private var columnWidthChanged = true

    init {
        setColumnWidth(columnWidth)
    }

    fun setColumnWidth(newColumnWidth: Int) {
        val result = newColumnWidth * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT

        if (result > 0 && result != columnWidth) {
            columnWidth = result
            columnWidthChanged = true
        }
    }

    override fun onLayoutChildren(recycler: Recycler?, state: RecyclerView.State?) {
        if (columnWidthChanged && columnWidth > 0) {
            val totalSpace: Int = if (orientation == VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }
            val spanCount = Math.max(1, totalSpace / columnWidth)
            setSpanCount(spanCount)
            columnWidthChanged = false
        }
        super.onLayoutChildren(recycler, state)
    }
}
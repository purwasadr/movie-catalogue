package com.alurwa.moviecatalogue.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView

class CustomImageView : ImageView {
    constructor(context: Context?) : super(context!!) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
    }

    override fun onDraw(canvas: Canvas?) {

        val shadow = Paint()

        shadow.setShadowLayer(10.0f, 0.0f, 2.0f, Color.BLACK)

        val d = drawable
        if (d != null) {
            setLayerType(LAYER_TYPE_SOFTWARE, shadow)
            val bitmap = (d as BitmapDrawable).bitmap
            canvas?.drawBitmap(bitmap, 0.0f, 0.0f, shadow)
        } else {
            super.onDraw(canvas)
        }

      //  canvas?.drawBitmap(bitmap, 0.0f, 0.0f. shadow)
    }
}
package com.example.uip

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity


class VerticalTextView: androidx.appcompat.widget.AppCompatTextView {
    private var topDown :Boolean? = null
    private val ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android"

    constructor(context: Context, attrs: AttributeSet):super(context, attrs){
        val gravity = gravity
        topDown =
            if (Gravity.isVertical(gravity) && gravity and Gravity.VERTICAL_GRAVITY_MASK == Gravity.BOTTOM) {
                setGravity(gravity and Gravity.HORIZONTAL_GRAVITY_MASK or Gravity.TOP)
                false
            } else true
        applyCustomFont(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val textPaint = paint
        textPaint.color = currentTextColor
        textPaint.drawableState = drawableState

        canvas!!.save()

        if (topDown!!) {
            canvas.translate(width.toFloat(), 0F)
            canvas.rotate(90F)
        } else {
            canvas.translate(0F, height.toFloat())
            canvas.rotate(-90F)
        }

        canvas.translate(compoundPaddingLeft.toFloat(), extendedPaddingTop.toFloat())

        layout.draw(canvas)
        canvas.restore()
    }

    private fun applyCustomFont(context: Context, attrs: AttributeSet) {
        val textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL)
        typeface = selectTypeface(context, textStyle)
    }

    private fun selectTypeface(context: Context, textStyle: Int): Typeface? {
        return when (textStyle) {
            Typeface.BOLD -> Typeface.createFromAsset(context.assets, "robotobold.ttf")
            Typeface.NORMAL -> Typeface.createFromAsset(
                context.assets,
                "robotobold.ttf"
            ) // regular
            else -> Typeface.createFromAsset(context.assets, "robotobold.ttf") // regular
        }
    }
}
package com.eaguirre.myflix.ui.common

import android.content.Context
import android.util.AttributeSet
import com.eaguirre.myflix.R

class AspectRatioImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private var ratio: Float = 1f

    companion object{
        private const val DEFAULT_RATIO = 1f
    }

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView)
        ratio = attr.getFloat(R.styleable.AspectRatioImageView_ratio, DEFAULT_RATIO)
        attr.recycle()
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var width = measuredWidth
        var height = measuredHeight

        if (width == 0 && height == 0){
            return
        }

        if (width > 0) {
            height = (width * ratio).toInt()
        } else if(height > 0) {
            width = (height/ratio).toInt()
        }

        setMeasuredDimension(width,height)
    }
}
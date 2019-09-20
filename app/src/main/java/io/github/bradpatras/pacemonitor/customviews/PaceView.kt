package io.github.bradpatras.pacemonitor.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import io.github.bradpatras.pacemonitor.R

/**
 * TODO: document your custom view class.
 */
class PaceView : View {

    private var _labelText: String? = null // TODO: use a default from R.string...
    private var _labelFontColor: Int = Color.DKGRAY

    private var textPaint: TextPaint? = null
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    /**
     * The text to draw
     */
    var labelText: String?
        get() = _labelText
        set(value) {
            _labelText = value
        }

    /**
     * The font color
     */
    var labelFontColor: Int
        get() = _labelFontColor
        set(value) {
            _labelFontColor = value
        }

    /**
     * In the example view, this drawable is drawn above the text.
     */
    var exampleDrawable: Drawable? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.PaceView, defStyle, 0
        )

        _labelText = a.getString(
            R.styleable.PaceView_labelText
        )

//        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
//        // values that should fall on pixel boundaries.
//        _exampleDimension = a.getDimension(
//            R.styleable.PaceView_exampleDimension,
//            exampleDimension
//        )

        a.recycle()
    }
}

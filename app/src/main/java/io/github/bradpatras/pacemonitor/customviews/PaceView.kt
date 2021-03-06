package io.github.bradpatras.pacemonitor.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.bradpatras.pacemonitor.R
import kotlinx.android.synthetic.main.pace_view.view.*
import java.text.DecimalFormat

private const val TIME_PLACEHOLDER: String = "--"

class PaceView : ConstraintLayout {
    enum class DistanceUnit(val abbreviation: String) {
        KM("km"),
        MI("mi")
    }

    private val secondsFormat = DecimalFormat("00")
    private var _labelText: String? = ""

    // The text above the time labels
    var labelText: String?
        get() = _labelText
        set(value) {
            _labelText = value
            setLabel(value ?: "")
        }

    var distanceUnit: DistanceUnit = DistanceUnit.MI
        set(value) {
            field = value
            unit_tv.text = resources.getString(R.string.per_unit, value.abbreviation)
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        View.inflate(context, R.layout.pace_view, this)
        val labelAttribute = context.obtainStyledAttributes(attrs, R.styleable.PaceView, defStyle, 0)
        labelText = labelAttribute.getString(R.styleable.PaceView_labelText)
        distanceUnit = distanceUnit
        labelAttribute.recycle()
    }

    fun setPace(minutes: Int?, seconds: Int?) {
        setMinutes(minutes)
        setSeconds(seconds)
    }

    private fun setMinutes(minutes: Int?) {
        if (minutes is Int) {
            minutes_tv.text = "$minutes"
        } else {
            minutes_tv.text = TIME_PLACEHOLDER
        }
    }

    private fun setSeconds(seconds: Int?) {
        if (seconds is Int) {
            seconds_tv.text = secondsFormat.format(seconds)
        } else {
            seconds_tv.text = TIME_PLACEHOLDER
        }
    }

    fun setLabel(label: String) {
        label_tv.text = label
    }
}

package hr.algebra.triathlist.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import hr.algebra.triathlist.R
import kotlinx.android.synthetic.main.swim_info_card.view.*

class SwimInfoCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var dayOfWeek: String
    private var lapCount: String
    private var time: String
    private var distance: String

    init {
        LayoutInflater.from(context).inflate(R.layout.swim_info_card, this, true)

        val typedArray: TypedArray? = context.theme.obtainStyledAttributes(attrs, R.styleable.SwimInfoCard, 0, 0)
        dayOfWeek = typedArray?.getString(R.styleable.SwimInfoCard_day_of_week) ?: "Monday"
        lapCount = typedArray?.getString(R.styleable.SwimInfoCard_lap_count) ?: "0 laps"
        time = typedArray?.getString(R.styleable.SwimInfoCard_time) ?: "00:00:00"
        distance = typedArray?.getString(R.styleable.SwimInfoCard_distance) ?: "0 m"
        tvDayOfWeek.text = dayOfWeek
        tvLapCount.text = lapCount
        tvTime.text = time
        tvDistance.text = distance
        typedArray?.recycle()
    }
}

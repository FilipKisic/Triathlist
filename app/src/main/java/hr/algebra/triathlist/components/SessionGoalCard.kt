package hr.algebra.triathlist.components

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import hr.algebra.triathlist.R
import kotlinx.android.synthetic.main.session_goal_card.view.*

class SessionGoalCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var unitTextColor: Int
    private var unit: String
    private var icon: Drawable

    init {
        LayoutInflater.from(context).inflate(R.layout.session_goal_card, this, true)
        val typedArray: TypedArray? = context.theme.obtainStyledAttributes(attrs, R.styleable.SessionGoalCard, 0, 0)
        unitTextColor = typedArray?.getColor(R.styleable.SessionGoalCard_unit_text_color, Color.WHITE) ?: Color.WHITE
        unit = typedArray?.getString(R.styleable.SessionGoalCard_unit) ?: "meters"
        icon = typedArray?.getDrawable(R.styleable.SessionGoalCard_icon) ?: resources.getDrawable(R.drawable.goal, context.theme)

        tvUnit.text = unit
        tvUnit.setTextColor(unitTextColor)
        ivIcon.setImageDrawable(icon)
    }
}
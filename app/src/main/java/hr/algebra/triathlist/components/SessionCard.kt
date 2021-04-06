package hr.algebra.triathlist.components

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import hr.algebra.triathlist.R
import kotlinx.android.synthetic.main.session_card.view.*

class SessionCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var activity_icon: Drawable
    private var activity_title: String
    private var title_text_color: Int

    init {
        LayoutInflater.from(context).inflate(R.layout.session_card, this, true)
        val typedArray: TypedArray? = context.theme.obtainStyledAttributes(attrs, R.styleable.SessionCard, 0, 0)
        activity_icon =
            typedArray?.getDrawable(R.styleable.SessionCard_activity_icon) ?: resources.getDrawable(R.drawable.swimming, context.theme)
        activity_title = typedArray?.getString(R.styleable.SessionCard_activity_title) ?: "Activity"
        title_text_color = typedArray?.getColor(R.styleable.SessionCard_title_text_color, Color.WHITE) ?: Color.WHITE

        ivActivityIcon.setImageDrawable(activity_icon)
        tvActivityTitle.text = activity_title
        tvActivityTitle.setTextColor(title_text_color)
    }
}
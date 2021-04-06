package hr.algebra.triathlist.components

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import hr.algebra.triathlist.R
import kotlinx.android.synthetic.main.action_button.view.*

class ActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var buttonBackgroundColor: Drawable
    var buttonText: String

    init {
        LayoutInflater.from(context).inflate(R.layout.action_button, this, true)
        val typedArray: TypedArray? = context.theme.obtainStyledAttributes(attrs, R.styleable.ActionButton, 0, 0)
        buttonBackgroundColor =
            typedArray?.getDrawable(R.styleable.ActionButton_background_color) ?: resources.getDrawable(R.drawable.button_background_yellow)
        buttonText = typedArray?.getString(R.styleable.ActionButton_button_text) ?: "Button"

        actionButton.background = buttonBackgroundColor
        tvButtonText.text = buttonText
    }
}
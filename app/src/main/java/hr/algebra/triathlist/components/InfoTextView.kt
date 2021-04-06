package hr.algebra.triathlist.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import hr.algebra.triathlist.R
import kotlinx.android.synthetic.main.info_text_view.view.*

class InfoTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var placeholder: String

    init {
        LayoutInflater.from(context).inflate(R.layout.info_text_view, this, true)
        val typedArray: TypedArray? = context.theme.obtainStyledAttributes(attrs, R.styleable.InfoTextView, 0, 0)
        placeholder = typedArray?.getString(R.styleable.InfoTextView_placeholder) ?: "Placeholder"

        etInfo.setText(placeholder)
    }
}
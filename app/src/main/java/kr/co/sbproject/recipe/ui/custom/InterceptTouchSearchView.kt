package kr.co.sbproject.recipe.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.SearchView

class InterceptTouchSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SearchView(context, attrs, defStyleAttr), View.OnTouchListener {

    init {
        setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        (v as? SearchView)?.onActionViewExpanded()
        return true
    }


}
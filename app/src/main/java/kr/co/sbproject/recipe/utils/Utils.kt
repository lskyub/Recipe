package kr.co.sbproject.recipe.utils

import android.content.Context
import android.util.TypedValue

// dp -> pixel 단위로 변경
fun dpToPx(context: Context?, dp: Float): Float {
    return try {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context?.resources?.displayMetrics
        )
    } catch (e: Exception) {
        0f
    }
}

// dp -> pixel 단위로 변경
fun pxToDp(context: Context, px: Int): Int {
    var metrics = context.resources.displayMetrics
    return px / (metrics.densityDpi / 160f).toInt()
}
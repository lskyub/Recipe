package kr.co.sbproject.recipe.ui.fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.view_calendar.*
import kr.co.sbproject.recipe.R

class MonthFragment : BaseFragment() {
    override fun getResourceId(): Int = R.layout.view_calendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_calendar.apply {  }

    }

    override fun onUiStart() {
    }

    override fun onUiResume() {
    }

    override fun onUiPause() {
    }

    override fun onUiStop() {
    }

}
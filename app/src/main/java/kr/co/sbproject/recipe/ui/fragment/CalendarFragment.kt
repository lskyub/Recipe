package kr.co.sbproject.recipe.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_calendar.*
import kr.co.sbproject.recipe.BR
import kr.co.sbproject.recipe.R
import kr.co.sbproject.recipe.databinding.FragmentCalendarBinding
import kr.co.sbproject.recipe.databinding.ItemCalendarDetailsBinding
import kr.co.sbproject.recipe.model.CalendarDetails
import kr.co.sbproject.recipe.model.CalendarViewModel
import kr.co.sbproject.recipe.ui.custom.BaseRecyclerView

class CalendarFragment : BaseBindingFragment<FragmentCalendarBinding>() {

    private lateinit var viewModel: CalendarViewModel

    override fun getResourceId(): Int = R.layout.fragment_calendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        viewModel.setTitle("Calendar")
        viewModel.setSubTitle("expiration date")

        viewModel.setDetailsData("1", "10 ~ 20")
        viewModel.setDetailsData("2", "10 ~ 20")
        viewModel.setDetailsData("3", "10 ~ 20")
        viewModel.setDetailsData("4", "10 ~ 20")
        viewModel.setDetailsData("5", "10 ~ 20")
        viewModel.setDetailsData("6", "10 ~ 20")
        viewModel.setDetailsData("7", "10 ~ 20")

        binding.setVariable(BR.calendar, viewModel)

        rv_calendar_details.apply {
            val detailsAdapter =
                object : BaseRecyclerView.Adapter<CalendarDetails, ItemCalendarDetailsBinding>(
                    R.layout.item_calendar_details,
                    BR.details
                ) {
                }
            adapter = detailsAdapter
        }
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
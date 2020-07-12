package kr.co.sbproject.recipe.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.view_month.*
import kr.co.sbproject.recipe.BR
import kr.co.sbproject.recipe.R
import kr.co.sbproject.recipe.databinding.ItemMonthBinding
import kr.co.sbproject.recipe.databinding.ViewMonthBinding
import kr.co.sbproject.recipe.model.Month
import kr.co.sbproject.recipe.model.MonthViewModel
import kr.co.sbproject.recipe.ui.custom.BaseRecyclerView

class MonthFragment : BaseBindingFragment<ViewMonthBinding>() {

    private lateinit var viewModel: MonthViewModel

    override fun getResourceId(): Int = R.layout.view_month

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MonthViewModel::class.java)

        for (i in 0..34) {
            viewModel.setMonthData("$i")
        }

        rv_calendar.apply {
            val monthAdapter =
                object : BaseRecyclerView.Adapter<Month, ItemMonthBinding>(
                    R.layout.item_month,
                    BR.month
                ) {
                    override fun onBindViewHolder(
                        holder: BaseRecyclerView.ViewHolder<ItemMonthBinding>,
                        position: Int
                    ) {
                        super.onBindViewHolder(holder, position)
                        var h = rv_calendar.height / 5

                        holder.itemView.layoutParams.apply {
                            height = h
                        }
                    }
                }
            adapter = monthAdapter
        }

        binding.setVariable(BR.monthViewModel, viewModel)
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
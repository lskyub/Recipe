package kr.co.sbproject.recipe.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import kotlinx.android.synthetic.main.fragment_refrigerator.*
import kr.co.sbproject.recipe.R
import kr.co.sbproject.recipe.databinding.FragmentRefrigeratorBinding
import kr.co.sbproject.recipe.databinding.ItemCategoryBinding
import kr.co.sbproject.recipe.databinding.ItemFoodBinding
import kr.co.sbproject.recipe.model.Food
import kr.co.sbproject.recipe.model.Category
import kr.co.sbproject.recipe.ui.custom.BaseDecoration
import kr.co.sbproject.recipe.ui.custom.BaseRecyclerView
import kr.co.sbproject.recipe.utils.dpToPx

class RefrigeratorFragment : BaseBindingFragment() {

    lateinit var binding: FragmentRefrigeratorBinding

    override fun setBindingView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_refrigerator, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_foods.apply {
            val foodAdapter = object : BaseRecyclerView.Adapter<Food, ItemFoodBinding>(
                R.layout.item_food,
                BR.food
            ) {
            }
            foodAdapter.replaceAll(
                listOf(
                    Food(resources.getDrawable(R.drawable.food, null), "1"),
                    Food(resources.getDrawable(R.drawable.food2, null), "2"),
                    Food(resources.getDrawable(R.drawable.food3, null), "3")
                )
            )
            adapter = foodAdapter
            addItemDecoration(BaseDecoration(dpToPx(mContext, 20f).toInt()))
        }

        rv_foodcategory.apply {
            val categoryAdapter =
                object : BaseRecyclerView.Adapter<Category, ItemCategoryBinding>(
                    R.layout.item_category,
                    BR.categorys
                ) {
                }
            categoryAdapter.replaceAll(
                listOf(
                    Category("Seafood"),
                    Category("Mexican"),
                    Category("Italian"),
                    Category("Chinese"),
                    Category("Korea"),
                    Category("Salad"),
                    Category("Steak"),
                    Category("Snacks")
                )
            )
            adapter = categoryAdapter
            addItemDecoration(BaseDecoration(dpToPx(mContext, 20f).toInt(), 0, 0, 0))
        }
        aiv_profile.apply {
            background = mContext.getDrawable(R.drawable.profile_foreground)
            clipToOutline = true
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

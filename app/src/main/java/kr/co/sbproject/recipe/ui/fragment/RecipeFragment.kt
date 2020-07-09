package kr.co.sbproject.recipe.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_recipe.*
import kr.co.sbproject.recipe.R
import kr.co.sbproject.recipe.databinding.FragmentRecipeBinding
import kr.co.sbproject.recipe.databinding.ItemCategoryBinding
import kr.co.sbproject.recipe.databinding.ItemFoodBinding
import kr.co.sbproject.recipe.model.Category
import kr.co.sbproject.recipe.model.Food
import kr.co.sbproject.recipe.model.RecipeViewModel
import kr.co.sbproject.recipe.ui.custom.BaseDecoration
import kr.co.sbproject.recipe.ui.custom.BaseRecyclerView
import kr.co.sbproject.recipe.utils.dpToPx

class RecipeFragment : BaseBindingFragment<FragmentRecipeBinding>() {

    private lateinit var viewModel: RecipeViewModel

    override fun getResourceId(): Int = R.layout.fragment_recipe

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
        viewModel.setTitle("Category")
        viewModel.setSubTitle("Popular")

        viewModel.setFoodData(resources.getDrawable(R.drawable.food, null), "1")
        viewModel.setFoodData(resources.getDrawable(R.drawable.food2, null), "2")
        viewModel.setFoodData(resources.getDrawable(R.drawable.food3, null), "3")

        viewModel.setCategoryData("Seafood")
        viewModel.setCategoryData("Mexican")
        viewModel.setCategoryData("Italian")
        viewModel.setCategoryData("Chinese")
        viewModel.setCategoryData("Korea")
        viewModel.setCategoryData("Salad")
        viewModel.setCategoryData("Steak")
        viewModel.setCategoryData("Snacks")

        binding.setVariable(BR.recipe, viewModel)

        rv_foods.apply {
            val foodAdapter = object : BaseRecyclerView.Adapter<Food, ItemFoodBinding>(
                R.layout.item_food,
                BR.food
            ) {
            }
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
            adapter = categoryAdapter
            addItemDecoration(BaseDecoration(dpToPx(mContext, 20f).toInt(), 0, 0, 0))
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

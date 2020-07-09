package kr.co.sbproject.recipe.model

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import kr.co.sbproject.recipe.constants.ListLiveData

class RecipeViewModel : TopViewModel() {
    private val foodMLiveData = ListLiveData<Food>()
    val foodLiveData: LiveData<List<Food>>
        get() = foodMLiveData as LiveData<List<Food>>

    private val categoryMLiveData = ListLiveData<Category>()
    val categoryLiveData: LiveData<List<Category>>
        get() = categoryMLiveData as LiveData<List<Category>>

    fun setFoodData(image: Drawable, title: String) {
        foodMLiveData.add(Food(image, title))
    }

    fun setCategoryData(title: String) {
        categoryMLiveData.add(Category(title))
    }
}
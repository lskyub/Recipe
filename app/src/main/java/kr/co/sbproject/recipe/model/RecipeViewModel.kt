package kr.co.sbproject.recipe.model

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.sbproject.recipe.constants.ListLiveData

class RecipeViewModel : TopViewModel() {
    private val foodMLiveData = ListLiveData<Food>()
    val foodLiveData: LiveData<List<Food>>
        get() = foodMLiveData as LiveData<List<Food>>

    private val categoryMLiveData = MutableLiveData<List<Category>>()
    val categoryLiveData: LiveData<List<Category>>
        get() = categoryMLiveData

    fun setFoodData(image: Drawable, title: String) {
        foodMLiveData.add(Food(image, title))
    }

    fun setCategoryData(categorys: List<Category>) {
        categoryMLiveData.postValue(categorys)
    }
}
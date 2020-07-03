package kr.co.sbproject.recipe.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecipeViewModel : ViewModel() {
    val foodLiveData = MutableLiveData<List<Food>>()

    val categoryLiveData = MutableLiveData<List<Category>>()

    init {
        foodLiveData.postValue(getFoodList())
        categoryLiveData.postValue(getCategoryList())
    }
}
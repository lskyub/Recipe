package kr.co.sbproject.recipe.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FoodViewModel : ViewModel() {
    val foodLiveData = MutableLiveData<List<Food>>()

    init {
        foodLiveData.postValue(getFoodList())
    }
}
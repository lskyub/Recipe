package kr.co.sbproject.recipe.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoryViewModel : ViewModel() {
    val ingredientsLiveData = MutableLiveData<List<Category>>()

    init {
        ingredientsLiveData.postValue(getCategoryList())
    }
}
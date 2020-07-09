package kr.co.sbproject.recipe.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kr.co.sbproject.recipe.constants.ListLiveData

class MonthViewModel : ViewModel() {
    private val monthMLiveData = ListLiveData<Month>()
    val monthLiveData: LiveData<List<Month>>
        get() = monthMLiveData as LiveData<List<Month>>

    fun setMonthData(title: String) {
        monthMLiveData.add(Month(title))
    }
}
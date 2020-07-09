package kr.co.sbproject.recipe.model

import androidx.lifecycle.LiveData
import kr.co.sbproject.recipe.constants.ListLiveData

class CalendarViewModel : TopViewModel() {
    private val detailsMLiveData = ListLiveData<CalendarDetails>()
    val detailsLiveData: LiveData<List<CalendarDetails>>
        get() = detailsMLiveData as LiveData<List<CalendarDetails>>

    fun setDetailsData(title: String, contents: String) {
        detailsMLiveData.add(CalendarDetails(title, contents))
    }
}
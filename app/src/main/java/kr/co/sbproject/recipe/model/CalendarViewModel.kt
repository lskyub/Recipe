package kr.co.sbproject.recipe.model

import androidx.lifecycle.LiveData
import kr.co.sbproject.recipe.constants.ListLiveData

class CalendarViewModel : TopViewModel() {
    private val detailsMLiveData = ListLiveData<CalendarDetails>()
    val detailsLiveData: LiveData<List<CalendarDetails>>
        get() = detailsMLiveData as LiveData<List<CalendarDetails>>

    private val calendarMLiveData = ListLiveData<Calendar>()
    val calendarLiveData: LiveData<List<Calendar>>
        get() = calendarMLiveData as LiveData<List<Calendar>>

    fun setDetailsData(title: String, contents: String) {
        detailsMLiveData.add(CalendarDetails(title, contents))
    }

    fun setCalendarData(title: String) {
        calendarMLiveData.add(Calendar(title))
    }
}
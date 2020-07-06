package kr.co.sbproject.recipe.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class TopViewModel : ViewModel() {
    private val liveData = MutableLiveData<TopModel>()
    val topModel: LiveData<TopModel>
        get() = liveData

    init {
        liveData.value = TopModel()
    }

    fun setTitle(title: String) {
        liveData.value?.title = title
    }

    fun setSubTitle(subTitle: String) {
        liveData.value?.subTitle = subTitle
    }

    fun onClickNotificationButton() {
        Log.i("sgim", "onClickNotificationButton")
    }
}
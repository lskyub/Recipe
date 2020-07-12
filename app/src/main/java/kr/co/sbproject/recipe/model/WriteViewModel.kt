package kr.co.sbproject.recipe.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class WriteViewModel : TopViewModel() {
    private val writeMLiveData = MutableLiveData<Write>()
    val writeLiveData: LiveData<Write>
        get() = writeMLiveData
}
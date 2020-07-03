package kr.co.sbproject.recipe

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

interface LifecycleListener : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onUiStart()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onUiResume()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onUiPause()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onUiStop()
}
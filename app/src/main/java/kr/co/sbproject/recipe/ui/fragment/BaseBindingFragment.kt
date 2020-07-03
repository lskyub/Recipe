package kr.co.sbproject.recipe.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.sbproject.recipe.LifecycleListener

abstract class BaseBindingFragment : Fragment(), LifecycleListener {
    val mContext by lazy {
        context as Context
    }

    abstract fun setBindingView(inflater: LayoutInflater, container: ViewGroup?): View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycle.addObserver(this)
        return setBindingView(inflater, container)
    }
}
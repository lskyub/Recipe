package kr.co.sbproject.recipe.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kr.co.sbproject.recipe.LifecycleListener

abstract class BaseFragment : Fragment(), LifecycleListener {
    val mContext by lazy {
        context as Context
    }

    @LayoutRes
    abstract fun getResourceId(): Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycle.addObserver(this)
        return inflater.inflate(getResourceId(), container, false)
    }
}
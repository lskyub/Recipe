package kr.co.sbproject.recipe.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import kr.co.sbproject.recipe.LifecycleListener
import kr.co.sbproject.recipe.R
import kr.co.sbproject.recipe.databinding.FragmentRecipeBinding

abstract class BaseBindingFragment<T : ViewDataBinding> : Fragment(), LifecycleListener {
    val mContext by lazy {
        context as Context
    }

    lateinit var binding: T

    @LayoutRes
    abstract fun getResourceId(): Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycle.addObserver(this)
        binding = DataBindingUtil.inflate(inflater, getResourceId(), container, false)
        binding.lifecycleOwner = activity
        return binding.root
    }
}
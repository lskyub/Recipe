package kr.co.sbproject.recipe.ui.custom

import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerView {
    abstract class Adapter<ITEM : Any, B : ViewDataBinding>(
        @LayoutRes private val layoutResId: Int,
        private val bindingVariableId: Int? = null
    ) : RecyclerView.Adapter<ViewHolder<B>>() {

        private val items = mutableListOf<ITEM>()

        fun replaceAll(items: List<ITEM>?) {
            items?.let {
                this.items.run {
                    clear()
                    addAll(it)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            object : ViewHolder<B>(
                layoutResId = layoutResId,
                parent = parent,
                bindingVariableId = bindingVariableId
            ) {
            }

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
            holder.onBindViewHolder(items[position])
        }
    }

    abstract class ViewHolder<B : ViewDataBinding>(
        @LayoutRes layoutResId: Int,
        parent: ViewGroup,
        private val bindingVariableId: Int?
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
    ) {

        protected val binding: B = DataBindingUtil.bind(itemView)!!

        fun onBindViewHolder(item: Any?) {
            try {
                bindingVariableId?.let {
                    binding.setVariable(it, item)
                }

                itemView.setOnClickListener {
                    Log.i("sgim","setOnClickListener")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class BaseDecoration : RecyclerView.ItemDecoration {
    var left: Int = 0
    var top: Int = 0
    var right: Int = 0
    var bottom: Int = 0

    constructor(margin: Int = 0) {
        this.left = margin
        this.top = margin
        this.right = margin
        this.bottom = margin
    }

    constructor(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val last = parent.adapter?.itemCount ?: -1
        when (position) {
            0 -> {
                outRect.left = left
                outRect.right = right
            }
            last - 1 -> {
                outRect.right = left
            }
            else -> {
                outRect.right = right
            }
        }
        outRect.top = top
        outRect.bottom = bottom
    }
}

@BindingAdapter("replaceAll")
fun RecyclerView.replaceAll(list: List<Any>?) {
    Log.i("sgim","replaceAll ${list?.size}")
    (this.adapter as? BaseRecyclerView.Adapter<Any, *>)?.run {
        replaceAll(list)
        notifyDataSetChanged()
    }
}
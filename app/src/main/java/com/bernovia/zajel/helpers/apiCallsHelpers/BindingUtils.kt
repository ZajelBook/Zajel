package com.bernovia.zajel.helpers.apiCallsHelpers

import android.content.ContextWrapper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bernovia.zajel.R


@BindingAdapter(value = ["isRefreshing"]) fun isSwipeLayoutRefreshing(view: SwipeRefreshLayout, value: MutableLiveData<Boolean>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null && value != null) {
        value.observe(parentActivity, Observer { view.isRefreshing = it })
    }

}

@BindingAdapter(value = ["setStatus"]) fun setStatusBackground(view: TextView, value: String?) {
    view.text.toString().capitalize()
    if (value == "available") {
        view.backgroundTintList = view.context.resources.getColorStateList(R.color.accept_color, view.context.resources.newTheme())
    } else {
        view.backgroundTintList = view.context.resources.getColorStateList(R.color.reject_color, view.context.resources.newTheme())

    }


}


@BindingAdapter("mutableVisibility") fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value ->
            view.visibility = value ?: View.VISIBLE
        })
    }
}

fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}
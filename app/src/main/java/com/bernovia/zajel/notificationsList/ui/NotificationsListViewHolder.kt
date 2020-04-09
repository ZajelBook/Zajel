package com.bernovia.zajel.notificationsList.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bernovia.zajel.MainActivity
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.ItemNotificationsBinding
import com.bernovia.zajel.helpers.DateUtil.timeAgo
import com.bernovia.zajel.helpers.FragmentSwitcher
import com.bernovia.zajel.messages.ui.MessagesListFragment
import com.bernovia.zajel.notificationsList.models.Notification
import com.bernovia.zajel.requests.ui.RequestsFragment


class NotificationsListViewHolder(var itemBinding: ItemNotificationsBinding, private var fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemBinding.root) {


    companion object {
        fun create(parent: ViewGroup, fragmentManager: FragmentManager): NotificationsListViewHolder {

            val binding: ItemNotificationsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_notifications, parent, false)
            return NotificationsListViewHolder(binding, fragmentManager)
        }
    }

    fun bindTo(data: Notification?) {
        if (data != null) {
            itemBinding.titleTextView.text = data.payload.title.capitalize()
            itemBinding.subjectTextView.text = data.payload.subject.capitalize()
            itemBinding.createdAtTextView.text = timeAgo(data.createdAt)
            itemBinding.root.setOnClickListener {

                when (data.payload.type) {
                    "request_accepted", "borrow_request", "request_rejected" -> {
                        MainActivity.bottomNavigationView.selectedItemId = R.id.navigation_requests
                        FragmentSwitcher.replaceFragmentWithNoAnimation(fragmentManager, R.id.main_content_frameLayout, RequestsFragment.newInstance())
                    }
                    "new_message" -> {
                        if (data.payload.conversationId != null) {
                            FragmentSwitcher.addFragment(fragmentManager, R.id.added_FrameLayout, MessagesListFragment.newInstance(data.payload.conversationId), FragmentSwitcher.AnimationType.PUSH)
                        }
                    }
                }

            }
        }

    }

}
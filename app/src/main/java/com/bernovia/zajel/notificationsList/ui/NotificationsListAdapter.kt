package com.bernovia.zajel.notificationsList.ui

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bernovia.zajel.notificationsList.models.Notification

class NotificationsListAdapter(private var fragmentManager: FragmentManager) : PagedListAdapter<Notification, RecyclerView.ViewHolder>(POST_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotificationsListViewHolder.create(parent, fragmentManager)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as NotificationsListViewHolder

        if (itemCount != 0) holder.bindTo(getItem(position))
    }


    companion object {

        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Notification>() {
            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean = oldItem.id == newItem.id

        }

    }

}
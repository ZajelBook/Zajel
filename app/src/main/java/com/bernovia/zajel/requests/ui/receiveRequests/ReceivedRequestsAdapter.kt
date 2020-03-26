package com.bernovia.zajel.requests.ui.receiveRequests

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bernovia.zajel.requests.models.BookActivity

class ReceivedRequestsAdapter (private var fragmentManager: FragmentManager,private var receivedRequestsClickListener: ReceivedRequestsClickListener) :
    PagedListAdapter<BookActivity, RecyclerView.ViewHolder>(POST_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ReceivedRequestsViewHolder.create(parent, fragmentManager)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ReceivedRequestsViewHolder

        if (itemCount != 0)
            holder.bindTo(getItem(position),receivedRequestsClickListener)
    }


    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<BookActivity>() {
            override fun areContentsTheSame(oldItem: BookActivity, newItem: BookActivity): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: BookActivity, newItem: BookActivity): Boolean =
                oldItem.id == newItem.id

        }

    }
    interface ReceivedRequestsClickListener {
        fun acceptRequestClickListener(data: BookActivity)
        fun rejectRequestClickListener(data: BookActivity)
        fun messageUserClickListener()
    }

}
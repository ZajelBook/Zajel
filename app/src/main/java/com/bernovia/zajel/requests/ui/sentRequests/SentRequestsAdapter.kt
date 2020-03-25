package com.bernovia.zajel.requests.ui.sentRequests

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bernovia.zajel.requests.models.BookActivity

class SentRequestsAdapter(private var fragmentManager: FragmentManager) : PagedListAdapter<BookActivity, RecyclerView.ViewHolder>(POST_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return SentRequestsViewHolder.create(parent, fragmentManager)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as SentRequestsViewHolder

        if (itemCount != 0) holder.bindTo(getItem(position))
    }


    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<BookActivity>() {
            override fun areContentsTheSame(oldItem: BookActivity, newItem: BookActivity): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: BookActivity, newItem: BookActivity): Boolean = oldItem.id == newItem.id

        }

    }

}
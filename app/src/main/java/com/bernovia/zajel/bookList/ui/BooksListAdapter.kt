package com.bernovia.zajel.bookList.ui

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bernovia.zajel.bookList.models.Book

class BooksListAdapter (private var fragmentManager: FragmentManager) :
    PagedListAdapter<Book, RecyclerView.ViewHolder>(POST_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BooksListViewHolder.create(parent, fragmentManager)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as BooksListViewHolder

        if (itemCount != 0)
            holder.bindTo(getItem(position))
    }



    companion object {

        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Book>() {
            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean =
                oldItem.id == newItem.id

        }

    }

}
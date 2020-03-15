package com.bernovia.zajel.bookList.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bernovia.zajel.R
import com.bernovia.zajel.bookList.models.Book
import com.bernovia.zajel.databinding.ItemBookBinding
import com.bernovia.zajel.helpers.ImageUtil


class BooksListViewHolder(var itemBinding: ItemBookBinding, private var fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemBinding.root) {


    companion object {
        fun create(parent: ViewGroup, fragmentManager: FragmentManager): BooksListViewHolder {

            val binding: ItemBookBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_book, parent, false)
            return BooksListViewHolder(binding, fragmentManager)
        }
    }

    fun bindTo(data: Book?) {
        if (data!=null){
            ImageUtil.renderImageWithNoPlaceHolder(data.image, itemBinding.myImageView, itemBinding.root.context)
        }
    }

}
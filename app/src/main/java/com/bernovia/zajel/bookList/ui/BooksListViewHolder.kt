package com.bernovia.zajel.bookList.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bernovia.zajel.R
import com.bernovia.zajel.addBook.AddBookFragment
import com.bernovia.zajel.bookList.models.Book
import com.bernovia.zajel.databinding.ItemBookBinding
import com.bernovia.zajel.helpers.FragmentSwitcher
import com.bernovia.zajel.helpers.ImageUtil
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager


class BooksListViewHolder(var itemBinding: ItemBookBinding, private var fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemBinding.root) {


    companion object {
        fun create(parent: ViewGroup, fragmentManager: FragmentManager): BooksListViewHolder {

            val binding: ItemBookBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_book, parent, false)
            return BooksListViewHolder(binding, fragmentManager)
        }
    }

    fun bindTo(data: Book?) {
        if (data != null) {
            ImageUtil.renderImageWithNoPlaceHolder(data.image, itemBinding.myImageView, itemBinding.root.context)

            itemBinding.titleTextView.text = data.title
            itemBinding.authorTextView.text = data.author


            itemBinding.root.setOnClickListener {
                Log.e("user id", preferenceManager.userId.toString())
                Log.e("user id from book ", data.userId.toString())

                if (data.userId == preferenceManager.userId){
                    FragmentSwitcher.addFragment(fragmentManager, R.id.added_FrameLayout, AddBookFragment.newInstance(data.id), FragmentSwitcher.AnimationType.PUSH)

                }else{
                    FragmentSwitcher.addFragment(fragmentManager, R.id.added_FrameLayout, BookDetailsFragment.newInstance(data.id), FragmentSwitcher.AnimationType.PUSH)

                }

            }
        }


    }

}
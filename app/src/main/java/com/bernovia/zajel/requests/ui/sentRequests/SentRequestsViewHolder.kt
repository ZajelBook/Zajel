package com.bernovia.zajel.requests.ui.sentRequests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.ItemSentRequestBinding
import com.bernovia.zajel.helpers.ImageUtil
import com.bernovia.zajel.requests.models.BookActivity

class SentRequestsViewHolder(var itemBinding: ItemSentRequestBinding, private var fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemBinding.root) {


    companion object {
        fun create(parent: ViewGroup, fragmentManager: FragmentManager): SentRequestsViewHolder {
            val binding: ItemSentRequestBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_sent_request, parent, false)
            return SentRequestsViewHolder(binding, fragmentManager)
        }
    }

    fun bindTo(data: BookActivity?) {
        if (data != null) {
            ImageUtil.renderImageWithNoPlaceHolder(data.book?.image , itemBinding.bookImageView, itemBinding.root.context)
            itemBinding.bookNameTextView.text = data.book?.title
            itemBinding.userNameTextView.text= data.lender?.name


            itemBinding.root.setOnClickListener {

            }
        }

    }

}
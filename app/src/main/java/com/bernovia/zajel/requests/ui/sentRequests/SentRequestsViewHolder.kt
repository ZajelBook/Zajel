package com.bernovia.zajel.requests.ui.sentRequests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bernovia.zajel.R
import com.bernovia.zajel.bookList.ui.BookDetailsFragment
import com.bernovia.zajel.databinding.ItemSentRequestBinding
import com.bernovia.zajel.helpers.FragmentSwitcher
import com.bernovia.zajel.helpers.ImageUtil
import com.bernovia.zajel.helpers.StringsUtil
import com.bernovia.zajel.messages.ui.MessagesListFragment
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
            ImageUtil.renderImageWithNoPlaceHolder(data.book?.image, itemBinding.bookImageView, itemBinding.root.context)
            itemBinding.bookNameTextView.text = data.book?.title
            itemBinding.userNameTextView.text = data.lender?.name
            itemBinding.statusTextView.text = StringsUtil.capitalize(data.status)

            if (data.status == "accepted") {
                itemBinding.statusTextView.backgroundTintList = itemBinding.root.context.resources.getColorStateList(R.color.accept_color, itemBinding.root.context.resources.newTheme())
                itemBinding.messageButton.isEnabled=true
//                itemBinding.messageButton.alpha = 1f

            } else {
                itemBinding.statusTextView.backgroundTintList = itemBinding.root.context.resources.getColorStateList(R.color.reject_color, itemBinding.root.context.resources.newTheme())
                itemBinding.messageButton.isEnabled =false
//                itemBinding.messageButton.alpha = 0.5f

            }

            itemBinding.bookImageView.setOnClickListener {
                if (data.book!= null){
                    FragmentSwitcher.addFragment(fragmentManager,R.id.added_FrameLayout,
                        BookDetailsFragment.newInstance(data.book.id),
                        FragmentSwitcher.AnimationType.PUSH)
                }
            }


            itemBinding.messageButton.setOnClickListener {
                if (data.conversationId != null) {
                    FragmentSwitcher.addFragment(fragmentManager, R.id.added_FrameLayout, MessagesListFragment.newInstance(data.conversationId), FragmentSwitcher.AnimationType.PUSH)
                }

            }

            itemBinding.root.setOnClickListener {

            }
        }

    }

}
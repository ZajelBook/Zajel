package com.bernovia.zajel.bookList.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bernovia.zajel.R
import com.bernovia.zajel.actions.SendRequestRequestBody
import com.bernovia.zajel.actions.cancelRequest.CancelRequestViewModel
import com.bernovia.zajel.actions.sendRequest.SendRequestViewModel
import com.bernovia.zajel.databinding.FragmentBookDetailsBinding
import com.bernovia.zajel.helpers.ImageUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class BookDetailsFragment : Fragment() {
    lateinit var binding: FragmentBookDetailsBinding
    private val booksListViewModel: BooksListViewModel by viewModel()
    private val sendRequestViewModel: SendRequestViewModel by viewModel()
    private val cancelRequestViewModel: CancelRequestViewModel by viewModel()


    companion object {
        fun newInstance(bookId: Int): BookDetailsFragment {
            val args = Bundle()
            args.putInt("book_id", bookId)
            val fragment = BookDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_details, container, false)
        if (arguments != null && arguments?.getInt("book_id") != null) {
            booksListViewModel.getBookById(arguments?.getInt("book_id")!!).observe(viewLifecycleOwner, Observer {
                binding.bookDetails = it
                ImageUtil.renderBlurImage(it.image!!, requireContext(), 50, binding.backgroundImageView, R.drawable.newsletter_placeholder)
                ImageUtil.renderImageWithNoPlaceHolder(it.image!!, binding.bookImageView, requireContext())


                if (binding.bookDetails!!.requested) {
                    binding.borrowBookButton.text = resources.getString(R.string.cancel)

                }else{
                    binding.borrowBookButton.text = resources.getString(R.string.borrow)

                }
            })
        }

        binding.borrowBookButton.setOnClickListener {
            if (binding.bookDetails != null) {
                if (binding.bookDetails!!.requested) {
                    cancelRequestViewModel.getDataFromRetrofit(binding.bookDetails!!.id).observe(viewLifecycleOwner, Observer {
                        binding.borrowBookButton.text = resources.getString(R.string.borrow)
                        booksListViewModel.updateRequested(binding.bookDetails!!.id, false)
                    })
                } else {
                    sendRequestViewModel.getDataFromRetrofit(SendRequestRequestBody(binding.bookDetails!!.id)).observe(viewLifecycleOwner, Observer {
                        binding.borrowBookButton.text = resources.getString(R.string.cancel)
                        booksListViewModel.updateRequested(binding.bookDetails!!.id, true)

                    })
                }
            }
        }

        return binding.root
    }

}

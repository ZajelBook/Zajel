package com.bernovia.zajel.bookList.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.FragmentBookDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class BookDetailsFragment : Fragment() {
    lateinit var binding: FragmentBookDetailsBinding
    private val booksListViewModel: BooksListViewModel by viewModel()

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
            })
        }
        return binding.root
    }

}

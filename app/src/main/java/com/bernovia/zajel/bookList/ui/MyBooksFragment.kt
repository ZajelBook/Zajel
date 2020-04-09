package com.bernovia.zajel.bookList.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bernovia.zajel.R
import com.bernovia.zajel.addBook.AddBookFragment
import com.bernovia.zajel.databinding.FragmentMyBooksBinding
import com.bernovia.zajel.helpers.FragmentSwitcher
import com.bernovia.zajel.helpers.NavigateUtil.closeFragment
import com.bernovia.zajel.helpers.apiCallsHelpers.Status
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class MyBooksFragment : Fragment() {

    lateinit var binding: FragmentMyBooksBinding
    private val booksListViewModel: BooksListViewModel by viewModel()
    var size: Int = 0

    companion object {
        fun newInstance(): MyBooksFragment {
            val args = Bundle()
            val fragment = MyBooksFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val bookListAdapter: BooksListAdapter by lazy {
        BooksListAdapter(requireActivity().supportFragmentManager)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_books, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksListViewModel.refreshPageMyBooks().observe(viewLifecycleOwner, Observer { it.refreshPage() })
        booksListViewModel.dataSourceMyBooks.observe(viewLifecycleOwner, Observer {
            size = it.size
            bookListAdapter.submitList(it)
        })


        binding.booksRecyclerView.apply {
            adapter = bookListAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        binding.swipeContainer.setOnRefreshListener {
            booksListViewModel.refreshPageMyBooks().observe(viewLifecycleOwner, Observer { it.refreshPage() })
        }

        booksListViewModel.networkStateMyBooks.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                binding.swipeContainer.isRefreshing = false
                if (size == 0) {
                    binding.emptyScreenLinearLayout.visibility = View.VISIBLE
                    binding.swipeContainer.visibility = View.GONE
                    binding.emptyScreenButton.setOnClickListener {
                        closeFragment(requireActivity().supportFragmentManager, this)
                        FragmentSwitcher.addFragment(requireActivity().supportFragmentManager, R.id.added_FrameLayout, AddBookFragment.newInstance(0), FragmentSwitcher.AnimationType.PUSH)
                    }
                } else {
                    binding.emptyScreenLinearLayout.visibility = View.GONE
                    binding.swipeContainer.visibility = View.VISIBLE
                }

            }
        })

        binding.backImageButton.setOnClickListener { closeFragment(requireActivity().supportFragmentManager, this) }


    }

}

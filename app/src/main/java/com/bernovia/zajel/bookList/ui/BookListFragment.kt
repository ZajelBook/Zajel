package com.bernovia.zajel.bookList.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.FragmentBookListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class BookListFragment : Fragment() {
    lateinit var binding: FragmentBookListBinding
    private val booksListViewModel: BooksListViewModel by viewModel()


    companion object {
        fun newInstance(): BookListFragment {
            val args = Bundle()
            val fragment = BookListFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private val bookListAdapter: BooksListAdapter by lazy {
        BooksListAdapter(requireActivity().supportFragmentManager)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_list, container, false)
        booksListViewModel.refreshPage().observe(viewLifecycleOwner, Observer { it.refreshPage() })
        booksListViewModel.dataSource.observe(viewLifecycleOwner, Observer {
            bookListAdapter.submitList(it)

        })


        binding.booksRecyclerView.apply {
            adapter = bookListAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }



        return binding.root
    }

}

package com.bernovia.zajel.requests.ui.sentRequests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.FragmentSentRequestsBinding
import com.bernovia.zajel.helpers.apiCallsHelpers.Status
import com.bernovia.zajel.requests.ui.BookActivitiesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class SentRequestsFragment : Fragment() {

    private val bookActivitiesViewModel: BookActivitiesViewModel by viewModel()
    lateinit var binding: FragmentSentRequestsBinding

    companion object {
        fun newInstance(): SentRequestsFragment {
            val args = Bundle()
            val fragment = SentRequestsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val sentRequestsAdapter: SentRequestsAdapter by lazy {
        SentRequestsAdapter(requireActivity().supportFragmentManager)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sent_requests, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookActivitiesViewModel.refreshPageSendRequests().observe(viewLifecycleOwner, Observer { it.refreshPage() })
        bookActivitiesViewModel.sentRequestsDataSource.observe(viewLifecycleOwner, Observer {
            sentRequestsAdapter.submitList(it)
        })
        binding.sentRequestsRecyclerView.apply {
            adapter = sentRequestsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.sentRequestsSwipeRefreshLayout.setOnRefreshListener {
            bookActivitiesViewModel.refreshPageSendRequests().observe(viewLifecycleOwner, Observer { it.refreshPage() })
        }

        bookActivitiesViewModel.sentRequestsNetworkState.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                binding.sentRequestsSwipeRefreshLayout.isRefreshing = false
            }
        })

    }

}

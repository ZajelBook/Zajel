package com.bernovia.zajel.requests.ui.receiveRequests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bernovia.zajel.R
import com.bernovia.zajel.actions.acceptRejectRequest.AcceptRejectRequestViewModel
import com.bernovia.zajel.databinding.FragmentReceivedRequestsBinding
import com.bernovia.zajel.requests.models.BookActivity
import com.bernovia.zajel.requests.ui.BookActivitiesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ReceivedRequestsFragment : Fragment(), ReceivedRequestsAdapter.ReceivedRequestsClickListener {
    lateinit var binding: FragmentReceivedRequestsBinding

    private val bookActivitiesViewModel: BookActivitiesViewModel by viewModel()
    private val acceptRejectRequestViewModel: AcceptRejectRequestViewModel by viewModel()

    companion object {
        fun newInstance(): ReceivedRequestsFragment {
            val args = Bundle()
            val fragment = ReceivedRequestsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val receivedRequestsAdapter: ReceivedRequestsAdapter by lazy {
        ReceivedRequestsAdapter(requireActivity().supportFragmentManager, this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_received_requests, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookActivitiesViewModel.refreshPageReceivedRequests().observe(viewLifecycleOwner, Observer { it.refreshPage() })
        bookActivitiesViewModel.receivedRequestsDataSource.observe(viewLifecycleOwner, Observer {
            receivedRequestsAdapter.submitList(it)
        })

        binding.receivedRequestsRecyclerView.apply {
            adapter = receivedRequestsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    override fun acceptRequestClickListener(data: BookActivity) {
        acceptRejectRequestViewModel.setType("accept")
        acceptRejectRequestViewModel.getDataFromRetrofit(data.id).observe(viewLifecycleOwner, Observer {
            bookActivitiesViewModel.updateBookActivityStatus(data.id,"accepted")

        })

    }

    override fun rejectRequestClickListener(data: BookActivity) {
        acceptRejectRequestViewModel.setType("reject")
        acceptRejectRequestViewModel.getDataFromRetrofit(data.id).observe(viewLifecycleOwner, Observer {
            bookActivitiesViewModel.deleteBookActivity(data)
        })

    }

    override fun messageUserClickListener() {
        TODO("Not yet implemented")
    }
}

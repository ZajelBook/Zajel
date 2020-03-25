package com.bernovia.zajel.requests.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.bernovia.zajel.R
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ReceivedRequestsFragment : Fragment() {

    private val bookActivitiesViewModel: BookActivitiesViewModel by viewModel()

    companion object {
        fun newInstance(): ReceivedRequestsFragment {
            val args = Bundle()
            val fragment = ReceivedRequestsFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_received_requests, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookActivitiesViewModel.refreshPageReceivedRequests().observe(viewLifecycleOwner, Observer { it.refreshPage() })
        bookActivitiesViewModel.receivedRequestsDataSource.observe(viewLifecycleOwner, Observer {


        })
    }
}

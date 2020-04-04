package com.bernovia.zajel.notificationsList.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.FragmentNotificationsListBinding
import com.bernovia.zajel.helpers.apiCallsHelpers.Status
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class NotificationsListFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsListBinding
    private val notificationsListViewModel: NotificationsListViewModel by viewModel()

    private val notificationsListAdapter: NotificationsListAdapter by lazy {
        NotificationsListAdapter(requireActivity().supportFragmentManager)
    }


    companion object {
        fun newInstance(): NotificationsListFragment {
            val args = Bundle()
            val fragment = NotificationsListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationsListViewModel.refreshPage().observe(viewLifecycleOwner, Observer { it.refreshPage() })
        notificationsListViewModel.dataSource.observe(viewLifecycleOwner, Observer {
            notificationsListAdapter.submitList(it)
        })


        binding.notificationsRecyclerView.apply {
            adapter = notificationsListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.swipeContainer.setOnRefreshListener {
            notificationsListViewModel.refreshPage().observe(viewLifecycleOwner, Observer { it.refreshPage() })
        }

        notificationsListViewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                binding.swipeContainer.isRefreshing = false
            }
        })
    }
}

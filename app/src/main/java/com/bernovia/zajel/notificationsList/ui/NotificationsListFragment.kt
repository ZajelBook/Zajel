package com.bernovia.zajel.notificationsList.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bernovia.zajel.MainActivity
import com.bernovia.zajel.R
import com.bernovia.zajel.auth.logIn.ui.LoginActivity
import com.bernovia.zajel.bookList.ui.BookListFragment
import com.bernovia.zajel.databinding.FragmentNotificationsListBinding
import com.bernovia.zajel.helpers.FragmentSwitcher
import com.bernovia.zajel.helpers.NavigateUtil
import com.bernovia.zajel.helpers.ZajelUtil
import com.bernovia.zajel.helpers.apiCallsHelpers.Status
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class NotificationsListFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsListBinding
    private val notificationsListViewModel: NotificationsListViewModel by viewModel()
    var size: Int = 0

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_notifications_list,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ZajelUtil.preferenceManager.accessToken == "" || ZajelUtil.preferenceManager.accessToken == null) {
            binding.emptyScreenLinearLayout.visibility = View.VISIBLE
            binding.swipeContainer.visibility = View.GONE
            binding.emptyScreenTextView.text = getString(R.string.login_to_see_notification)
            binding.emptyScreenButton.text = getString(R.string.login)
            binding.emptyScreenButton.setOnClickListener {
                NavigateUtil.start<LoginActivity>(requireContext())
            }
        } else {
            binding.swipeContainer.isRefreshing = true

            notificationsListViewModel.refreshPage()
                .observe(viewLifecycleOwner, Observer { it.refreshPage() })
            notificationsListViewModel.dataSource.observe(viewLifecycleOwner, Observer {
                size = it.size
                notificationsListAdapter.submitList(it)
            })


            binding.notificationsRecyclerView.apply {
                adapter = notificationsListAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            binding.swipeContainer.setOnRefreshListener {
                notificationsListViewModel.refreshPage()
                    .observe(viewLifecycleOwner, Observer { it.refreshPage() })
            }

            notificationsListViewModel.networkState.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS || it.status == Status.FAILED) {
                    binding.swipeContainer.isRefreshing = false
                    binding.emptyScreenLinearLayout.postDelayed({
                        if (size == 0) {
                            binding.emptyScreenLinearLayout.visibility = View.VISIBLE
                            binding.swipeContainer.visibility = View.GONE
                            binding.emptyScreenButton.setOnClickListener {
                                MainActivity.bottomNavigationView.selectedItemId =
                                    R.id.navigation_home
                                MainActivity.fabButton.visibility = View.VISIBLE
                                FragmentSwitcher.replaceFragmentWithNoAnimation(
                                    requireActivity().supportFragmentManager,
                                    R.id.main_content_frameLayout,
                                    BookListFragment.newInstance()
                                )
                            }
                        } else {
                            binding.emptyScreenLinearLayout.visibility = View.GONE
                            binding.swipeContainer.visibility = View.VISIBLE
                        }
                    }, 200)
                }
            })
        }
    }
}

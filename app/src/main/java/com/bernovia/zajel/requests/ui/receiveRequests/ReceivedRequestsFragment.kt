package com.bernovia.zajel.requests.ui.receiveRequests

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
import com.bernovia.zajel.actions.acceptRejectRequest.AcceptRejectRequestViewModel
import com.bernovia.zajel.auth.logIn.ui.LoginActivity
import com.bernovia.zajel.bookList.ui.BookListFragment
import com.bernovia.zajel.databinding.FragmentReceivedRequestsBinding
import com.bernovia.zajel.helpers.FragmentSwitcher
import com.bernovia.zajel.helpers.NavigateUtil
import com.bernovia.zajel.helpers.ZajelUtil
import com.bernovia.zajel.helpers.apiCallsHelpers.Status
import com.bernovia.zajel.messages.ui.MessagesListFragment
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
    var size: Int = 0

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_received_requests, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ZajelUtil.preferenceManager.accessToken == "" || ZajelUtil.preferenceManager.accessToken == null) {
            binding.emptyScreenLinearLayout.visibility = View.VISIBLE
            binding.receivedRequestsSwipeRefreshLayout.visibility = View.GONE
            binding.emptyScreenTextView.text = getString(R.string.login_to_see_reqeuests)
            binding.emptyScreenButton.text = getString(R.string.login)
            binding.emptyScreenButton.setOnClickListener {
                NavigateUtil.start<LoginActivity>(requireContext())
            }
        } else {

            binding.receivedRequestsSwipeRefreshLayout.isRefreshing = true

            bookActivitiesViewModel.refreshPageReceivedRequests()
                .observe(viewLifecycleOwner, Observer { it.refreshPage() })
            bookActivitiesViewModel.receivedRequestsDataSource.observe(
                viewLifecycleOwner,
                Observer {
                    size = it.size
                    receivedRequestsAdapter.submitList(it)
                })

            binding.receivedRequestsRecyclerView.apply {
                adapter = receivedRequestsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            binding.receivedRequestsSwipeRefreshLayout.setOnRefreshListener {
                bookActivitiesViewModel.refreshPageReceivedRequests()
                    .observe(viewLifecycleOwner, Observer { it.refreshPage() })
            }

            bookActivitiesViewModel.receivedRequestsNetworkState.observe(
                viewLifecycleOwner,
                Observer {

                    if (it.status == Status.SUCCESS || it.status == Status.FAILED) {
                        binding.receivedRequestsSwipeRefreshLayout.isRefreshing = false
                        binding.emptyScreenLinearLayout.postDelayed({
                            if (size == 0) {
                                binding.emptyScreenLinearLayout.visibility = View.VISIBLE
                                binding.receivedRequestsSwipeRefreshLayout.visibility = View.GONE
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
                                binding.receivedRequestsSwipeRefreshLayout.visibility = View.VISIBLE
                            }
                        }, 200)

                    }
                })
        }
    }

    override fun acceptRequestClickListener(data: BookActivity) {
        acceptRejectRequestViewModel.setType("accept")
        acceptRejectRequestViewModel.getDataFromRetrofit(data.id)
            .observe(viewLifecycleOwner, Observer {
                bookActivitiesViewModel.refreshPageReceivedRequests()
                    .observe(viewLifecycleOwner, Observer { it.refreshPage() })


            })

    }

    override fun rejectRequestClickListener(data: BookActivity) {
        acceptRejectRequestViewModel.setType("reject")
        acceptRejectRequestViewModel.getDataFromRetrofit(data.id)
            .observe(viewLifecycleOwner, Observer {
                bookActivitiesViewModel.deleteBookActivity(data)
            })

    }

    override fun messageUserClickListener(data: BookActivity) {
        if (data.conversationId != null) {
            FragmentSwitcher.addFragment(
                requireActivity().supportFragmentManager,
                R.id.added_FrameLayout,
                MessagesListFragment.newInstance(data.conversationId),
                FragmentSwitcher.AnimationType.PUSH
            )
        }

    }
}

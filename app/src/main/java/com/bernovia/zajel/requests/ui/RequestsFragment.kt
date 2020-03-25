package com.bernovia.zajel.requests.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.FragmentRequestsBinding
import com.bernovia.zajel.requests.ui.receiveRequests.ReceivedRequestsFragment
import com.bernovia.zajel.requests.ui.sentRequests.SentRequestsFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
 * A simple [Fragment] subclass.
 */
class RequestsFragment : Fragment() {
    lateinit var binding: FragmentRequestsBinding

    companion object {
        fun newInstance(): RequestsFragment {
            val args = Bundle()
            val fragment = RequestsFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_requests, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        binding.requestsViewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.requestsTablayout, binding.requestsViewPager) { tab, position ->

            if (position == 0) {
                tab.text = resources.getString(R.string.sent_requests)
            } else {
                tab.text = resources.getString(R.string.received_requests)

            }

        }.attach()
    }


    private inner class ScreenSlidePagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    SentRequestsFragment.newInstance()
                }
                else -> {
                    ReceivedRequestsFragment.newInstance()
                }
            }
        }
    }

}

package com.bernovia.zajel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bernovia.zajel.databinding.FragmentWebViewBinding
import com.bernovia.zajel.helpers.NavigateUtil.closeFragment

/**
 * A simple [Fragment] subclass.
 */
class WebViewFragment : Fragment() {
    lateinit var binding: FragmentWebViewBinding


    companion object {
        fun newInstance(pageTitle: String, url: String): WebViewFragment {
            val args = Bundle()
            args.putString("url_link", url)
            args.putString("page_title", pageTitle)

            val fragment = WebViewFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_view, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val url = arguments?.getString("url_link")
            binding.webView.loadUrl(url)
            val webSettings: WebSettings = binding.webView.settings
            webSettings.javaScriptEnabled = true

            binding.pageTitleTextView.text = arguments?.getString("page_title")

        }
        binding.backImageButton.setOnClickListener { closeFragment(requireActivity().supportFragmentManager, this) }

    }

}

package com.bernovia.zajel

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bernovia.zajel.databinding.FragmentWebViewBinding
import com.bernovia.zajel.helpers.NavigateUtil.closeFragment
import com.bernovia.zajel.splashScreen.ui.MetaDataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */
class WebViewFragment : Fragment() {
    lateinit var binding: FragmentWebViewBinding
    private val metaDataViewModel: MetaDataViewModel by viewModel()


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
        binding.backImageButton.setOnClickListener { closeFragment(requireActivity().supportFragmentManager, this) }

        if (arguments != null) {
            binding.pageTitleTextView.text = arguments?.getString("page_title")

            val url = arguments?.getString("url_link")

            metaDataViewModel.getMetaData().observe(viewLifecycleOwner, Observer {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.contentTextView.text = Html.fromHtml(it?.privacy?.content, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    binding.contentTextView.text = Html.fromHtml(it?.privacy?.content)
                }

            })


        }

    }

}

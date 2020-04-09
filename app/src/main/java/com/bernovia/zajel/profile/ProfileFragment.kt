package com.bernovia.zajel.profile

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bernovia.zajel.AppDatabase
import com.bernovia.zajel.R
import com.bernovia.zajel.WebViewFragment
import com.bernovia.zajel.actions.logout.LogoutViewModel
import com.bernovia.zajel.auth.logIn.ui.LoginActivity
import com.bernovia.zajel.bookList.ui.MyBooksFragment
import com.bernovia.zajel.databinding.FragmentProfileBinding
import com.bernovia.zajel.editProfile.EditProfileFragment
import com.bernovia.zajel.helpers.FragmentSwitcher
import com.bernovia.zajel.helpers.NavigateUtil
import com.bernovia.zajel.helpers.ZajelUtil
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager
import com.bernovia.zajel.helpers.ZajelUtil.singleItemClick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentProfileBinding
    private val logoutViewModel: LogoutViewModel by viewModel()

    companion object {
        fun newInstance(): ProfileFragment {
            val args = Bundle()
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userNameTextView.text = preferenceManager.userName

        var versionNumber: String? = null
        try {
            versionNumber = "V " + requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName
        }
        catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        binding.versionNumberTextView.text = versionNumber

        binding.nameRelativeLayout.setOnClickListener(this)
        binding.myBooksRelativeLayout.setOnClickListener(this)
        binding.termsRelativeLayout.setOnClickListener(this)
        binding.privacyPolicyRelativeLayout.setOnClickListener(this)
        binding.logoutRelativeLayout.setOnClickListener(this)
        binding.aboutRelativeLayout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.name_RelativeLayout -> {
                if (singleItemClick()) return

                FragmentSwitcher.addFragment(requireActivity().supportFragmentManager, R.id.added_FrameLayout, EditProfileFragment.newInstance(), FragmentSwitcher.AnimationType.PUSH)

            }
            R.id.my_books_RelativeLayout -> {
                if (singleItemClick()) return
                FragmentSwitcher.addFragment(requireActivity().supportFragmentManager, R.id.added_FrameLayout, MyBooksFragment.newInstance(), FragmentSwitcher.AnimationType.PUSH)
            }
            R.id.terms_RelativeLayout -> {
                if (singleItemClick()) return
                FragmentSwitcher.addFragment(requireActivity().supportFragmentManager,
                    R.id.added_FrameLayout,
                    WebViewFragment.newInstance(getString(R.string.terms_and_conditions), getString(R.string.terms_link)),
                    FragmentSwitcher.AnimationType.PUSH)
            }
            R.id.privacy_policy_RelativeLayout -> {
                if (singleItemClick()) return
                FragmentSwitcher.addFragment(requireActivity().supportFragmentManager,
                    R.id.added_FrameLayout,
                    WebViewFragment.newInstance(getString(R.string.privacy_policy), getString(R.string.privacy_link)),
                    FragmentSwitcher.AnimationType.PUSH)
            }
            R.id.about_RelativeLayout -> {
                if (singleItemClick()) return
                FragmentSwitcher.addFragment(requireActivity().supportFragmentManager,
                    R.id.added_FrameLayout,
                    WebViewFragment.newInstance(getString(R.string.about), getString(R.string.about_link)),
                    FragmentSwitcher.AnimationType.PUSH)
            }
            R.id.logout_RelativeLayout -> {
                if (singleItemClick()) return
                CoroutineScope(Dispatchers.IO).launch {
                    AppDatabase.getInstance(requireContext()).clearAllTables()
                }

                NavigateUtil.start<LoginActivity>(requireContext())
                requireActivity().finish()
                logoutViewModel.getDataFromRetrofit().observe(viewLifecycleOwner, Observer {
                    preferenceManager.clear()

                })
            }
        }

    }
}

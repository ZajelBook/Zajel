package com.bernovia.zajel.auth.updatePassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bernovia.zajel.AppDatabase
import com.bernovia.zajel.R
import com.bernovia.zajel.actions.logout.LogoutViewModel
import com.bernovia.zajel.auth.logIn.ui.LoginActivity
import com.bernovia.zajel.databinding.FragmentUpdatePasswordBinding
import com.bernovia.zajel.helpers.NavigateUtil
import com.bernovia.zajel.helpers.TextWatcherAdapter
import com.bernovia.zajel.helpers.ValidateUtil
import com.bernovia.zajel.helpers.ZajelUtil
import com.bernovia.zajel.helpers.ZajelUtil.hideKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class UpdatePasswordFragment : Fragment(), View.OnClickListener, TextWatcherAdapter.TextWatcherListener {

    lateinit var binding: FragmentUpdatePasswordBinding
    private val logoutViewModel: LogoutViewModel by viewModel()
    private val updatePasswordViewModel: UpdatePasswordViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_password, container, false)
        setOnClickListeners()
        addTextChangedListeners()

        return binding.root
    }

    companion object {
        fun newInstance() = UpdatePasswordFragment().apply {}
    }


    private fun setOnClickListeners() {
        binding.backImageButton.setOnClickListener(this)
        binding.updatePasswordButton.setOnClickListener(this)
    }

    private fun addTextChangedListeners() {
        binding.confirmPasswordEditText.addTextChangedListener(TextWatcherAdapter(binding.confirmPasswordEditText, this))
        binding.newPasswordEditText.addTextChangedListener(TextWatcherAdapter(binding.newPasswordEditText, this))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_ImageButton -> NavigateUtil.closeFragment(requireActivity().supportFragmentManager, this)
            R.id.update_password_Button -> submitUpdatePasswordForm()
        }
    }

    private fun submitUpdatePasswordForm() {
        hideKeyboard(requireActivity())

        if (!ValidateUtil.validatePassword(binding.newPasswordEditText, binding.newPasswordTextInputLayout, requireActivity())) {
            return
        }
        if (!ValidateUtil.validateFieldsDidnotMatch(binding.confirmPasswordEditText,
                binding.confirmPasswordTextInputLayout,
                requireActivity(),
                binding.newPasswordEditText,
                getString(R.string.password_not_match))) {
            return
        }
        if (binding.newPasswordEditText.text != null && binding.confirmPasswordEditText.text != null) sendUpdatePasswordRequest()
    }

    override fun onTextChanged(view: EditText?, text: String?) {
        when (view?.id) {
            R.id.new_password_EditText -> ValidateUtil.validatePassword(binding.newPasswordEditText, binding.newPasswordTextInputLayout, requireActivity())
            R.id.confirm_password_EditText -> ValidateUtil.validateFieldsDidnotMatch(binding.confirmPasswordEditText,
                binding.confirmPasswordTextInputLayout,
                requireActivity(),
                binding.newPasswordEditText,
                getString(R.string.password_not_match))
        }
    }


    private fun sendUpdatePasswordRequest() {
        updatePasswordViewModel.getDataFromRetrofit(UpdatePasswordRequestBody(binding.newPasswordEditText.text.toString(), binding.confirmPasswordEditText.text.toString())).observe(this, {
            if (it != null) {
                if (it.isSuccessful) {
                    CoroutineScope(Dispatchers.IO).launch {
                        AppDatabase.getInstance(requireContext()).clearAllTables()
                    }

                    NavigateUtil.start<LoginActivity>(requireContext())
                    requireActivity().finish()
                    logoutViewModel.getDataFromRetrofit().observe(viewLifecycleOwner, {
                        ZajelUtil.preferenceManager.clear()

                    })
                }

            }
        })
    }

}
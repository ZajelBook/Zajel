package com.bernovia.zajel.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.FragmentEditProfileBinding
import com.bernovia.zajel.editProfile.models.EditProfileRequestBody
import com.bernovia.zajel.editProfile.ui.EditProfileViewModel
import com.bernovia.zajel.editProfile.ui.GetProfileViewModel
import com.bernovia.zajel.helpers.DateUtil
import com.bernovia.zajel.helpers.NavigateUtil.closeFragment
import com.bernovia.zajel.helpers.TextWatcherAdapter
import com.bernovia.zajel.helpers.ValidateUtil
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class EditProfileFragment : Fragment(), View.OnClickListener, TextWatcherAdapter.TextWatcherListener {
    private lateinit var binding: FragmentEditProfileBinding
    private val getProfileViewModel: GetProfileViewModel by viewModel()
    private val editProfileViewModel: EditProfileViewModel by viewModel()


    companion object {
        fun newInstance(): EditProfileFragment {
            val args = Bundle()
            val fragment = EditProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editProfileViewModel.setUserId(preferenceManager.userId)
        getProfileViewModel.getDataFromRetrofit(preferenceManager.userId).observe(viewLifecycleOwner, Observer {
            binding.firstNameEditText.setText(it.body()?.firstName)
            binding.lastnameEditText.setText(it.body()?.lastName)
            binding.phoneNumberEditText.setText(it.body()?.phoneNumber)
            binding.dateEditText.setText(it.body()?.birthDate)

        })
        binding.backImageButton.setOnClickListener(this)
        binding.doneButton.setOnClickListener(this)
        binding.dateEditText.setOnClickListener(this)
        binding.firstNameEditText.addTextChangedListener(TextWatcherAdapter(binding.firstNameEditText, this))
        binding.lastnameEditText.addTextChangedListener(TextWatcherAdapter(binding.lastnameEditText, this))
        binding.dateEditText.addTextChangedListener(TextWatcherAdapter(binding.dateEditText, this))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_ImageButton -> closeFragment(requireActivity().supportFragmentManager, this)
            R.id.date_EditText -> DateUtil.openDatePickerAndUpdateText(binding.dateEditText, requireActivity().supportFragmentManager)
            R.id.done_Button -> submitForm()
        }
    }

    override fun onTextChanged(view: EditText?, text: String?) {
        when (view?.id) {
            R.id.first_name_EditText -> ValidateUtil.validateEmptyField(binding.firstNameEditText, binding.firstNameTextInputLayout, requireActivity(), resources.getString(R.string.empty_name))
            R.id.lastname_EditText -> ValidateUtil.validateEmptyField(binding.lastnameEditText, binding.lastnameTextInputLayout, requireActivity(), resources.getString(R.string.empty_last_name))
            R.id.phone_number_EditText -> ValidateUtil.validateEmptyField(binding.phoneNumberEditText,
                binding.phoneNumberTextInputLayout,
                requireActivity(),
                resources.getString(R.string.empty_phone_number))
            R.id.date_EditText -> ValidateUtil.validateEmptyField(binding.dateEditText, binding.dateTextInputLayout, requireActivity(), resources.getString(R.string.empty_date))
        }
    }

    private fun submitForm() {
        if (!ValidateUtil.validateEmptyField(binding.firstNameEditText, binding.firstNameTextInputLayout, requireActivity(), resources.getString(R.string.empty_name))) return
        if (!ValidateUtil.validateEmptyField(binding.lastnameEditText, binding.lastnameTextInputLayout, requireActivity(), resources.getString(R.string.empty_last_name))) return
        if (!ValidateUtil.validateEmptyField(binding.phoneNumberEditText, binding.phoneNumberTextInputLayout, requireActivity(), resources.getString(R.string.empty_phone_number))) return
        if (!ValidateUtil.validateEmptyField(binding.dateEditText, binding.dateTextInputLayout, requireActivity(), resources.getString(R.string.empty_date))) return

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new Instance ID token
            val token = task.result?.token
            editProfileViewModel.getDataFromRetrofit(EditProfileRequestBody(token,
                binding.dateEditText.text.toString(),
                binding.firstNameEditText.text.toString(),
                binding.lastnameEditText.text.toString(),
                binding.phoneNumberEditText.text.toString())).observe(viewLifecycleOwner, Observer {
                if (it.isSuccessful) {
                    preferenceManager.userName = binding.firstNameEditText.text.toString() + " " + binding.lastnameEditText.text.toString()

                    Toast.makeText(requireContext(),getString(R.string.profile_updated_),Toast.LENGTH_SHORT).show()
                    closeFragment(requireActivity().supportFragmentManager, this)
                }

            })

        })


    }


}

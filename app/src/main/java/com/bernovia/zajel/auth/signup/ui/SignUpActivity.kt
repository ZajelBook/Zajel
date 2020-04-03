package com.bernovia.zajel.auth.signup.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bernovia.zajel.AskForLocationActivity
import com.bernovia.zajel.R
import com.bernovia.zajel.WebViewFragment
import com.bernovia.zajel.auth.logIn.ui.LoginActivity
import com.bernovia.zajel.auth.signup.models.SignUpRequestBody
import com.bernovia.zajel.databinding.ActivitySignUpBinding
import com.bernovia.zajel.helpers.DateUtil.openDatePickerAndUpdateText
import com.bernovia.zajel.helpers.FragmentSwitcher
import com.bernovia.zajel.helpers.NavigateUtil.start
import com.bernovia.zajel.helpers.TextWatcherAdapter
import com.bernovia.zajel.helpers.ValidateUtil.validateEmail
import com.bernovia.zajel.helpers.ValidateUtil.validateEmptyField
import com.bernovia.zajel.helpers.ValidateUtil.validateFieldsDidnotMatch
import com.bernovia.zajel.helpers.ValidateUtil.validatePassword
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager
import com.bernovia.zajel.helpers.ZajelUtil.setHeaders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.jaychang.st.SimpleText
import okhttp3.Headers
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : AppCompatActivity(), View.OnClickListener, TextWatcherAdapter.TextWatcherListener {
    private lateinit var binding: ActivitySignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.signupButton.setOnClickListener(this)
        binding.signInLinearLayout.setOnClickListener(this)
        binding.termsTextView.setOnClickListener(this)
        binding.dateEditText.setOnClickListener(this)

        binding.firstNameEditText.addTextChangedListener(TextWatcherAdapter(binding.firstNameEditText, this))
        binding.lastnameEditText.addTextChangedListener(TextWatcherAdapter(binding.lastnameEditText, this))
        binding.emailEditText.addTextChangedListener(TextWatcherAdapter(binding.emailEditText, this))
        binding.passwordEditText.addTextChangedListener(TextWatcherAdapter(binding.passwordEditText, this))
        binding.confirmPasswordEditText.addTextChangedListener(TextWatcherAdapter(binding.confirmPasswordEditText, this))
        binding.dateEditText.addTextChangedListener(TextWatcherAdapter(binding.dateEditText, this))

        try {
            val text = resources.getString(R.string.by_signing_up_you_agree_on_our_terms_and_conditions)
            val termsurl = resources.getString(R.string.terms_link)
            val privacyUrl = resources.getString(R.string.privacy_link)
            val simpleText =
                SimpleText.from(text).first(getString(R.string.privacy_policy)).textColor(R.color.colorPrimary).background(R.color.white).pressedTextColor(R.color.colorPrimary).bold().onClick(
                        binding.termsTextView) { _, _, _ ->
                        FragmentSwitcher.addFragment(supportFragmentManager,
                            R.id.added_FrameLayout,
                            WebViewFragment.newInstance(getString(R.string.privacy_policy), privacyUrl),
                            FragmentSwitcher.AnimationType.PUSH)
                    }

                    .first(getString(R.string.terms)).textColor(R.color.colorPrimary).background(R.color.white).pressedTextColor(R.color.colorPrimary).bold().onClick(binding.termsTextView) { _, _, _ ->
                        FragmentSwitcher.addFragment(supportFragmentManager,
                            R.id.added_FrameLayout,
                            WebViewFragment.newInstance(getString(R.string.terms_and_conditions), termsurl),
                            FragmentSwitcher.AnimationType.PUSH)

                    }
            binding.termsTextView.text = simpleText
        }
        catch (e: Exception) {
            binding.termsTextView.visibility = View.GONE
        }


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.date_EditText -> openDatePickerAndUpdateText(binding.dateEditText, supportFragmentManager)
            R.id.signup_Button -> submitForm()
            R.id.sign_in_LinearLayout -> {
                start<LoginActivity>(this)
                finish()
            }
        }
    }

    override fun onTextChanged(view: EditText?, text: String?) {
        when (view?.id) {
            R.id.first_name_EditText -> validateEmptyField(binding.firstNameEditText, binding.firstNameTextInputLayout, this, resources.getString(R.string.empty_name))
            R.id.lastname_EditText -> validateEmptyField(binding.lastnameEditText, binding.lastnameTextInputLayout, this, resources.getString(R.string.empty_last_name))
            R.id.email_EditText -> validateEmail(binding.emailEditText, binding.emailTextInputLayout, this)
            R.id.password_EditText -> validatePassword(binding.passwordEditText, binding.passwordTextInputLayout, this)
            R.id.confirm_password_EditText -> validateFieldsDidnotMatch(binding.confirmPasswordEditText,
                binding.confirmPasswordTextInputLayout,
                this,
                binding.passwordEditText,
                resources.getString(R.string.password_not_match))
            R.id.phone_number_EditText -> validateEmptyField(binding.phoneNumberEditText, binding.phoneNumberTextInputLayout, this, resources.getString(R.string.empty_phone_number))
            R.id.date_EditText -> validateEmptyField(binding.dateEditText, binding.dateTextInputLayout, this, resources.getString(R.string.empty_date))

        }

    }


    private fun submitForm() {
        if (!validateEmptyField(binding.firstNameEditText, binding.firstNameTextInputLayout, this, resources.getString(R.string.empty_name))) return
        if (!validateEmptyField(binding.lastnameEditText, binding.lastnameTextInputLayout, this, resources.getString(R.string.empty_last_name))) return
        if (!validateEmail(binding.emailEditText, binding.emailTextInputLayout, this)) return
        if (!validatePassword(binding.passwordEditText, binding.passwordTextInputLayout, this)) return
        if (!validateFieldsDidnotMatch(binding.confirmPasswordEditText,
                binding.confirmPasswordTextInputLayout,
                this,
                binding.passwordEditText,
                resources.getString(R.string.password_not_match))) return
        if (!validateEmptyField(binding.phoneNumberEditText, binding.phoneNumberTextInputLayout, this, resources.getString(R.string.empty_phone_number))) return
        if (!validateEmptyField(binding.dateEditText, binding.dateTextInputLayout, this, resources.getString(R.string.empty_date))) return

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new Instance ID token
            val token = task.result?.token
            signUpViewModel.getDataFromRetrofit(SignUpRequestBody(binding.dateEditText.text.toString(),
                binding.emailEditText.text.toString(),
                token,
                binding.firstNameEditText.text.toString(),
                binding.lastnameEditText.text.toString(),
                binding.passwordEditText.text.toString(),
                binding.confirmPasswordEditText.text.toString(),
                binding.phoneNumberEditText.text.toString())).observe(this, Observer {

                if (it.isSuccessful) {
                    val headers: Headers = it.headers()
                    preferenceManager.userId = it.body()?.data?.id!!
                    preferenceManager.userName = it.body()?.data?.firstName + " " + it.body()?.data?.lastName
                    setHeaders(headers, preferenceManager)
                    start<AskForLocationActivity>(this)
                    finish()
                }

            })

        })


    }

}

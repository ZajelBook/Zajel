package com.bernovia.zajel.auth.signup.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bernovia.zajel.MainActivity
import com.bernovia.zajel.R
import com.bernovia.zajel.auth.logIn.ui.LoginActivity
import com.bernovia.zajel.auth.signup.models.SignUpRequestBody
import com.bernovia.zajel.databinding.ActivitySignUpBinding
import com.bernovia.zajel.helpers.DateUtil.dateListener
import com.bernovia.zajel.helpers.DateUtil.myCalendar
import com.bernovia.zajel.helpers.DateUtil.showDatePicker
import com.bernovia.zajel.helpers.NavigateUtil.start
import com.bernovia.zajel.helpers.PreferenceManager
import com.bernovia.zajel.helpers.TextWatcherAdapter
import com.bernovia.zajel.helpers.ValidateUtil.validateEmail
import com.bernovia.zajel.helpers.ValidateUtil.validateEmptyField
import com.bernovia.zajel.helpers.ValidateUtil.validateFieldsDidnotMatch
import com.bernovia.zajel.helpers.ValidateUtil.validatePassword
import com.bernovia.zajel.helpers.ZajelUtil.setHeaders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import okhttp3.Headers
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : AppCompatActivity(), View.OnClickListener, TextWatcherAdapter.TextWatcherListener {
    private lateinit var binding: ActivitySignUpBinding
    private val preferenceManager: PreferenceManager = PreferenceManager.instance
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


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.date_EditText -> showDatePicker(this, myCalendar, dateListener(binding.dateEditText))
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
                    setHeaders(headers, preferenceManager)
                    start<MainActivity>(this)
                    finish()
                }

            })

        })


    }

}

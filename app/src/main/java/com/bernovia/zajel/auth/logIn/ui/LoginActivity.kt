package com.bernovia.zajel.auth.logIn.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bernovia.zajel.AskForLocationActivity
import com.bernovia.zajel.R
import com.bernovia.zajel.auth.logIn.models.LoginRequestBody
import com.bernovia.zajel.auth.signup.ui.SignUpActivity
import com.bernovia.zajel.databinding.ActivityLoginBinding
import com.bernovia.zajel.helpers.NavigateUtil.start
import com.bernovia.zajel.helpers.TextWatcherAdapter
import com.bernovia.zajel.helpers.ValidateUtil.validateEmail
import com.bernovia.zajel.helpers.ValidateUtil.validatePassword
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager
import com.bernovia.zajel.helpers.ZajelUtil.setHeaders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import okhttp3.Headers
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(), TextWatcherAdapter.TextWatcherListener, View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.emailEditText.addTextChangedListener(TextWatcherAdapter(binding.emailEditText, this))
        binding.passwordEditText.addTextChangedListener(TextWatcherAdapter(binding.passwordEditText, this))
        binding.loginButton.setOnClickListener(this)
        binding.signupTextView.setOnClickListener(this)
        binding.forgotPasswordTextView.setOnClickListener(this)

    }

    override fun onTextChanged(view: EditText?, text: String?) {
        when (view?.id) {
            R.id.email_EditText -> validateEmail(binding.emailEditText, binding.emailTextInputLayout, this)
            R.id.password_EditText -> validatePassword(binding.passwordEditText, binding.passwordTextInputLayout, this)

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_Button -> submitForm()
            R.id.signup_TextView -> {
                start<SignUpActivity>(this)
                finish()
            }
            R.id.forgot_password_TextView -> {
            }

        }
    }

    private fun submitForm() {
        if (!validateEmail(binding.emailEditText, binding.emailTextInputLayout, this)) return
        if (!validatePassword(binding.passwordEditText, binding.passwordTextInputLayout, this)) return
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new Instance ID token
            val token = task.result?.token
            loginViewModel.getDataFromRetrofit(LoginRequestBody(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())).observe(this, Observer {
                if (it.isSuccessful) {
                    val headers: Headers = it.headers()
                    preferenceManager.userId = it.body()?.data?.id!!
                    preferenceManager.userName = it.body()?.data?.firstName +" " + it.body()?.data?.lastName
                    setHeaders(headers, preferenceManager)
                    start<AskForLocationActivity>(this)
                    finish()
                }
            })
        })


    }

}

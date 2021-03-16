package com.bernovia.zajel.auth.activateEmail

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bernovia.zajel.AskForLocationActivity
import com.bernovia.zajel.MainActivity
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.ActivityActivateEmailBinding
import com.bernovia.zajel.helpers.NavigateUtil
import com.bernovia.zajel.helpers.TextWatcherAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivateEmailActivity : AppCompatActivity(), TextWatcherAdapter.TextWatcherListener {

    lateinit var binding: ActivityActivateEmailBinding
    private val activateEmailViewModel: ActivateEmailViewModel by viewModel()
    private val resendEmailViewModel: ResendEmailViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_activate_email)
        binding.pinEditText.addTextChangedListener(TextWatcherAdapter(binding.pinEditText, this))

        binding.nextButton.setOnClickListener {
            activateEmailViewModel.getDataFromRetrofit(ActivateEmailRequestBody(binding.pinEditText.text.toString())).observe(this, Observer {
                if (it?.isSuccessful!!) {
                    NavigateUtil.start<AskForLocationActivity>(this)
                    if (MainActivity.activity != null) MainActivity.activity.finish()
                    finish()
                }
            })

        }

        binding.resendTextView.setOnClickListener {
            resendEmailViewModel.getDataFromRetrofit().observe(this, Observer {
                if (it?.isSuccessful!!) {
                    Toast.makeText(applicationContext, getString(R.string.email_sent), Toast.LENGTH_LONG).show()
                }
            })

        }

    }

    override fun onTextChanged(view: EditText?, text: String?) {
        if (text != null) {
            binding.nextButton.isEnabled = text.length == 4
        }
    }

}

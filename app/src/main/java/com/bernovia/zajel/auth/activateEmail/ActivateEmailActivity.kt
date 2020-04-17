package com.bernovia.zajel.auth.activateEmail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_activate_email)
        binding.etPin.addTextChangedListener(TextWatcherAdapter(binding.etPin, this))

        binding.nextButton.setOnClickListener {
            activateEmailViewModel.getDataFromRetrofit(ActivateEmailRequestBody(binding.etPin.text.toString()))
                .observe(this,
                    Observer {
                        NavigateUtil.start<AskForLocationActivity>(this)
                        if (MainActivity.activity != null)
                            MainActivity.activity.finish()
                        finish()
                    })

        }

    }

    override fun onTextChanged(view: EditText?, text: String?) {
        if (text != null) {
            binding.nextButton.isEnabled = text.length == 4
        }
    }

}

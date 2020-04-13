package com.bernovia.zajel.auth.activateEmail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bernovia.zajel.AskForLocationActivity
import com.bernovia.zajel.R
import com.bernovia.zajel.databinding.ActivityActivateEmailBinding
import com.bernovia.zajel.helpers.NavigateUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivateEmailActivity : AppCompatActivity() {

    lateinit var binding: ActivityActivateEmailBinding
    private val activateEmailViewModel: ActivateEmailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_activate_email)


        binding.nextButton.setOnClickListener {
            activateEmailViewModel.getDataFromRetrofit(ActivateEmailRequestBody(binding.etPin.text.toString()))
                .observe(this,
                    Observer {

                        NavigateUtil.start<AskForLocationActivity>(this)
                        finish()
                    })

        }

    }

}

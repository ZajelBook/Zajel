package com.bernovia.zajel.splashScreen.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bernovia.zajel.AskForLocationActivity
import com.bernovia.zajel.R
import com.bernovia.zajel.auth.logIn.ui.LoginActivity
import com.bernovia.zajel.helpers.NavigateUtil
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val metaDataViewModel: MetaDataViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        metaDataViewModel.insertMetaDataInLocal()
        if (preferenceManager.accessToken == "" || preferenceManager.accessToken == null) {
            NavigateUtil.start<LoginActivity>(applicationContext)
            finish()
        } else {
            val i = Intent(applicationContext, AskForLocationActivity::class.java)
            if (intent.extras != null) {
                i.putExtra("type", intent.extras!!.getString("type"))
                i.putExtra("conversation_id", intent.extras!!.getInt("conversation_id"))
            }
            startActivity(i)
            finish()
        }

    }
}

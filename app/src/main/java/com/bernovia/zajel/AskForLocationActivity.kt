package com.bernovia.zajel

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bernovia.zajel.databinding.ActivityAskForLocationBinding
import com.bernovia.zajel.editProfile.ui.EditProfileViewModel
import com.bernovia.zajel.helpers.LocationUtil
import com.bernovia.zajel.helpers.LocationUtil.LOCATION_REQUEST
import com.bernovia.zajel.helpers.LocationUtil.getLocationAndSendItToServer
import com.bernovia.zajel.helpers.NavigateUtil
import com.bernovia.zajel.helpers.PreferenceManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class AskForLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAskForLocationBinding
    private val editProfileViewModel: EditProfileViewModel by viewModel()

    private lateinit var preferenceManager: PreferenceManager
    private var loadOnce: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ask_for_location)
        editProfileViewModel.setUserId(preferenceManager.userId)
        preferenceManager = PreferenceManager.instance
        setUpAllowLocation()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        } else { // Permission has already been granted
            if (!LocationUtil.isLocationEnabled(this)) {
                LocationUtil.enableLocation(this)
            } else {
                getLocationAndSendItToServer(this, editProfileViewModel)
                openMainActivity()

            }
        }

    }

    private fun openMainActivity() {
        NavigateUtil.start<MainActivity>(this)
        finish()

    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        } else { // Permission has already been granted
            if (!LocationUtil.isLocationEnabled(this)) {
                if (loadOnce) {
                    loadOnce = false
                    LocationUtil.enableLocation(this)
                }
            } else {
                getLocationAndSendItToServer(this, editProfileViewModel)
                openMainActivity()

            }
        }

    }

    private fun setUpAllowLocation() {
        binding.locationTitleTextView.text = resources.getText(R.string.enable_location)
        binding.locationMessageTextView.text = resources.getText(R.string.you_ll_need_to_enable_your_location_in_order_to_use_sugarbook)
        binding.locationButton.text = resources.getText(R.string.allow_location)
        binding.locationButton.setOnClickListener {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST)

        }
    }

    private fun setUpOpenSettings() {
        binding.locationTitleTextView.text = resources.getText(R.string.oops)
        binding.locationMessageTextView.text = resources.getText(R.string.in_order_to_use_sugarbook_please_enable_your_location_n_n_go_to_app_settings_permissions)
        binding.locationButton.text = resources.getText(R.string.open_settings)
        binding.locationButton.setOnClickListener {
            LocationUtil.openAppSettings(this)
        }

    }

    private fun setUpEnableLocation() {
        binding.locationTitleTextView.text = resources.getText(R.string.oops)
        binding.locationMessageTextView.text = resources.getText(R.string.you_need_to_allow_access_to_location_in_order_to_use_sugarbook_nplease_try_again_and_hit_ok)
        binding.locationButton.text = resources.getText(R.string.try_again)
        binding.locationButton.setOnClickListener {
            LocationUtil.enableLocation(this)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check for the integer request code originally supplied to startResolutionForResult().
        if (requestCode == 444) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    getLocationAndSendItToServer(this, editProfileViewModel)
                    openMainActivity()

                }
                Activity.RESULT_CANCELED -> {
                    setUpEnableLocation()
                    loadOnce = false
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_REQUEST) { // If request is cancelled, the result arrays are empty.
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!LocationUtil.isLocationEnabled(this)) {
                    LocationUtil.enableLocation(this)
                }
            }
            var i = 0
            val len = permissions.size
            while (i < len) {
                val permission = permissions[i]
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) { // user rejected the permission
                    var showRationale: Boolean
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission)
                        if (!showRationale) { // user also CHECKED "never ask again"

                            setUpOpenSettings()

                        }

                    }
                }
                i++
            }
        }
    }

}
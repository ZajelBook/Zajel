package com.bernovia.zajel.helpers


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bernovia.zajel.MainActivity
import com.bernovia.zajel.editProfile.models.EditProfileRequestBody
import com.bernovia.zajel.editProfile.ui.EditProfileViewModel
import com.bernovia.zajel.helpers.ZajelUtil.isLoggedIn
import com.bernovia.zajel.helpers.ZajelUtil.preferenceManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


object LocationUtil {
    const val LOCATION_REQUEST = 111

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun openMainActivity(activity: Activity) {
        val i = Intent(activity.applicationContext, MainActivity::class.java)
        if (activity.intent.extras != null) {
            i.putExtra("type", activity.intent.extras!!.getString("type"))
            i.putExtra("conversation_id", activity.intent.extras!!.getString("conversation_id"))
        }
        activity.startActivity(i)
        activity.finish()

    }


    @SuppressLint("MissingPermission")
    fun getLocationAndSendItToServer(
        activity: Activity,
        editProfileViewModel: EditProfileViewModel,
        fcmToken: String?,
        lifecycleOwner: LifecycleOwner
    ) {

        val fusedLocationClient: FusedLocationProviderClient? =
            LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null) {

                preferenceManager.latitude = location.latitude.toFloat()
                preferenceManager.longitude = location.longitude.toFloat()

                if (isLoggedIn()){
                    editProfileViewModel.getDataFromRetrofit(
                        EditProfileRequestBody(
                            location.latitude,
                            location.longitude,
                            fcmToken
                        )
                    ).observe(lifecycleOwner, Observer {
                        openMainActivity(activity)
                    })
                }else{
                    openMainActivity(activity)

                }

            } else {
                val locationRequest: LocationRequest? = LocationRequest.create()
                if (locationRequest != null) {
                    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    locationRequest.interval = 20 * 1000.toLong()
                    val locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            for (location in locationResult.locations) {
                                if (location != null) {
                                    preferenceManager.latitude = location.latitude.toFloat()
                                    preferenceManager.longitude = location.longitude.toFloat()

                                    if (isLoggedIn()){
                                        editProfileViewModel.getDataFromRetrofit(
                                            EditProfileRequestBody(
                                                location.latitude,
                                                location.longitude,
                                                fcmToken
                                            )
                                        ).observe(lifecycleOwner, Observer {
                                            openMainActivity(activity)
                                        })
                                    }else{
                                        openMainActivity(activity)

                                    }
                                }
                            }
                        }
                    }
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        null
                    )
                }
            }

        }

    }


    fun openAppSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivityForResult(intent, 1111)
    }

    fun enableLocation(activity: Activity) {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000 / 2.toLong()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val result =
            LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build())
        result.addOnCompleteListener { task: Task<LocationSettingsResponse?> ->
            try {
                val response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.

            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->  // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try { // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(activity, 444)
                        } catch (e: IntentSender.SendIntentException) { // Ignore the error.
                        } catch (e: ClassCastException) { // Ignore, should be an impossible error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        activity.startActivity(myIntent)
                    }
                }
            }
        }
    }


}
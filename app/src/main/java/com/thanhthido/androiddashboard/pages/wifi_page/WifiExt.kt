package com.thanhthido.androiddashboard.pages.wifi_page

import android.content.IntentSender
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

fun WifiFragment.isLocationEnable(locationManager: LocationManager): Boolean {
    return LocationManagerCompat.isLocationEnabled(locationManager)
}

fun WifiFragment.showErrorContainer() {
    binding.containerErrorLocation.visibility = View.VISIBLE
}

fun WifiFragment.hideErrorContainer() {
    binding.containerErrorLocation.visibility = View.GONE
}

fun WifiFragment.enableLocation(
    wifiManager: WifiManager,
    locationRequest: LocationRequest,
    checkLocationResult: ActivityResultLauncher<IntentSenderRequest>,
    locationManager: LocationManager
) {

    val settingsBuilder = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)

    settingsBuilder.setAlwaysShow(true)

    LocationServices.getSettingsClient(requireActivity())
        .checkLocationSettings(settingsBuilder.build())
        .addOnSuccessListener {
            if (isLocationEnable(locationManager)) {
                hideErrorContainer()
            }
        }
        .addOnFailureListener { exception ->
            try {
                val apiException = exception as ApiException
                when (apiException.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvableApiException = exception as ResolvableApiException
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(resolvableApiException.resolution).build()

                        checkLocationResult.launch(intentSenderRequest)
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Unit
                }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }

}
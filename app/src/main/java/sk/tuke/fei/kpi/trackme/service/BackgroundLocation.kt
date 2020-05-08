package sk.tuke.fei.kpi.trackme.service

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.callbackFlow
import sk.tuke.fei.kpi.trackme.utility.Constants
import sk.tuke.fei.kpi.trackme.view.TrackingActivity


class BackgroundLocation(private val activity: Activity) {

    val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

    fun createLocationRequest(): LocationRequest? {
        val locationRequest = LocationRequest.create()?.apply {
            interval = Constants.BACKGROUND_LOCATION_REQUEST_TIMEOUT
            fastestInterval = Constants.BACKGROUND_LOCATION_REQUEST_TIMEOUT
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)

        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse -> }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                try {
                    exception.startResolutionForResult(activity, 0)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }

        return locationRequest
    }

    fun startLocationUpdatesOnCallback(locationCallback: LocationCallback) {

        client.requestLocationUpdates(createLocationRequest(),
            locationCallback,
            Looper.getMainLooper())
    }

    fun stopLocationUpdatesCallback(locationCallback: LocationCallback) {
        client.removeLocationUpdates(locationCallback)
    }

}
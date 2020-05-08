package sk.tuke.fei.kpi.trackme.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import sk.tuke.fei.kpi.trackme.utility.Constants

class BackgroundLocationService: IntentService(Constants.BACKGROUND_LOCATION_INTENT_TAG) {

    private val TAG = this.javaClass.name
    private lateinit var backgroundLocation: BackgroundLocation
    private lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service createsd")
//        backgroundLocation = BackgroundLocation()
    }

    override fun onHandleIntent(intent: Intent?) {
        val dataString = intent!!.dataString
    }


}

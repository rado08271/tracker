package sk.tuke.fei.kpi.trackme.utility

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object Utils {

    fun activitiesToGson(anyObject: List<ActivityTransitionEvent>): String {
        val type = object: TypeToken<List<ActivityTransitionEvent>>() {}.type
        return Gson().toJson(anyObject, type).toString()
    }

    fun gsonToActivities(gson: String): List<ActivityTransitionEvent> {

        if (gson.isNullOrEmpty()) return emptyList()

        val type = object: TypeToken<List<ActivityTransitionEvent>>() {}.type

        val gsonData: List<ActivityTransitionEvent> = Gson().fromJson(gson, type)

        if (gsonData.isNullOrEmpty()) return emptyList()

        return gsonData
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}
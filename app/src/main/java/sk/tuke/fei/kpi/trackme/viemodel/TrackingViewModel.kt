package sk.tuke.fei.kpi.trackme.viemodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activiy_tracking.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sk.tuke.fei.kpi.trackme.R
import sk.tuke.fei.kpi.trackme.data.Route
import sk.tuke.fei.kpi.trackme.data.TrackingData
import sk.tuke.fei.kpi.trackme.service.remote.FirebaseService
import sk.tuke.fei.kpi.trackme.utility.CacheManager
import sk.tuke.fei.kpi.trackme.utility.Constants
import sk.tuke.fei.kpi.trackme.utility.Utils
import java.util.*
import kotlin.collections.ArrayList

class TrackingViewModel : ViewModel() {
    var liveData = MutableLiveData<List<ActivityTransitionEvent>>()
    var liveDataInt = MutableLiveData<Int>()
    var liveDataTrackingData = MutableLiveData<TrackingData>()
    var dataRoute: Route? = null

    fun getInt(): LiveData<Int> {
        liveDataInt.value = 0

        GlobalScope.launch {
            while (true) {
                delay(1000)
                liveDataInt.postValue(liveDataInt.value!! + 1)
            }
        }

        return liveDataInt
    }

    fun pushData(data: TrackingData) {
        FirebaseService.addDataToDatabase(data)
    }

    fun pushRoute() {
        if (dataRoute == null) return

        FirebaseService.addRouteToDatabase(dataRoute!!)
        dataRoute = null
    }

    fun addToRoute(activity: Activity, data: TrackingData) {
        if (data.state.equals(activity.resources.getString(R.string.string_state_unknown))) return

        if (dataRoute == null)
            dataRoute = Route(userId = CacheManager.getValue(
                activity.applicationContext, Constants.CACHE_USER_ID, "-1"),
                timestamp = Calendar.getInstance().timeInMillis,
                trackingRoute = ArrayList<TrackingData>())

        if (dataRoute!!.trackingRoute.contains(data)) return
        dataRoute!!.trackingRoute.add(data)
    }

    fun getTrackingData(activity: Activity, locationResult: LocationResult): LiveData<TrackingData> {
        for (location in locationResult.locations) {
            liveDataTrackingData.postValue(
                TrackingData(
                    userId = CacheManager.getValue(activity.applicationContext, Constants.CACHE_USER_ID, "-1"),
                    timestamp = Calendar.getInstance().timeInMillis,
                    latitude = location.latitude.toFloat(),
                    longitude = location.longitude.toFloat(),
                    altitude = location.altitude.toFloat(),
                    state = CacheManager.getValue(activity.applicationContext, Constants.CACHE_CURRENT_STATE, activity.resources.getString(R.string.string_state_unknown))
                )
            )
        }

        return liveDataTrackingData
    }

    fun getActivities(activity: Activity): LiveData<List<ActivityTransitionEvent>> {
        liveData.value = Utils.gsonToActivities(
            CacheManager.getValue(
                activity.applicationContext,
                Constants.CACHE_CHANGE_ACTIVITY,
                ""
            )
        )

        GlobalScope.launch {
            while (true) {
                delay(1000)
                val changed = CacheManager.getValue(
                    activity.applicationContext,
                    Constants.CACHE_NEW_ACTIVITY,
                    false
                )
                if (changed) {
                    CacheManager.addToCache(
                        activity.applicationContext,
                        Constants.CACHE_NEW_ACTIVITY,
                        false
                    )
                    liveData.postValue(
                        Utils.gsonToActivities(
                            CacheManager.getValue(
                                activity.applicationContext,
                                Constants.CACHE_CHANGE_ACTIVITY,
                                ""
                            )
                        )
                    )
                }
            }
        }

        return liveData
    }

}
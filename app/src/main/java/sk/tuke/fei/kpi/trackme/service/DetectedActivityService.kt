package sk.tuke.fei.kpi.trackme.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityTransitionResult
import sk.tuke.fei.kpi.trackme.utility.CacheManager
import sk.tuke.fei.kpi.trackme.utility.Constants
import sk.tuke.fei.kpi.trackme.utility.Utils
import java.util.*


class DetectedActivityService: IntentService(Constants.DETECT_SERVICE_INTENT_TAG) {

    override fun onHandleIntent(intent: Intent?) {
        val activityTransitionResult = ActivityTransitionResult.extractResult(intent)
        val transitionResult = activityTransitionResult!!.transitionEvents

        CacheManager.addToCache(this.application, Constants.CACHE_CHANGE_ACTIVITY, Utils.activitiesToGson(transitionResult))
        CacheManager.addToCache(this.application, Constants.CACHE_NEW_ACTIVITY, true)
        CacheManager.addToCache(this.application, Constants.CACHE_CHANGED_STATE_TIMESTAMP, Calendar.getInstance().timeInMillis)

        for (i in transitionResult) {
            Log.d(Constants.DETECT_SERVICE_INTENT_TAG, i.activityType.toString() + "  |   " + i.transitionType.toString() + "   |   " + i.elapsedRealTimeNanos.toString())
        }
    }

}
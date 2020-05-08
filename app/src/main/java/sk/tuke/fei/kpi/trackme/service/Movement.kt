package sk.tuke.fei.kpi.trackme.service

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import kotlinx.coroutines.*
import sk.tuke.fei.kpi.trackme.utility.CacheManager
import sk.tuke.fei.kpi.trackme.utility.Constants


class Movement(private val activity: Activity) {

    private var activityClient = ActivityRecognitionClient(activity)
    private var changed = false

    fun requestUpdates(): Boolean {
        val transitionRequest = buildTransitionRequest()
        val pendingIntent = getPendingIntent()
        val task = activityClient.requestActivityTransitionUpdates(transitionRequest, pendingIntent)

        var success = false
        task.addOnSuccessListener {
            CacheManager.addToCache(activity.applicationContext, Constants.CACHE_STARTED_MOVEMENT, true)
            success = true;
        }

        return success
    }

    fun stopRequestingUpdates(): Boolean {
        val task = activityClient.removeActivityTransitionUpdates(getPendingIntent())

        var success = false
        task.addOnSuccessListener {
            CacheManager.addToCache(activity.applicationContext, Constants.CACHE_STARTED_MOVEMENT, false)
            success = true;
        }

        return success
    }

    fun getActivities(onReceiveListener: OnReceiveActivityListener) {
        while (true) {
            onReceiveListener.onReceive(
                changed,
                CacheManager.getValue(
                    activity.applicationContext,
                    Constants.CACHE_CHANGE_ACTIVITY,
                    "UNKNOWN"
                )
            )

            changed = false
        }
    }


    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(activity.applicationContext, DetectedActivityService::class.java)
        return PendingIntent.getService(
            activity.applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    private fun buildTransitionRequest(): ActivityTransitionRequest {
        val transitions: MutableList<ActivityTransition> = ArrayList()
        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        )
        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
        )
        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_FOOT)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        )
        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_FOOT)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
        )
        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        )
        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
        )
        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        )
        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
        )
        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        )
        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
        )
        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        )
        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
        )

        return ActivityTransitionRequest(transitions)
    }

    interface OnReceiveActivityListener {
        fun onReceive(new: Boolean, activities: String)
    }
}
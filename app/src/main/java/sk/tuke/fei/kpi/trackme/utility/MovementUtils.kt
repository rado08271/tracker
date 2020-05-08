package sk.tuke.fei.kpi.trackme.utility

import android.content.Context
import com.google.android.gms.location.DetectedActivity
import sk.tuke.fei.kpi.trackme.R

object MovementUtils {
    fun getActivityString(detectedActivityType: Int, context: Context): String {
        return when (detectedActivityType) {
            DetectedActivity.IN_VEHICLE -> return context.getString(R.string.string_state_in_vehicle)
            DetectedActivity.ON_BICYCLE -> return context.getString(R.string.string_state_on_bicycle)
            DetectedActivity.ON_FOOT -> return context.getString(R.string.string_state_on_foot)
            DetectedActivity.RUNNING -> return context.getString(R.string.string_state_running)
            DetectedActivity.STILL -> return context.getString(R.string.string_state_still)
            DetectedActivity.TILTING -> return context.getString(R.string.string_state_tilting)
            DetectedActivity.UNKNOWN -> return context.getString(R.string.string_state_unknown)
            DetectedActivity.WALKING -> return context.getString(R.string.string_state_walking)
            else -> return context.getString(R.string.string_state_unknown)
        }
    }


}
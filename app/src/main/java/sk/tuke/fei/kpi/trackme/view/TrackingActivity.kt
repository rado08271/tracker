package sk.tuke.fei.kpi.trackme.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.gsm.GsmCellLocation
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activiy_tracking.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sk.tuke.fei.kpi.trackme.R
import sk.tuke.fei.kpi.trackme.service.BackgroundLocation
import sk.tuke.fei.kpi.trackme.service.BackgroundLocationService
import sk.tuke.fei.kpi.trackme.service.Movement
import sk.tuke.fei.kpi.trackme.utility.CacheManager
import sk.tuke.fei.kpi.trackme.utility.Constants
import sk.tuke.fei.kpi.trackme.utility.MovementUtils
import sk.tuke.fei.kpi.trackme.viemodel.TrackingViewModel
import java.util.*

class TrackingActivity : AppCompatActivity() {

    private lateinit var movement: Movement
    private lateinit var viewModel: TrackingViewModel
    private lateinit var locationService: Intent

    private lateinit var locationCallback: LocationCallback
    private lateinit var backgroundLocation: BackgroundLocation

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiy_tracking)

        locationService = Intent(this.application, BackgroundLocationService::class.java)
        startForegroundService(intent);

        viewModel = ViewModelProvider(this)[TrackingViewModel::class.java]
        backgroundLocation = BackgroundLocation(this)

        movement = Movement(this)

        check()

        id_start_background_button.setOnClickListener {
            movement.requestUpdates()
            initLocationTracking()
            id_start_background_button.isEnabled = false
            id_stop_background_button.isEnabled = true
        }

        id_stop_background_button.setOnClickListener {
            movement.stopRequestingUpdates()
            backgroundLocation.stopLocationUpdatesCallback(locationCallback)
            id_stop_background_button.isEnabled = false
            id_start_background_button.isEnabled = true
        }

        viewModel.getInt().observe(this, Observer<Int> {
            id_current_time.text = it.toString()
        })

        viewModel.getActivities(this).observe(this, Observer<List<ActivityTransitionEvent>> {
            changedBehaviour(it)
        })
    }

    fun check() {
        if (CacheManager.getValue(applicationContext, Constants.CACHE_STARTED_MOVEMENT, false)) {
            movement.requestUpdates()
            initLocationTracking()
            id_start_background_button.isEnabled = false
            id_stop_background_button.isEnabled = true
        }
    }

    fun getValues() {
        if (CacheManager.getValue(applicationContext, Constants.CACHE_STARTED_MOVEMENT, false)) {
            id_current_longitude.text = getString(R.string.string_waiting_for_data)
            id_current_latitude.text = getString(R.string.string_waiting_for_data)
            id_currently_working.text = getString(R.string.string_waiting_for_data)
        } else {

        }
    }

    fun changedBehaviour(activities: List<ActivityTransitionEvent>) {
        for (i in activities) {
            if (i.transitionType == 1) {
                CacheManager.addToCache(applicationContext, Constants.CACHE_CURRENT_STATE, MovementUtils.getActivityString(i.activityType, applicationContext))
                id_currently_working.text = MovementUtils.getActivityString(i.activityType, applicationContext)
            }
        }
    }

    fun initLocationTracking() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                viewModel.getTrackingData(this@TrackingActivity, locationResult!!)
                    .observe(this@TrackingActivity, Observer {
                        id_current_longitude.text = it.longitude.toString()
                        id_current_latitude.text = it.latitude.toString()
                        viewModel.addToRoute(this@TrackingActivity, it)
                    })
            }
        }
        backgroundLocation.startLocationUpdatesOnCallback(locationCallback)

        getValues()
        dataSender()
    }

    fun dataSender() {
        GlobalScope.launch {

            val state = CacheManager.getValue(applicationContext, Constants.CACHE_CURRENT_STATE, resources.getString(R.string.string_state_unknown))
            val timeChange = Calendar.getInstance().timeInMillis - CacheManager.getValue(applicationContext, Constants.CACHE_CHANGED_STATE_TIMESTAMP, -1L)
            if (!state.equals(resources.getString(R.string.string_state_unknown))) {
                // TODO person is moving...
                if (!(state.equals(resources.getString(R.string.string_state_still))
                            && (timeChange > Constants.INACTIVE_MAX_INTERVAL))) {
                    viewModel.pushRoute()
                } else {
                    id_currently_working.text = getString(R.string.string_data_not_collected_currently)
                }
            }
            delay(Constants.SEND_DATA_INTERVAL)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        check()
    }

}

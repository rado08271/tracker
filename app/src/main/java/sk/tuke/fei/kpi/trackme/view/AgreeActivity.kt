package sk.tuke.fei.kpi.trackme.view

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_agree.*
import sk.tuke.fei.kpi.trackme.R
import sk.tuke.fei.kpi.trackme.utility.CacheManager
import sk.tuke.fei.kpi.trackme.utility.Constants
import sk.tuke.fei.kpi.trackme.utility.Utils

class AgreeActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agree)

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {}
                override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {}
            })
            .check()

        checkSettings()

        id_agree_battery_consent.setOnClickListener {
            checkConsent()
        }

        id_agree_data_consent.setOnClickListener {
            checkConsent()
        }

        id_agree_permissions_consent.setOnClickListener {
            checkConsent()
        }

        id_agree_next.setOnClickListener {
            nextActivity()
        }


    }

    fun checkConsent() {
        if (id_agree_battery_consent.isChecked && id_agree_data_consent.isChecked && id_agree_permissions_consent.isChecked) {
            id_agree_next.isEnabled = true
        }
    }

    fun nextActivity() {
        CacheManager.addToCache(applicationContext, Constants.CACHE_CONSENT_GIVEN, true)
        startActivity(Intent(applicationContext, TrackingActivity::class.java))
    }

    fun checkSettings() {
        if (!Utils.isLocationEnabled(applicationContext)) {
            startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0)
        }

    }
}

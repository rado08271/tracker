package sk.tuke.fei.kpi.trackme.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sk.tuke.fei.kpi.trackme.R
import sk.tuke.fei.kpi.trackme.utility.CacheManager
import sk.tuke.fei.kpi.trackme.utility.Constants
import sk.tuke.fei.kpi.trackme.utility.Utils
import java.util.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (!CacheManager.getValue(applicationContext, Constants.CACHE_USER_EXISTS, false)) {
            createUser()
        }

        if (CacheManager.getValue(applicationContext, Constants.CACHE_CONSENT_GIVEN, false) && Utils.isLocationEnabled(applicationContext)) {
            trackingActivity()
        } else {
            agreeActivity()
        }
    }

    fun createUser() {
        CacheManager.addToCache(applicationContext, Constants.CACHE_USER_EXISTS, true)
        CacheManager.addToCache(applicationContext, Constants.CACHE_USER_ID, UUID.randomUUID().toString())
        CacheManager.addToCache(applicationContext, Constants.CACHE_CURRENT_TIMER, 0L)
    }

    fun trackingActivity() {
        startActivity(Intent(applicationContext, TrackingActivity::class.java))
    }

    fun agreeActivity() {
        startActivity(Intent(applicationContext, AgreeActivity::class.java))
    }

}

package io.github.bradpatras.pacemonitor

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import io.github.bradpatras.pacemonitor.events.PermissionEvents
import io.github.bradpatras.pacemonitor.util.PermissionHelper
import org.greenrobot.eventbus.EventBus

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
    }

    override fun onResume() {
        super.onResume()
        PermissionHelper.getInstance(this).checkLocationPermissions {
            EventBus.getDefault().postSticky(PermissionEvents.LocationPermissionGrantedEvent)
        }
    }
}

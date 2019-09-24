package io.github.bradpatras.pacemonitor.services.speed

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.SystemClock
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.github.bradpatras.pacemonitor.events.SpeedReportEvents
import org.greenrobot.eventbus.EventBus

private const val REQUEST_INTERVAL: Long = 1000
private const val REQUEST_MAX_WAIT: Long = 1000
private const val REQUEST_FASTEST_INTERVAL: Long = 250

class SpeedReportService : Service() {
    private var isRunning: Boolean = false
    private var speeds: MutableList<Pair<Long, Float>> = mutableListOf()

    private val fusedLocation by lazy {
        LocationServices.getFusedLocationProviderClient(this@SpeedReportService)
    }

    private val locationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                onLocationChanged(locationResult?.lastLocation)
            }
        }
    }

    private fun onLocationChanged(location: Location?) {
        location?.let { loc ->
            if (loc.hasSpeed()) {
                updateSpeeds(loc)
            }
        }
    }

    private fun updateSpeeds(location: Location) {
        val locTimeMillis = location.elapsedRealtimeNanos / 1000000
        val locSpeed = location.speed
        val nowTimeMillis = SystemClock.elapsedRealtimeNanos() / 1000000

        // expiration is 60 seconds in the past
        val expirationTimeMillis = nowTimeMillis - (60 * 1000)

        // add new speed report
        speeds.add(Pair(locTimeMillis, locSpeed))

        // trim down the speed reports to only include reports within the last 60 seconds
        val newStartIndex = speeds.indexOfFirst { it.first > expirationTimeMillis }
        speeds = speeds.subList(newStartIndex, speeds.count())

        // compute averages
        var sixtySum = 0f
        var thirtySum = 0f
        var fiveSum = 0f

        val fiveTimeCutoff = nowTimeMillis - (5 * 1000)
        val thirtyTimeCutoff = nowTimeMillis - (30 * 1000)

        var fiveCount = 0
        var thirtyCount = 0

        speeds.forEach { speed ->
            sixtySum += speed.second

            if (speed.first > fiveTimeCutoff) {
                fiveSum += speed.second
                fiveCount += 1
            }

            if (speed.first > thirtyTimeCutoff) {
                thirtySum += speed.second
                thirtyCount += 1
            }
        }

        if (speeds.count() > 30) {
            postSixtyAvg(sixtySum / speeds.count().toFloat())
        }
        if (thirtyCount > 15) {
            postThirtyAvg(thirtySum / thirtyCount.toFloat())
        }
        if (fiveCount > 3) {
            postFiveAvg(fiveSum / fiveCount.toFloat())
        }
    }

    private fun postSixtyAvg(avg: Float) {
        EventBus.getDefault().post(SpeedReportEvents.SixtySecondReport(avg))
    }

    private fun postThirtyAvg(avg: Float) {
        EventBus.getDefault().post(SpeedReportEvents.ThirtySecondReport(avg))
    }

    private fun postFiveAvg(avg: Float) {
        EventBus.getDefault().post(SpeedReportEvents.FiveSecondReport(avg))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRunning) return super.onStartCommand(intent, flags, startId)
        isRunning = true

        startLocationRequests()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startLocationRequests() {
        val locationRequest = LocationRequest()
            .setInterval(REQUEST_INTERVAL)
            .setMaxWaitTime(REQUEST_MAX_WAIT)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setFastestInterval(REQUEST_FASTEST_INTERVAL)

        fusedLocation.requestLocationUpdates(locationRequest,
            locationCallback,
            mainLooper)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocation.removeLocationUpdates(locationCallback)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}
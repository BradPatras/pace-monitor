package io.github.bradpatras.pacemonitor.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.Dexter
import io.github.bradpatras.pacemonitor.BuildConfig
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.github.bradpatras.pacemonitor.R

class PermissionHelper private constructor(val activity: Activity) {

    fun checkLocationPermissions(onAllowed: () -> Unit) {
        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(
                Listener(
                    onAllowed,
                    onDenied = {
                        permissionDeniedAlert(activity) { checkLocationPermissions(onAllowed) }
                            .show()
                    },
                    onDeniedForever = {
                        permissionDeniedForeverAlert(activity) { openApplicationSettings() }
                            .show()
                    }
                )
            ).check()
    }

    private fun openApplicationSettings() {
        activity.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)))
    }

    private fun permissionDeniedAlert(context: Context, action: () -> Unit): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(R.string.denied_alert_title)
            .setMessage(context.getString(R.string.denied_alert_message))
            .setPositiveButton(context.getString(R.string.denied_alert_button)) { _, _ -> action.invoke() }
            .setCancelable(true)
            .create()
    }

    private fun permissionDeniedForeverAlert(context: Context, action: () -> Unit): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(R.string.denied_alert_title)
            .setMessage(context.getString(R.string.denied_forever_alert_message))
            .setPositiveButton(context.getString(R.string.denied_alert_button)) { _, _ -> action.invoke() }
            .setCancelable(true)
            .create()
    }

    companion object : SingletonArgHolder<PermissionHelper, Activity>(::PermissionHelper)

    private class Listener(
        val onAllowed: () -> Unit,
        val onDenied: () -> Unit,
        val onDeniedForever: () ->  Unit
    ) : MultiplePermissionsListener {
        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
            report?.let {
                when {
                    it.areAllPermissionsGranted() -> onAllowed.invoke()
                    it.isAnyPermissionPermanentlyDenied -> onDeniedForever.invoke()
                    else -> onDenied.invoke()
                }
            }
        }

        override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
            token?.continuePermissionRequest()
        }
    }
}
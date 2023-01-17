package com.example.echo.path

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat

class PathService : Service() {
    val manager = getSystemService(LOCATION_SERVICE) as LocationManager


    @SuppressLint("MissingPermission")
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")



        var result = "All Providers : "
        val provider = manager.allProviders
        for (provider in provider) {
            result += "$provider,"
        }
        Log.d("제발", result)

        val listener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
              Log.d("위치", "${location.latitude}, ${location.longitude}, ${location.accuracy}")
            }

            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
            }

            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
            }

        }

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10_000L, 10f, listener)
        manager.removeUpdates(listener)
    }
}
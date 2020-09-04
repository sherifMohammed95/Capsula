package com.freelance.capsoula.locationService

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.freelance.capsoula.data.repository.DeliveryRepository
import com.freelance.capsoula.data.requests.AddAddressRequest
import com.freelance.capsoula.utils.Domain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class LocationService : Service() {

    private val TAG = "LOCATION_SERVICE"
    private var mLocationManager: LocationManager? = null
    private val LOCATION_INTERVAL = 1000
    private val LOCATION_DISTANCE = 10f


    companion object {
        const val TAG = "LocationWorker"
        private val repo = DeliveryRepository()

        suspend fun submitLocation(location: Location) {
            val request = AddAddressRequest()
            request.latitude = location.latitude
            request.longitude = location.longitude
            request.addressDesc = getCompleteAddressString(location.latitude, location.longitude)

            withContext(Dispatchers.IO) {
                repo.updateDeliveryCurrentLocation(request)
            }
        }

        private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String {
            var strAdd = ""
            val geocoder = Geocoder(Domain.application, Locale.getDefault())
            try {
                val addresses: List<Address>? =
                    geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
                if (addresses != null) {
                    val returnedAddress: Address = addresses[0]
                    val strReturnedAddress = StringBuilder()
                    for (i in 0..returnedAddress.maxAddressLineIndex) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i))
                    }
                    strAdd = strReturnedAddress.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return strAdd
        }
    }

    class LocationListener(provider: String) :
        android.location.LocationListener {
        @SuppressLint("LogNotTimber")
        override fun onLocationChanged(location: Location) {
            Log.e(TAG, "onLocationChanged: $location")
            CoroutineScope(Dispatchers.IO).launch {
                submitLocation(location)
            }
        }

        override fun onProviderDisabled(provider: String) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    private var mLocationListeners = arrayOf(
        LocationListener(LocationManager.GPS_PROVIDER),
        LocationListener(LocationManager.NETWORK_PROVIDER)
    )


    private fun initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager =
                Domain.application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    @SuppressLint("LogNotTimber")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        initializeLocationManager()


        try {
            mLocationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                LOCATION_INTERVAL.toLong(), LOCATION_DISTANCE, mLocationListeners[0],
                Looper.getMainLooper()
            )
        } catch (ex: SecurityException) {
            Log.e(TAG, "fail to request location update, ignore", ex)
        } catch (ex: java.lang.IllegalArgumentException) {
            Log.e(TAG, "gps provider does not exist " + ex.message)
        }

        try {
            mLocationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                LOCATION_INTERVAL.toLong(),
                LOCATION_DISTANCE,
                mLocationListeners[1],
                Looper.getMainLooper()
            )
        } catch (ex: SecurityException) {
            Log.e(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.e(TAG, "network provider does not exist, " + ex.message)
        }

        return START_STICKY
    }

    @SuppressLint("LogNotTimber")
    override fun onDestroy() {
        super.onDestroy()
        super.onDestroy()
        if (mLocationManager != null) {
            for (i in mLocationListeners.indices) {
                try {
                    mLocationManager!!.removeUpdates(mLocationListeners[i])
                } catch (ex: java.lang.Exception) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex)
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
package com.freelance.capsoula.ui.addAddress

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.data.UserAddress
import com.freelance.capsoula.data.repository.UserRepository
import com.freelance.capsoula.databinding.ActivityAddAddressBinding
import com.freelance.capsoula.ui.home.HomeActivity
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.GpsUtils
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.tbruyelle.rxpermissions2.RxPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module
import rx.functions.Action1
import java.util.*

val addAddressModule = module {
    factory { UserRepository() }
}

class AddAddressActivity : BaseActivity<ActivityAddAddressBinding, AddAddressViewModel>(),
    AddAddressNavigator, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var mGoogleMap: GoogleMap? = null

    private val mViewModel: AddAddressViewModel by viewModel()
    private var mLocation: Location? = null
    private var locationMarker: Marker? = null
    private var addressFragments = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMapView()
        getIntentsData()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mViewModel.addAddressResponse.observe(this, androidx.lifecycle.Observer {
            if (mViewModel.fromWhere == Constants.FROM_CHECKOUT_DETAILS)
                finish()
            else
                openHome()
        })
    }

    private fun getIntentsData() {
        mViewModel.fromWhere = intent.getIntExtra(Constants.FROM_WHERE, -1)
    }

    override fun openHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun backWithResult() {
        val returnIntent = Intent()
        val address = UserAddress()
        address.latitude = mViewModel.mLat
        address.longitude = mViewModel.mLng
        address.addressDesc = mViewModel.currentLocationText.get()!!
        returnIntent.putExtra(Constants.EXTRA_ADD_NEW_ADDRESS, Gson().toJson(address))
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun getMyViewModel(): AddAddressViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_address
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
        viewDataBinding?.backImageView!!.setOnClickListener {
            finish()
        }
    }


    private fun initMapView() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapClick(latLng: LatLng?) {
        setLocationMarker(latLng!!)
    }

    private fun setLocationMarker(latLng: LatLng) {
        mGoogleMap!!.clear()
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            mViewModel.mLat = latLng.latitude
            mViewModel.mLng = latLng.latitude
            addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )
        } catch (e: Exception) {
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.isEmpty()) {
            return
        } else {
            val address = addresses[0]
            addressFragments = ""


            if (address.maxAddressLineIndex > 0) {
                for (i in 0 until address.maxAddressLineIndex) {
                    addressFragments += address.getAddressLine(i)

                    if (i != address.maxAddressLineIndex - 1) addressFragments += ", "
                }
            } else {
                try {
                    addressFragments += address.getAddressLine(0)
                } catch (ignored: Exception) {
                }
            }
            locationMarker = mGoogleMap!!.addMarker(
                MarkerOptions()
                    .position(latLng)
                    // .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin))
                    .title(addressFragments)
            )

            mViewModel.currentLocationText.set(addressFragments)
            locationMarker!!.showInfoWindow()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        getLocation()
        setLocationButton()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mGoogleMap!!.isMyLocationEnabled = true
            mGoogleMap!!.uiSettings.isMapToolbarEnabled = false
        }

        mGoogleMap!!.setOnMapClickListener(this)
    }

    private fun setLocationButton() {
        val locationButton = (this.findViewById<View>(Integer.parseInt("1"))?.parent
                as View).findViewById<View>(Integer.parseInt("2")) as ImageView
        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 500, 700)

//        locationButton.setImageResource(R.drawable.ic_navigate)
    }

    private fun initGoogleAPIClient() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = (10 * 1000).toLong() // 10 seconds
        locationRequest!!.fastestInterval = (5 * 1000).toLong() // 5 seconds
        GpsUtils.onGpsListener {
            if (!it)
                GpsUtils(this).turnGPSOn { getLocation() }
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        mLocation = location
                        mViewModel.mLat = location.latitude
                        mViewModel.mLng = location.longitude
                        setLocationMarker(LatLng(mViewModel.mLat, mViewModel.mLng))
                        zoomToMyLocation()
                        if (mFusedLocationClient != null) {
                            mFusedLocationClient!!.removeLocationUpdates(locationCallback)
                        }
                    }
                }
            }
        }
    }

    private fun zoomToMyLocation() {
        if (mLocation != null) {
            val latLng = LatLng(mLocation!!.latitude, mLocation!!.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14f)
            mGoogleMap!!.animateCamera(cameraUpdate)
        }
    }

    @SuppressLint("CheckResult")
    fun requestRunTimePermissionForStorage(
        context: FragmentActivity,
        action: Action1<Boolean>
    ) {
        val rxPermissions = RxPermissions(context)
        rxPermissions.request(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).subscribe { action.call(it) }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        requestRunTimePermissionForStorage(this, Action1 {
            if (it) {
                initGoogleAPIClient()
                mFusedLocationClient!!.lastLocation.addOnSuccessListener {
                    if (it != null) {
                        mLocation = it
                        mViewModel.mLat = it.latitude
                        mViewModel.mLng = it.longitude
                        setLocationMarker(LatLng(mViewModel.mLat, mViewModel.mLng))
                        zoomToMyLocation()
                    } else {
                        mFusedLocationClient!!.requestLocationUpdates(
                            locationRequest,
                            locationCallback, null
                        )
                    }
                }
            }
        })
    }
}

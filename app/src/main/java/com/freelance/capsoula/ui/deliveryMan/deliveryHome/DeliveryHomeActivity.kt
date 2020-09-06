package com.freelance.capsoula.ui.deliveryMan.deliveryHome

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.DeliveryOrder
import com.freelance.capsoula.databinding.ActivityDeliveryHomeBinding
import com.freelance.capsoula.locationService.LocationServiceManager
import com.freelance.capsoula.ui.checkout.CheckoutActivity
import com.freelance.capsoula.ui.deliveryMan.deliveryHome.adapters.DeliveryOrdersAdapter
import com.freelance.capsoula.ui.deliveryMan.deliveryOrderDetails.DeliveryOrderDetailsActivity
import com.freelance.capsoula.ui.more.MoreActivity
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.Constants.GPS_CODE
import com.freelance.capsoula.utils.MyLocationManager
import com.freelance.capsoula.utils.Utils
import com.google.gson.Gson
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest
import kotlinx.android.synthetic.main.activity_delivery_home.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module


val deliveryHomeModule = module {
    factory { DeliveryOrdersAdapter() }
}

class DeliveryHomeActivity : BaseActivity<ActivityDeliveryHomeBinding, DeliveryHomeViewModel>(),
    DeliveryHomeNavigator, BaseRecyclerAdapter.OnITemClickListener<DeliveryOrder>,
    DeliveryOrdersAdapter.OnNavigateClickListener {

    private val mViewModel: DeliveryHomeViewModel by viewModel()
    private val mAdapter: DeliveryOrdersAdapter by inject()
    private val mLocationManager = MyLocationManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        subscribeToLiveData()
    }


    private fun subscribeToLiveData() {
        mViewModel.deliveryHomeDataResponse.observe(this, Observer {
            if (!it.ordersList.isNullOrEmpty()) {
                Constants.REFRESH_DELIVERY_ORDER = false
                mAdapter.setData(it.ordersList!!)
                getUserLocationAndScheduleService()
            }
        })
    }

    private fun initRecyclerView() {
        delivery_orders_recyclerView.adapter = mAdapter
        mAdapter.onITemClickListener = this
        mAdapter.onNavigateClickListener = this
    }

    override fun onResume() {
        super.onResume()
        if (Constants.REFRESH_DELIVERY_ORDER) {
            mViewModel.getHomeData()
        }
    }

    override fun getMyViewModel(): DeliveryHomeViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_delivery_home
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    override fun openMore() {
        startActivity(Intent(this, MoreActivity::class.java))
    }

    override fun onItemClick(pos: Int, item: DeliveryOrder) {
        val intent = Intent(this, DeliveryOrderDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_ORDER, Gson().toJson(item))
        intent.putExtra(Constants.FROM_HISTORY, false)
        startActivity(intent)
    }

    override fun onNavigateClick(item: DeliveryOrder) {
        Utils.navigateToLocation(this, item.customerLat.toString(), item.customerLong.toString())
    }

    private val quickPermissionsOptions = QuickPermissionsOptions(
        permanentDeniedMethod = { handleDenyPermissionsPermanently(it) },
        rationaleMethod = { handleDenyPermissions(it) }
    )

    private fun handleDenyPermissions(arg: QuickPermissionsRequest) {
        showPopUp(
            R.string.permission_denied,
            android.R.string.ok,
            DialogInterface.OnClickListener { _, _ ->
            }, false
        )
    }

    private fun handleDenyPermissionsPermanently(arg: QuickPermissionsRequest) {
        showPopUp(
            getString(R.string.permission_denied_permanently),
            android.R.string.ok,
            DialogInterface.OnClickListener { _, _ ->
                arg.openAppSettings()
            }, false
        )
    }

    private fun getUserLocationAndScheduleService() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            runWithPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                options = quickPermissionsOptions
            ) {
                getCurrentLocation()
            }
        } else {
            runWithPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                options = quickPermissionsOptions
            ) {
                getCurrentLocation()
            }
        }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (mLocationManager.isGPSEnabled(this)) {
            LocationServiceManager().startBackgroundService(this)
        } else {
            showPopUp(
                getString(R.string.should_enable_gps), android.R.string.ok,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    startActivityForResult(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                        GPS_CODE
                    )
                }, false
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GPS_CODE && resultCode == 0) {
            if (mLocationManager.isGPSEnabled(this))
                getCurrentLocation()
        } else {
            //Users did not switch on the GPS
        }
    }
}
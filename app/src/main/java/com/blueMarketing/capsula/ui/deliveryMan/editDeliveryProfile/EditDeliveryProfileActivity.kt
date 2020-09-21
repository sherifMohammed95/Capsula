package com.blueMarketing.capsula.ui.deliveryMan.editDeliveryProfile

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.databinding.ActivityEditDeliveryProfileBinding
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.carDetails.CarDetailsFragment
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.personalDetails.PersonalDetailsFragment
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.requiredDocuments.RequiredDocumentsFragment
import com.blueMarketing.capsula.utils.Constants.CAR_DETAILS_STEP
import com.blueMarketing.capsula.utils.Constants.PERSONAL_DETAILS_STEP
import com.blueMarketing.capsula.utils.Constants.REQUIRED_DOCS_STEP
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditDeliveryProfileActivity :
    BaseActivity<ActivityEditDeliveryProfileBinding, EditDeliveryProfileViewModel>(),
    EditDeliveryProfileNavigator {

    val mViewModel: EditDeliveryProfileViewModel by viewModel()

    private var mPersonalDetailsFragment: Fragment? = null
    private var mCarDetailsFragment: Fragment? = null
    private var mRequiredDocsFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openPersonalDetails()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mViewModel.updateDeliveryProfileResponse.observe(this, Observer {
            showPopUp(
                getString(R.string.profile_updated), android.R.string.ok,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    finish()
                }, false
            )
        })
    }

    override fun getMyViewModel(): EditDeliveryProfileViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_delivery_profile
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    override fun backAction() {
        finish()
    }

    override fun saveAction() {
        when (mViewModel.currentPage) {
            PERSONAL_DETAILS_STEP -> {
                (mPersonalDetailsFragment as PersonalDetailsFragment)
                    .mViewModel.validateForSaveRequest()
            }
            CAR_DETAILS_STEP -> {
                (mCarDetailsFragment as CarDetailsFragment)
                    .mViewModel.validateForSaveRequest()
            }
            REQUIRED_DOCS_STEP -> {
                (mRequiredDocsFragment as RequiredDocumentsFragment)
                    .mViewModel.validateForSaveRequest()
            }
        }
    }

    override fun openPersonalDetails() {
        mViewModel.currentPage = PERSONAL_DETAILS_STEP
        mPersonalDetailsFragment = PersonalDetailsFragment.newInstance(true)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.edit_delivery_container, mPersonalDetailsFragment!!)
            .commit()
    }

    override fun openCarDetails() {
        mViewModel.currentPage = CAR_DETAILS_STEP
        mCarDetailsFragment = CarDetailsFragment.newInstance(true)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.edit_delivery_container, mCarDetailsFragment!!)
            .addToBackStack(null)
            .commit()
    }

    override fun openRequiredDocuments() {
        mViewModel.currentPage = REQUIRED_DOCS_STEP
        mRequiredDocsFragment = RequiredDocumentsFragment.newInstance(true)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.edit_delivery_container, mRequiredDocsFragment!!)
            .addToBackStack(null)
            .commit()
    }
}
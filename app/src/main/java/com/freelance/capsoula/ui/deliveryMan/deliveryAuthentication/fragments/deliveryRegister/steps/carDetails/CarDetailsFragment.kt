package com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.carDetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.freelance.base.BaseFragment
import com.freelance.capsoula.R
import com.freelance.capsoula.custom.bottomSheet.BottomSelectionFragment
import com.freelance.capsoula.data.SpinnerModel
import com.freelance.capsoula.databinding.FragmentDeliveryCarDetailsBinding
import com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.DeliveryAuthenticationActivity
import com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.personalDetails.PersonalDetailsFragment
import com.freelance.capsoula.ui.deliveryMan.editDeliveryProfile.EditDeliveryProfileActivity
import com.freelance.capsoula.ui.deliveryMan.editDeliveryProfile.EditDeliveryProfileViewModel
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.ValidationUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import rx.functions.Action2

class CarDetailsFragment : BaseFragment<FragmentDeliveryCarDetailsBinding, CarDetailsViewModel>(),
    CarDetailsNavigator {

    val mViewModel: CarDetailsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundles()
        subscribeToLiveData()
    }

    private fun getBundles() {
        if (arguments != null) {
            mViewModel.isEditMode.set(arguments!!.getBoolean(Constants.IS_EDIT_MODE))

            if (mViewModel.isEditMode.get())
                mViewModel.fillViewFromUserObject()
        }
    }

    override fun getMyViewModel(): CarDetailsViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_delivery_car_details
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    private fun subscribeToLiveData() {
        mViewModel.saveApiRequestEvent.observe(viewLifecycleOwner, Observer {
            buildApiRequest()
            (activity as EditDeliveryProfileActivity).mViewModel.updateProfile()
        })

        mViewModel.deliveryRegisterBasicResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mViewModel.carBrands = it.carTypes!!
                SpinnerModel().initialize(mViewModel.carBrands)

                mViewModel.modelYears = it.years!!
                SpinnerModel().initialize(mViewModel.modelYears)

                mViewModel.licenseTypes = it.vehicleTypes!!
                SpinnerModel().initialize(mViewModel.licenseTypes)

                if (mViewModel.isEditMode.get())
                    mViewModel.setSelectedPos()
            }
        })

        mViewModel.carModelsResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mViewModel.carModels = it.list
                SpinnerModel().initialize(mViewModel.carModels)
                if (mViewModel.isEditMode.get())
                    mViewModel.setCarModelSelectedPos()
            }
        })
    }

    override fun showCarBrandsSheet() {
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.car_brand),
            mViewModel.carBrands,
            Action2 { pos, item ->
                mViewModel.selectedCarBrandPos = pos
                mViewModel.carBrandText.set(item.text)
                mViewModel.selectedCarModelPos = -1
                mViewModel.carModelText.set("")
                mViewModel.loadCarModels()
            },
            mViewModel.selectedCarBrandPos,
            showClearText = false,
            showIconImage = false,
            showAddNewAddressText = false
        )
        fragment.show(childFragmentManager, fragment.tag)
    }

    override fun showCarModelsSheet() {
        if (mViewModel.selectedCarBrandPos == -1) {
            showPopUp(
                "", getString(R.string.choose_car_brand_first),
                getString(android.R.string.ok), false
            )
            return
        }
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.car_model),
            mViewModel.carModels,
            Action2 { pos, item ->
                mViewModel.selectedCarModelPos = pos
                mViewModel.carModelText.set(item.text)
            },
            mViewModel.selectedCarModelPos,
            showClearText = false,
            showIconImage = false,
            showAddNewAddressText = false
        )
        fragment.show(childFragmentManager, fragment.tag)
    }

    override fun showModelYearsSheet() {
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.model_year),
            mViewModel.modelYears,
            Action2 { pos, item ->
                mViewModel.selectedModelYearPos = pos
                mViewModel.modelYearText.set(item.text)
            },
            mViewModel.selectedModelYearPos,
            showClearText = false,
            showIconImage = false,
            showAddNewAddressText = false
        )
        fragment.show(childFragmentManager, fragment.tag)
    }

    override fun showLicenseTypesSheet() {
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.license_type),
            mViewModel.licenseTypes,
            Action2 { pos, item ->
                mViewModel.selectedLicenseTypePos = pos
                mViewModel.licenseTypeText.set(item.text)
            },
            mViewModel.selectedLicenseTypePos,
            showClearText = false,
            showIconImage = false,
            showAddNewAddressText = false
        )
        fragment.show(childFragmentManager, fragment.tag)
    }

    private fun buildApiRequest() {
        if (mViewModel.isEditMode.get()) {
            (activity as EditDeliveryProfileActivity).mViewModel.request.carTypeId =
                mViewModel.carBrands[mViewModel.selectedCarBrandPos].id

            (activity as EditDeliveryProfileActivity).mViewModel.request.carModelId =
                mViewModel.carModels[mViewModel.selectedCarModelPos].id

            (activity as EditDeliveryProfileActivity).mViewModel.request.yearId =
                mViewModel.modelYears[mViewModel.selectedModelYearPos].id

            (activity as EditDeliveryProfileActivity).mViewModel.request.vehiclePlateLetters =
                mViewModel.plateLetterText.get()!!

            (activity as EditDeliveryProfileActivity).mViewModel.request.vehiclePlateNumber =
                mViewModel.plateNumberText.get()!!.toInt()

            (activity as EditDeliveryProfileActivity).mViewModel.request.vehicleTypeId =
                mViewModel.licenseTypes[mViewModel.selectedLicenseTypePos].id
        } else {
            (activity as DeliveryAuthenticationActivity).mViewModel.request.carTypeId =
                mViewModel.carBrands[mViewModel.selectedCarBrandPos].id

            (activity as DeliveryAuthenticationActivity).mViewModel.request.carModelId =
                mViewModel.carModels[mViewModel.selectedCarModelPos].id

            (activity as DeliveryAuthenticationActivity).mViewModel.request.yearId =
                mViewModel.modelYears[mViewModel.selectedModelYearPos].id

            (activity as DeliveryAuthenticationActivity).mViewModel.request.vehiclePlateLetters =
                mViewModel.plateLetterText.get()!!

            (activity as DeliveryAuthenticationActivity).mViewModel.request.vehiclePlateNumber =
                mViewModel.plateNumberText.get()!!.toInt()

            (activity as DeliveryAuthenticationActivity).mViewModel.request.vehicleTypeId =
                mViewModel.licenseTypes[mViewModel.selectedLicenseTypePos].id
        }
    }

    override fun openNextStep() {
        buildApiRequest()
        if (mViewModel.isEditMode.get())
            (activity as EditDeliveryProfileActivity).mViewModel.navigator?.openRequiredDocuments()
        else
            (activity as DeliveryAuthenticationActivity).mViewModel.navigator?.openRequiredDocuments()
    }

    companion object {
        fun newInstance(isEditMode: Boolean) =
            CarDetailsFragment().apply {

                arguments = Bundle().apply {
                    putBoolean(Constants.IS_EDIT_MODE, isEditMode)
                }
            }
    }
}
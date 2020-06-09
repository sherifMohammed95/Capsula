package com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.carDetails

import com.freelance.base.BaseFragment
import com.freelance.capsoula.R
import com.freelance.capsoula.custom.bottomSheet.BottomSelectionFragment
import com.freelance.capsoula.databinding.FragmentDeliveryCarDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import rx.functions.Action2

class CarDetailsFragment : BaseFragment<FragmentDeliveryCarDetailsBinding, CarDetailsViewModel>(),
    CarDetailsNavigator {

    private val mViewModel: CarDetailsViewModel by viewModel()

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

    override fun showCarBrandsSheet() {
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.car_brand),
            mViewModel.carBrands,
            Action2 { pos, item ->
                mViewModel.selectedCarBrandId = pos
                mViewModel.carBrandText.set(item.text)
            },
            mViewModel.selectedCarBrandId,
            showClearText = false,
            showIconImage = false,
            showAddNewAddressText = false
        )
        fragment.show(childFragmentManager, fragment.tag)
    }

    override fun showCarModelsSheet() {
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.car_model),
            mViewModel.carModels,
            Action2 { pos, item ->
                mViewModel.selectedCarModelId = pos
                mViewModel.carModelText.set(item.text)
            },
            mViewModel.selectedCarModelId,
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
                mViewModel.selectedModelYearId = pos
                mViewModel.modelYearText.set(item.text)
            },
            mViewModel.selectedModelYearId,
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
                mViewModel.selectedLicenseTypeId = pos
                mViewModel.licenseTypeText.set(item.text)
            },
            mViewModel.selectedLicenseTypeId,
            showClearText = false,
            showIconImage = false,
            showAddNewAddressText = false
        )
        fragment.show(childFragmentManager, fragment.tag)
    }

    override fun openNextStep() {
        TODO("Not yet implemented")
    }
}
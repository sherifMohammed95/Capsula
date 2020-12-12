package com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.personalDetails

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseFragment
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.custom.bottomSheet.BottomSelectionFragment
import com.blueMarketing.capsula.data.ImagePickerOption
import com.blueMarketing.capsula.data.SpinnerModel
import com.blueMarketing.capsula.data.UserAddress
import com.blueMarketing.capsula.databinding.FragmentDeliveryPersonalDetailsBinding
import com.blueMarketing.capsula.ui.addAddress.AddAddressActivity
import com.blueMarketing.capsula.ui.checkout.CustomerCheckoutActivity
import com.blueMarketing.capsula.ui.checkout.fragment.details.IMAGE_PICKER_OPTIONS_LIST
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.DeliveryAuthenticationActivity
import com.blueMarketing.capsula.ui.deliveryMan.editDeliveryProfile.EditDeliveryProfileActivity
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.ImageUtil
import com.google.gson.Gson
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import rx.functions.Action2

class PersonalDetailsFragment :
    BaseFragment<FragmentDeliveryPersonalDetailsBinding, PersonalDetailsViewModel>(),
    PersonalDetailsNavigator {

    private val REQUEST_CAMERA = 1000
    private val REQUEST_GALLERY = 2000
    private val ADD_ADDRESS = 3000

    val mViewModel: PersonalDetailsViewModel by viewModel()
    private val imagePickerOptionsList: ArrayList<ImagePickerOption> by inject(
        named(IMAGE_PICKER_OPTIONS_LIST)
    )

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

    override fun getMyViewModel(): PersonalDetailsViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_delivery_personal_details
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

        mViewModel.nationalitiesResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mViewModel.nationalitiesList = it.list
                SpinnerModel().initialize(mViewModel.nationalitiesList)
                if (mViewModel.isEditMode.get()) {
                    mViewModel.setSelectedNationalityPos()
                }
            }
        })
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun openGallery() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(pickPhoto, REQUEST_GALLERY)
    }

    override fun showImagePickerSheet() {
        openPickerWithPermission()
    }

    private fun buildApiRequest() {
        if (mViewModel.isEditMode.get()) {
            (activity as EditDeliveryProfileActivity).mViewModel.request.personalPicture =
                mViewModel.personalPhotoBase64

            (activity as EditDeliveryProfileActivity).mViewModel.request.nationalityId =
                mViewModel.nationalitiesList[mViewModel.selectedNatiolityPos].id

            (activity as EditDeliveryProfileActivity).mViewModel.request.email =
                mViewModel.emailText.get()!!

            (activity as EditDeliveryProfileActivity).mViewModel.request.phoneNumber =
                mViewModel.phoneText.get()!!

            (activity as EditDeliveryProfileActivity).mViewModel.request.bankAccountNumber =
                mViewModel.bankAccountText.get()!!

            (activity as EditDeliveryProfileActivity).mViewModel.request.addressDesc =
                mViewModel.fullAddressText.get()!!

            (activity as EditDeliveryProfileActivity).mViewModel.request.accountHolderAddress =
                mViewModel.fullAddressText.get()!!

            (activity as EditDeliveryProfileActivity).mViewModel.request.latitude =
                mViewModel.fullAddressObj.latitude

            (activity as EditDeliveryProfileActivity).mViewModel.request.longitude =
                mViewModel.fullAddressObj.longitude
        } else {
            (activity as DeliveryAuthenticationActivity).mViewModel.request.personalPicture =
                mViewModel.personalPhotoBase64

            (activity as DeliveryAuthenticationActivity).mViewModel.request.fullName =
                mViewModel.fullNameText.get()!!

            (activity as DeliveryAuthenticationActivity).mViewModel.request.nationalityId =
                mViewModel.nationalitiesList[mViewModel.selectedNatiolityPos].id

            (activity as DeliveryAuthenticationActivity).mViewModel.request.email =
                mViewModel.emailText.get()!!

            (activity as DeliveryAuthenticationActivity).mViewModel.request.phoneNumber =
                mViewModel.phoneText.get()!!

            (activity as DeliveryAuthenticationActivity).mViewModel.request.bankAccountNumber =
                mViewModel.bankAccountText.get()!!

            (activity as DeliveryAuthenticationActivity).mViewModel.request.nationalId =
                mViewModel.nationalIdText.get()!!

            (activity as DeliveryAuthenticationActivity).mViewModel.request.addressDesc =
                mViewModel.fullAddressText.get()!!

            (activity as DeliveryAuthenticationActivity).mViewModel.request.accountHolderAddress =
                mViewModel.fullAddressText.get()!!

            (activity as DeliveryAuthenticationActivity).mViewModel.request.latitude =
                mViewModel.fullAddressObj.latitude

            (activity as DeliveryAuthenticationActivity).mViewModel.request.longitude =
                mViewModel.fullAddressObj.longitude

        }
    }

    override fun openNextStep() {
        buildApiRequest()
        if (mViewModel.isEditMode.get())
            (activity as EditDeliveryProfileActivity).mViewModel.navigator?.openCarDetails()
        else
            (activity as DeliveryAuthenticationActivity).mViewModel.navigator?.openCarDetails()
    }

    override fun addAddress() {
        val intent = Intent(activity, AddAddressActivity::class.java)
        intent.putExtra(Constants.FROM_WHERE, Constants.FROM_DELIVERY_PERSONAL_DETAILS)
        startActivityForResult(intent, ADD_ADDRESS);
    }

    override fun showNationalitiesSheet() {
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.citizenship),
            mViewModel.nationalitiesList,
            Action2 { pos, item ->
                mViewModel.selectedNatiolityPos = pos
                mViewModel.citizenshipText.set(item.text)
            },
            mViewModel.selectedNatiolityPos,
            showClearText = false,
            showIconImage = false,
            showAddNewAddressText = false
        )
        fragment.show(childFragmentManager, fragment.tag)
    }

    private fun openPickerWithPermission() = runWithPermissions(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        options = quickPermissionsOptions
    ) {
        showImagePickerFragment()
    }

    private fun showImagePickerFragment() {
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.choose_option),
            imagePickerOptionsList,
            Action2 { pos, item ->
                when (pos) {
                    0 -> openGallery()
                    1 -> openCamera()
                }
            }, -1, showClearText = false, showIconImage = false, showAddNewAddressText = false
        )
        fragment.show(childFragmentManager, fragment.tag)
    }

    private val quickPermissionsOptions = QuickPermissionsOptions(
        permanentDeniedMethod = { handleDenyPermissionsPermanently(it) },
        rationaleMethod = { handleDenyPermissions(it) }
    )

    private fun handleDenyPermissions(arg: QuickPermissionsRequest) {

        (activity as CustomerCheckoutActivity).showPopUp(
            R.string.permission_denied,
            android.R.string.ok,
            DialogInterface.OnClickListener { _, _ ->
            }, false
        )
    }

    private fun handleDenyPermissionsPermanently(arg: QuickPermissionsRequest) {
        (activity as CustomerCheckoutActivity).showPopUp(
            getString(R.string.permission_denied_permanently),
            android.R.string.ok,
            DialogInterface.OnClickListener { _, _ ->
                arg.openAppSettings()
            }, false
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    val contentURI = data.data
                    var bitmap =
                        MediaStore.Images.Media.getBitmap(activity!!.contentResolver, contentURI)
                    mViewModel.personalPhotoUri.set(contentURI)
                    bitmap = ImageUtil.getResizedBitmap(bitmap, 800)
                    convertBitmapToBase64(bitmap)
                    mViewModel.hasPersonalPhotoError.set(false)
                }
                REQUEST_CAMERA -> {
                    var thumbnail = data.extras?.get("data") as Bitmap
                    mViewModel.personalPhotoUri.set(
                        ImageUtil.getImageUri(
                            activity!!,
                            thumbnail
                        )
                    )
                    thumbnail = ImageUtil.getResizedBitmap(thumbnail, 800)
                    convertBitmapToBase64(thumbnail)
                    mViewModel.hasPersonalPhotoError.set(false)
                }
                ADD_ADDRESS -> {
                    val result = data.getStringExtra(Constants.EXTRA_ADD_NEW_ADDRESS)
                    mViewModel.fullAddressObj = Gson().fromJson(result, UserAddress::class.java)
                    mViewModel.fullAddressText.set(mViewModel.fullAddressObj.addressDesc)
                }
            }
        }
    }

    private fun convertBitmapToBase64(bm: Bitmap) {
//        mViewModel.setIsLoading(true)
        Thread(Runnable {
            val encodedImage = ImageUtil.convert2Base64(bm)
            if (!encodedImage!!.contentEquals("")) {
                mViewModel.personalPhotoBase64 = encodedImage
//                mViewModel.setIsLoading(false)
            }
        }).start()
    }

    companion object {
        fun newInstance(isEditMode: Boolean) =
            PersonalDetailsFragment().apply {

                arguments = Bundle().apply {
                    putBoolean(Constants.IS_EDIT_MODE, isEditMode)
                }
            }
    }
}
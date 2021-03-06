package com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.requiredDocuments

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
import com.blueMarketing.capsula.databinding.FragmentRequiredDocumentsBinding
import com.blueMarketing.capsula.ui.checkout.CustomerCheckoutActivity
import com.blueMarketing.capsula.ui.checkout.fragment.details.IMAGE_PICKER_OPTIONS_LIST
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.DeliveryAuthenticationActivity
import com.blueMarketing.capsula.ui.deliveryMan.editDeliveryProfile.EditDeliveryProfileActivity
import com.blueMarketing.capsula.ui.terms.TermsActivity
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.ImageUtil
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import rx.functions.Action2

class RequiredDocumentsFragment :
    BaseFragment<FragmentRequiredDocumentsBinding, RequiredDocumentsViewModel>(),
    RequiredDocumentsNavigator {

    private val REQUEST_CAMERA = 1000
    private val REQUEST_GALLERY = 2000

    val mViewModel: RequiredDocumentsViewModel by viewModel()
    private val imagePickerOptionsList: ArrayList<ImagePickerOption> by inject(
        named(
            IMAGE_PICKER_OPTIONS_LIST
        )
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

    override fun getMyViewModel(): RequiredDocumentsViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_required_documents
    }

    override fun init() {
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        viewModel = mViewModel
        mViewModel.navigator = this
    }

    private fun subscribeToLiveData() {

        mViewModel.saveApiRequestEvent.observe(viewLifecycleOwner, Observer {
            buildApiRequest()
            (activity as EditDeliveryProfileActivity).mViewModel.updateProfile()
        })

        mViewModel.deliveryRegisterResponse.observe(viewLifecycleOwner, Observer {
            showPopUp(
                it, android.R.string.ok,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    (activity as DeliveryAuthenticationActivity).mViewModel.navigator?.openLogin()
                }, false
            )
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

    override fun showImagePickerSheet(currentImage: Int) {
        openPickerWithPermission(currentImage)
    }

    private fun buildApiRequest() {
        if (mViewModel.isEditMode.get()) {
            (activity as EditDeliveryProfileActivity).mViewModel.request.driverLicensePicture =
                mViewModel.carLicenseBase64

            (activity as EditDeliveryProfileActivity).mViewModel.request.idCardPicture =
                mViewModel.nationalIDBase64

            (activity as EditDeliveryProfileActivity).mViewModel.request.carFromFrontPicture =
                mViewModel.carFrontBase64

            (activity as EditDeliveryProfileActivity).mViewModel.request.carFromBehindPicture =
                mViewModel.carBackBase64

            (activity as EditDeliveryProfileActivity).mViewModel.request.carRegistrationPicture =
                mViewModel.carRegistrationBase64
        } else {
            (activity as DeliveryAuthenticationActivity).mViewModel.request.driverLicensePicture =
                mViewModel.carLicenseBase64

            (activity as DeliveryAuthenticationActivity).mViewModel.request.idCardPicture =
                mViewModel.nationalIDBase64

            (activity as DeliveryAuthenticationActivity).mViewModel.request.carFromFrontPicture =
                mViewModel.carFrontBase64

            (activity as DeliveryAuthenticationActivity).mViewModel.request.carFromBehindPicture =
                mViewModel.carBackBase64

            (activity as DeliveryAuthenticationActivity).mViewModel.request.carRegistrationPicture =
                mViewModel.carRegistrationBase64
        }
    }

    override fun submitRequest() {
        buildApiRequest()
        mViewModel.register((activity as DeliveryAuthenticationActivity).mViewModel.request)
    }

    override fun openTerms() {
        startActivity(Intent(activity, TermsActivity::class.java))
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
        rationaleMethod = { handleDenyPermissions(it) })

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

    private fun openPickerWithPermission(currentImage: Int) = runWithPermissions(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        options = quickPermissionsOptions
    ) {
        mViewModel.currentPickedImageIndex.set(currentImage)
        showImagePickerFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    val contentURI = data.data
                    var bitmap =
                        MediaStore.Images.Media.getBitmap(activity!!.contentResolver, contentURI)
                    mViewModel.currentPickedImageUri.set(contentURI)
                    bitmap = ImageUtil.getResizedBitmap(bitmap, 800)
                    convertBitmapToBase64(bitmap)
                    mViewModel.hideError()
                }
                REQUEST_CAMERA -> {
                    var thumbnail = data.extras?.get("data") as Bitmap
                    mViewModel.currentPickedImageUri.set(
                        ImageUtil.getImageUri(
                            activity!!,
                            thumbnail
                        )
                    )
                    thumbnail = ImageUtil.getResizedBitmap(thumbnail, 800)
                    convertBitmapToBase64(thumbnail)
                    mViewModel.hideError()
                }
            }
        }
    }

    private fun convertBitmapToBase64(bm: Bitmap) {
//        mViewModel.setIsLoading(true)
        Thread(Runnable {
            val encodedImage = ImageUtil.convert2Base64(bm)
            if (!encodedImage!!.contentEquals("")) {
                mViewModel.setImageBase64(encodedImage)
//                mViewModel.setIsLoading(false)
            }
        }).start()
    }

    companion object {
        fun newInstance(isEditMode: Boolean) =
            RequiredDocumentsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(Constants.IS_EDIT_MODE, isEditMode)
                }
            }
    }
}
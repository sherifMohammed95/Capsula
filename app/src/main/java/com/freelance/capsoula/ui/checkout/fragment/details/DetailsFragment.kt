package com.freelance.capsoula.ui.checkout.fragment.details

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.freelance.base.BaseFragment
import com.freelance.capsoula.R
import com.freelance.capsoula.custom.bottomSheet.BottomSelectionFragment
import com.freelance.capsoula.data.ImagePickerOption
import com.freelance.capsoula.data.PaymentMethod
import com.freelance.capsoula.databinding.FragmentDetailsBinding
import com.freelance.capsoula.ui.checkout.CheckoutActivity
import com.freelance.capsoula.ui.checkout.fragment.cart.CartViewModel
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.Domain
import com.freelance.capsoula.utils.ImageUtil
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import rx.functions.Action2

class DetailsFragment : BaseFragment<FragmentDetailsBinding, DetailsViewModel>(), DetailsNavigator {

    private val mViewModel: DetailsViewModel by viewModel()
    private val imagePickerOptionsList: ArrayList<ImagePickerOption> by inject(
        named(
            IMAGE_PICKER_OPTIONS_LIST
        )
    )

    private val paymentMethodList: ArrayList<PaymentMethod> by inject(
        named(
            PAYMENT_METHOD_LIST
        )
    )

    private val REQUEST_CAMERA = 1000
    private val REQUEST_GALLERY = 2000

    override fun getMyViewModel(): DetailsViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_details
    }

    override fun init() {
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        viewModel = mViewModel
//        mViewModel.navigator = this
    }

    override fun onResume() {
        super.onResume()
        (activity as CheckoutActivity).viewDataBinding?.toolbar?.progressBarImageView
            ?.setImageResource(R.drawable.details_progress_bar)
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

    override fun showPaymentMethodSheet() {
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.payment_method),
            paymentMethodList,
            Action2 { pos, item ->
                mViewModel.selectedPaymentMethodPos = pos
                mViewModel.paymentMethodText.set(item.text)
//                when (pos) {
//                    0 -> openGallery()
//                    1 -> openCamera()
//                }
            }, mViewModel.selectedPaymentMethodPos, showClearText = false, showIconImage = true
        )
        fragment.show(childFragmentManager, fragment.tag)
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

    private fun showImagePickerFragment() {
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.choose_option),
            imagePickerOptionsList,
            Action2 { pos, item ->
                when (pos) {
                    0 -> openGallery()
                    1 -> openCamera()
                }
            }, -1, showClearText = false, showIconImage = false
        )
        fragment.show(childFragmentManager, fragment.tag)
    }

    private val quickPermissionsOptions = QuickPermissionsOptions(
        permanentDeniedMethod = { handleDenyPermissionsPermanently(it) },
        rationaleMethod = { handleDenyPermissions(it) }
    )

    private fun handleDenyPermissions(arg: QuickPermissionsRequest) {

        (activity as CheckoutActivity).showPopUp(
            R.string.permission_denied,
            android.R.string.ok,
            DialogInterface.OnClickListener { _, _ ->
            }, false
        )
    }

    private fun handleDenyPermissionsPermanently(arg: QuickPermissionsRequest) {
        (activity as CheckoutActivity).showPopUp(
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
                    mViewModel.currentPickedImageUri.set(contentURI)
                    bitmap = ImageUtil.getResizedBitmap(bitmap, 800)
                    convertBitmapToBase64(bitmap)
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
}
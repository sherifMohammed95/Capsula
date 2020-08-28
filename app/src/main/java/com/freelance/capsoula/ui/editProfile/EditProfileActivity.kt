package com.freelance.capsoula.ui.editProfile

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.custom.bottomSheet.BottomSelectionFragment
import com.freelance.capsoula.data.ImagePickerOption
import com.freelance.capsoula.data.UserAddress
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.databinding.ActivityEditProfileBinding
import com.freelance.capsoula.ui.addAddress.AddAddressActivity
import com.freelance.capsoula.ui.checkout.CheckoutActivity
import com.freelance.capsoula.ui.checkout.fragment.details.IMAGE_PICKER_OPTIONS_LIST
import com.freelance.capsoula.ui.resetPassword.ResetPasswordActivity
import com.freelance.capsoula.ui.userTypes.UserTypesViewModel
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.ImageUtil
import com.google.gson.Gson
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import rx.functions.Action2

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding, EditProfileViewModel>(),
    EditProfileNavigator {

    private val REQUEST_CAMERA = 1000
    private val REQUEST_GALLERY = 2000
    private val mViewModel: EditProfileViewModel by viewModel()

    private val imagePickerOptionsList: ArrayList<ImagePickerOption> by inject(
        named(IMAGE_PICKER_OPTIONS_LIST)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToLiveData()
    }

    override fun getMyViewModel(): EditProfileViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_profile
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    private fun subscribeToLiveData() {

        mViewModel.updateDefaultAddressResponse.observe(this, Observer {
            mViewModel.setSelectedUserAddressPos()
            mViewModel.userAddress.set(mViewModel.selectedAddress.text)
        })

        mViewModel.updateUserProfileResponse.observe(this, Observer {
            finish()
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
        fragment.show(supportFragmentManager, fragment.tag)
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

    override fun backAction() {
        finish()
    }

    override fun showImagePickerSheet() {
        openPickerWithPermission()
    }

    override fun changeUserAddress() {
        val userAddressList = UserDataSource.getUser()?.userAddresses
        UserAddress().initialize(userAddressList)
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.change_user_address),
            userAddressList!!,
            Action2 { pos, item ->

                mViewModel.selectedAddress = item
                mViewModel.updateDefaultAddress()
            },
            mViewModel.selectedUserAddressPos,
            showClearText = false,
            showIconImage = true,
            showAddNewAddressText = true
        )
        fragment.show(supportFragmentManager, fragment.tag);
    }

    override fun changeUserPassword() {
        val intent = Intent(this,ResetPasswordActivity::class.java)
        intent.putExtra(Constants.FROM_CHANGE_PASSWORD, true)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    val contentURI = data.data
                    var bitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    mViewModel.personalPhotoUri.set(contentURI)
                    bitmap = ImageUtil.getResizedBitmap(bitmap, 800)
                    convertBitmapToBase64(bitmap)
                }
                REQUEST_CAMERA -> {
                    var thumbnail = data.extras?.get("data") as Bitmap
                    mViewModel.personalPhotoUri.set(
                        ImageUtil.getImageUri(
                            this,
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
                mViewModel.personalPhotoBase64 = encodedImage
//                mViewModel.setIsLoading(false)
            }
        }).start()
    }
}
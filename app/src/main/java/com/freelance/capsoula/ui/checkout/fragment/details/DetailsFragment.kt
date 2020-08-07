package com.freelance.capsoula.ui.checkout.fragment.details

import android.Manifest
import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.freelance.base.BaseFragment
import com.freelance.capsoula.R
import com.freelance.capsoula.custom.bottomSheet.BottomSelectionFragment
import com.freelance.capsoula.data.ImagePickerOption
import com.freelance.capsoula.data.PaymentMethod
import com.freelance.capsoula.data.UserAddress
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.databinding.FragmentDetailsBinding
import com.freelance.capsoula.ui.checkout.CheckoutActivity
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.ImageUtil
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings
import com.oppwa.mobile.connect.checkout.meta.CheckoutStorePaymentDetailsMode
import com.oppwa.mobile.connect.exception.PaymentError
import com.oppwa.mobile.connect.provider.Connect
import kotlinx.android.synthetic.main.fragment_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import rx.functions.Action2
import kotlin.math.round


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
        mViewModel.getDeliveryCost()
    }

    override fun onResume() {
        super.onResume()
        (activity as CheckoutActivity).viewDataBinding?.toolbar?.progressBarImageView
            ?.setImageResource(R.drawable.details_progress_bar)
    }


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
        mViewModel.navigator = this
    }

    @SuppressLint("SetTextI18n")
    private fun calCostDetails() {
        var total = 0.0
        var prodPrice: Double
        UserDataSource.getUser()?.cartContent?.forEach {
            prodPrice = if (it.offerType == Constants.DISCOUNT_OFFER)
                it.priceInOffer!!.toDouble()
            else
                it.price

            total += (it.quantity * prodPrice)
        }

        val estimatedTotal =
            round((total + delivery_cost_value.text.toString().toDouble()) * 100) / 100
        items_cost_value.text = total.toString()
        estimated_total_value.text = estimatedTotal.toString()
    }

    private fun subscribeToLiveData() {
        mViewModel.deliveryCostResponse.observe(viewLifecycleOwner, Observer {
            val cost = round((it.toString().toDouble() * 100)) / 100
            delivery_cost_value.text = cost.toString()
            calCostDetails()
        })
        mViewModel.updateDefaultAddressResponse.observe(viewLifecycleOwner, Observer {
            mViewModel.setSelectedAddressPos()
            mViewModel.deliveryAddressText.set(mViewModel.selectedAddress.text)
            mViewModel.getDeliveryCost()
        })

        mViewModel.successEvent.observe(viewLifecycleOwner, Observer {
            if (mViewModel.selectedPaymentMethodValue == Constants.CASH)
                (activity as CheckoutActivity).openDoneFragment()
            else {
                mViewModel.prepareCheckout()
            }
        })

        mViewModel.checkTotalCostEvent.observe(viewLifecycleOwner, Observer {

            if (estimated_total_value.text.toString().toDouble() > 500 &&
                mViewModel.selectedPaymentMethodValue == Constants.CASH
            ) {
                showPopUp(
                    "", getString(R.string.proceed_online_payment),
                    getString(android.R.string.ok), false
                )
            } else {
                if (mViewModel.selectedPaymentMethodValue == Constants.CASH) {
                    mViewModel.resoursePath = ""
                    mViewModel.submitCheckoutDetails()
                } else {
                    mViewModel.prepareCheckout()
                }
            }
        })

        mViewModel.checkoutIdResponse.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                openHyperPayCheckout(it)
            }
        })
    }

    private fun openHyperPayCheckout(checkoutId: String) {
        val paymentBrands: MutableSet<String> = LinkedHashSet()
        paymentBrands.add("VISA")
        paymentBrands.add("MADA")
        val checkoutSettings =
            CheckoutSettings(checkoutId, paymentBrands, Connect.ProviderMode.TEST);
        checkoutSettings.locale = "en_US";
        checkoutSettings.storePaymentDetailsMode = CheckoutStorePaymentDetailsMode.ALWAYS
        checkoutSettings.shopperResultUrl = "capsula://result";

        val intent =
            Intent(activity, com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity::class.java)
        intent.putExtra(
            com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity.CHECKOUT_SETTINGS,
            checkoutSettings
        )

        startActivityForResult(
            intent,
            com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity.REQUEST_CODE_CHECKOUT
        )
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
                when (pos) {
                    0 -> mViewModel.selectedPaymentMethodValue = Constants.CASH
                    1 -> mViewModel.selectedPaymentMethodValue = Constants.MADA
                }
            },
            mViewModel.selectedPaymentMethodPos,
            showClearText = false,
            showIconImage = true,
            showAddNewAddressText = false
        )
        fragment.show(childFragmentManager, fragment.tag)
    }

    override fun showDeliveryAddressSheet() {
        val userAddressList = UserDataSource.getUser()?.userAddresses
        UserAddress().initialize(userAddressList)
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.delivery_address),
            userAddressList!!,
            Action2 { pos, item ->

                mViewModel.selectedAddress = item
                mViewModel.updateDefaultAddress()
            },
            mViewModel.selectedDeliveryAddressPos,
            showClearText = false,
            showIconImage = true,
            showAddNewAddressText = true
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
            }, -1, showClearText = false, showIconImage = false, showAddNewAddressText = false
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

        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    REQUEST_GALLERY -> {
                        val contentURI = data?.data
                        var bitmap =
                            MediaStore.Images.Media.getBitmap(
                                activity!!.contentResolver,
                                contentURI
                            )
                        mViewModel.currentPickedImageUri.set(contentURI)
                        bitmap = ImageUtil.getResizedBitmap(bitmap, 800)
                        convertBitmapToBase64(bitmap)
                        mViewModel.prescriptionError.set(false)
                    }
                    REQUEST_CAMERA -> {
                        var thumbnail = data?.extras?.get("data") as Bitmap
                        mViewModel.currentPickedImageUri.set(
                            ImageUtil.getImageUri(
                                activity!!,
                                thumbnail
                            )
                        )
                        thumbnail = ImageUtil.getResizedBitmap(thumbnail, 800)
                        convertBitmapToBase64(thumbnail)
                        mViewModel.prescriptionError.set(false)
                    }
                }
            }

            com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity.RESULT_OK -> {

                /* resource path if needed */
                val resourcePath =
                    data?.getStringExtra(
                        com.oppwa.mobile.connect.checkout.dialog
                            .CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH
                    )

                Toast.makeText(activity, resourcePath, Toast.LENGTH_LONG).show()
            }

            com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity.RESULT_ERROR -> {
                val error: PaymentError? =
                    data?.getParcelableExtra(
                        com.oppwa.mobile.connect.checkout
                            .dialog.CheckoutActivity.CHECKOUT_RESULT_ERROR
                    )

                Toast.makeText(activity, error?.errorInfo, Toast.LENGTH_LONG).show()

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
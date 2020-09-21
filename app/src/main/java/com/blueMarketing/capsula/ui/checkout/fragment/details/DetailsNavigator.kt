package com.blueMarketing.capsula.ui.checkout.fragment.details

interface DetailsNavigator {

    fun showImagePickerSheet(currentImage: Int)

    fun showPaymentMethodSheet()

    fun showDeliveryAddressSheet()
}
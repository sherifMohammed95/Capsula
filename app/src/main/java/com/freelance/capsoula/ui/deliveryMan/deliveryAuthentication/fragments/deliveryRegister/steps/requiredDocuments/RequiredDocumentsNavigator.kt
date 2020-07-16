package com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.requiredDocuments

interface RequiredDocumentsNavigator {

    fun showImagePickerSheet(currentImage: Int)

    fun submitRequest()

    fun openTerms()
}
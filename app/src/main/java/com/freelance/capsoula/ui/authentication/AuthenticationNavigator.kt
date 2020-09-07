package com.freelance.capsoula.ui.authentication

interface AuthenticationNavigator {

    fun openLogin()

    fun openRegister()

    fun signInWithGoogle()

    fun signInWithFaceBook()

    fun signInWithTwitter()

    fun openVerification()

    fun openHome()

    fun openCompleteProfile()

    fun openAddAddress()

    fun openForgetPassword()

    fun openTerms()
}
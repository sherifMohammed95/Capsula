package com.freelance.capsoula.utils

import android.app.Application

object Domain {

    internal lateinit var application: Application private set


    fun integrateWith(application: Application) {
        Domain.application = application
    }
}
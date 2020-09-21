package com.blueMarketing.capsula.utils

import com.ninenox.kotlinlocalemanager.ApplicationLocale

object Domain {

    internal lateinit var application: ApplicationLocale private set


    fun integrateWith(application: ApplicationLocale) {
        Domain.application = application
    }

}
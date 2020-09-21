package com.blueMarketing.capsula.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import rx.functions.Action1


@SuppressLint("Registered")
class SocialNetworkUtils : FragmentActivity() {


    private val facebookPermissions = listOf("email", "public_profile")

    private val callbackManager = CallbackManager.Factory.create()

    @SuppressLint("MissingSuperCall")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun loginWithFacebook(
        context: Activity, accessToken: Action1<String>, fbError: Action1<String>
    ) {

        LoginManager.getInstance().logInWithReadPermissions(context, facebookPermissions)
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    accessToken.call(AccessToken.getCurrentAccessToken().token)
                }

                override fun onCancel() {
                    Log.e("Facebook", "Cancelled")
                }

                override fun onError(error: FacebookException) {
                    fbError.call(error.localizedMessage)
                    error.printStackTrace()
                    Log.e("Facebook", error.message)
                }
            })
    }

    fun initGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("757084905257-j3gt3clkmltd4uir5gfhq1ictrnfsbf0.apps.googleusercontent.com")
            .build()
        return GoogleSignIn.getClient(context, gso)

    }

}
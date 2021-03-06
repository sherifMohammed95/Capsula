package com.blueMarketing.capsula.utils

import android.view.ViewAnimationUtils
import android.view.View
import android.animation.AnimatorListenerAdapter
import android.animation.Animator
import io.reactivex.functions.Action


object AnimationUtils {


    fun circularTransition(view: View) {

        val x = (view.parent as View).right
        val y = (view.parent as View).bottom
        val startRadius = 0
        val endRadius = Math.hypot(
            (view.parent as View).width.toDouble(),
            (view.parent as View).height.toDouble()
        ).toInt()
        val anim = ViewAnimationUtils.createCircularReveal(
            view,
            x,
            y,
            startRadius.toFloat(),
            endRadius.toFloat()
        )
        anim.duration = 1500
        view.visibility = View.VISIBLE
        anim.start()
    }


    fun circularReverseTransition(view: View,callback: Action) {

        val x = (view.parent as View).right
        val y = (view.parent as View).bottom
        val startRadius = Math.max((view.parent as View).width, (view.parent as View).height)
        val endRadius = 0
        val anim = ViewAnimationUtils.createCircularReveal(
            view,
            x,
            y,
            startRadius.toFloat(),
            endRadius.toFloat()
        )
        anim.duration = 1500
        //        view.setVisibility(View.VISIBLE);
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                try {
                    callback.run()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
        anim.start()
    }
}
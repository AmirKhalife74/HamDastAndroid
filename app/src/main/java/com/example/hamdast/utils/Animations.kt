package com.example.hamdast.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatDelegate
import kotlin.math.hypot

class Animations {

     private fun toggleThemeWithAnimation(rootLayOut: View,isDarkMode:Boolean) {
        val cx = rootLayOut.width / 2
        val cy = rootLayOut.height / 2
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val reveal = ViewAnimationUtils.createCircularReveal(
            rootLayOut, cx, cy, 0f, finalRadius
        )
        reveal.duration = 500
        reveal.interpolator = AccelerateDecelerateInterpolator()

             reveal.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                val mode = if (isDarkMode)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO

                AppCompatDelegate.setDefaultNightMode(mode)
            }
        })

        reveal.start()
    }
}
package com.example.hamdast.utils

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.hamdast.R


fun hideButtonWithFade(view: View) {
    view.animate()
        .alpha(0f)
        .setDuration(300)
        .withEndAction {
            view.visibility = View.GONE
        }
        .start()
}

 fun showButtonWithFade(view: View) {
    view.apply {
        alpha = 0f
        visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .setDuration(300)
            .start()
    }
}

fun NavController.navigateWithAnim(actionId: Int) {
    val options = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()
    this.navigate(actionId, null, options)
}
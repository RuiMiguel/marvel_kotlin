package com.everis.android.marvelkotlin.presentation.extension

import android.app.Activity
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.constraintlayout.widget.Group

fun Group.show(visible: Boolean = true) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun Group.hide(visible: Boolean = true) {
    show(!visible)
}

fun View.show(visible: Boolean = true) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.hide(visible: Boolean = true) {
    show(!visible)
}

fun View?.visible() {
    this ?: return
    this.visibility = View.VISIBLE
}

fun View?.invisible() {
    this ?: return
    this.visibility = View.INVISIBLE
}

fun View?.gone() {
    this ?: return
    this.visibility = View.GONE
}


fun View?.fadeIn(fadeDuration: Long? = 1000) {
    this?.let { view ->
        clearAnimation()
        val animation = AlphaAnimation(alpha, 1f).apply {
            duration = fadeDuration ?: 1000
            interpolator = AccelerateDecelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {
                    view.show()
                }

            })
        }
        startAnimation(animation)
    }
}

fun View?.fadeOut(fadeDuration: Long? = 1000) {
    this?.let { view ->
        clearAnimation()

        val animation = AlphaAnimation(alpha, 0f).apply {
            duration = fadeDuration ?: 1000
            interpolator = AccelerateDecelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    view.hide()
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
        }
        startAnimation(animation)
    }
}

fun Activity.screenSize(): DisplayMetrics {
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics
}

fun View.setSafeOnClickListener(elapsedDelay: Int = 500, onSafeClick: (View) -> Unit) {
    val safeClickListener = object: View.OnClickListener {
        private var lastTimeClicked: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastTimeClicked < elapsedDelay) {
                return
            }
            lastTimeClicked = SystemClock.elapsedRealtime()
            onSafeClick(v)
        }
    }
    setOnClickListener(safeClickListener)
}
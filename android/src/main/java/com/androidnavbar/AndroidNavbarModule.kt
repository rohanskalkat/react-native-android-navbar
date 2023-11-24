// package com.androidnavbar

// import com.facebook.react.bridge.ReactApplicationContext
// import com.facebook.react.bridge.ReactMethod
// import com.facebook.react.bridge.Promise

// class AndroidNavbarModule internal constructor(context: ReactApplicationContext) :
//   AndroidNavbarSpec(context) {

//   override fun getName(): String {
//     return NAME
//   }

//   // Example method
//   // See https://reactnative.dev/docs/native-modules-android
//   @ReactMethod
//   override fun multiply(a: Double, b: Double, promise: Promise) {
//     promise.resolve(a * b)
//   }

//   companion object {
//     const val NAME = "AndroidNavbar"
//   }
// }


package com.androidnavbar

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.turbomodule.core.interfaces.TurboModule

class AndroidNavbarModule internal constructor(context: ReactApplicationContext) :
    AndroidNavbarSpec(context) {
    private val reactContext = context


    companion object {
        const val NAME = "AndroidNavbar"
    }

    private fun setNavigationBarTheme(activity: Activity?, light: Boolean) {
        activity?.let { act ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val window: Window = act.window
                val decorView: View = window.decorView
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    // For Android 11 (API level 30) and above
                    val windowInsetsController = decorView.windowInsetsController
                    windowInsetsController?.let { controller ->
                        if (light) {
                            controller.setSystemBarsAppearance(
                                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                            )
                        } else {
                            controller.setSystemBarsAppearance(
                                0,
                                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                            )
                        }
                    }
                } else {
                    // For Android 8 (API level 26) to Android 10 (API level 29)
                    var flags: Int = decorView.systemUiVisibility
                    flags = if (light) {
                        flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    } else {
                        flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
                    }
                    decorView.systemUiVisibility = flags
                }
            }
        }
    }

    @ReactMethod
    override fun changeNavigationBarColor(color: String, light: Boolean, animated: Boolean, promise: Promise) {
        val currentActivity = reactContext.currentActivity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            currentActivity?.let {
                UiThreadUtil.runOnUiThread {
                    try {
                        val window: Window = it.window
                        // Clear flags for transparent and translucent, set them later if needed
                        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

                        if (color == "transparent" || color == "translucent") {
                            if (color == "transparent") {
                                window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                            } else {
                                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                            }
                        }

                        val colorTo: Int = if (color == "transparent") Color.TRANSPARENT else Color.parseColor(color)
                        if (animated) {
                            val colorFrom: Int = window.navigationBarColor
                            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
                            colorAnimation.duration = 200 // Duration of the animation
                            colorAnimation.addUpdateListener { animator ->
                                window.navigationBarColor = animator.animatedValue as Int
                            }
                            colorAnimation.start()
                        } else {
                            window.navigationBarColor = colorTo
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            // For Android 11 (API level 30) and above
                            val windowInsetsController = window.insetsController
                            if (light) {
                                windowInsetsController?.setSystemBarsAppearance(
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                                )
                            } else {
                                windowInsetsController?.setSystemBarsAppearance(
                                    0,
                                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                                )
                            }
                        } else {
                            // For Android 8 (API level 26) to Android 10 (API level 29)
                            var flags: Int = window.decorView.systemUiVisibility
                            flags = if (light) {
                                flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                            } else {
                                flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
                            }
                            window.decorView.systemUiVisibility = flags
                        }

                        promise.resolve(true)
                    } catch (e: Exception) {
                        promise.reject("error", e)
                    }
                }
            } ?: run {
                promise.reject("E_NO_ACTIVITY", Throwable("Tried to change the navigation bar while not attached to an Activity"))
            }
        } else {
            promise.reject("API_LEVEL", Throwable("Only Android Lollipop and above is supported"))
        }
    }

    @ReactMethod
    override fun hideNavigationBar(promise: Promise) {
        val currentActivity = reactContext.currentActivity
        if (currentActivity != null) {
            UiThreadUtil.runOnUiThread {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        // Use WindowInsetsController for Android 11 (API level 30) and above
                        currentActivity.window.insetsController?.hide(WindowInsets.Type.navigationBars())
                    } else {
                        // Use systemUiVisibility for API levels below 30
                        val decorView: View = currentActivity.window.decorView
                        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                    }
                    promise.resolve(true)
                } catch (e: Exception) {
                    promise.reject("error", e)
                }
            }
        } else {
            promise.reject("E_NO_ACTIVITY", Throwable("Tried to hide the navigation bar while not attached to an Activity"))
        }
    }

    @ReactMethod
    override fun showNavigationBar(promise: Promise) {
        val currentActivity = reactContext.currentActivity
        if (currentActivity != null) {
            UiThreadUtil.runOnUiThread {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        // Use WindowInsetsController for Android 11 (API level 30) and above
                        currentActivity.window.insetsController?.show(WindowInsets.Type.navigationBars())
                    } else {
                        // Use systemUiVisibility for API levels below 30
                        val decorView: View = currentActivity.window.decorView
                        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    }
                    promise.resolve(true)
                } catch (e: Exception) {
                    promise.reject("error", e)
                }
            }
        } else {
            promise.reject("E_NO_ACTIVITY", Throwable("Tried to show the navigation bar while not attached to an Activity"))
        }
    }
}

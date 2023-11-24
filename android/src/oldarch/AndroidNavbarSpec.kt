package com.androidnavbar

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.Promise

abstract class AndroidNavbarSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {

  abstract fun changeNavigationBarColor(color: String, light: Boolean, animated: Boolean, promise: Promise)
  abstract fun hideNavigationBar(promise: Promise)
  abstract fun showNavigationBar(promise: Promise)
}

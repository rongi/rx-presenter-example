package com.github.rongi.rxpresenter.example

import android.annotation.SuppressLint
import android.app.Application
import com.github.rongi.rxpresenter.example.dagger.AppModule

class App : Application() {

  override fun onCreate() {
    super.onCreate()

    appModule = AppModule(this)
  }

  companion object {
    @SuppressLint("StaticFieldLeak")
    lateinit var appModule: AppModule
  }

}
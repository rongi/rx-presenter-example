package com.github.rongi.rxpresenter.example

import android.app.Application
import com.github.rongi.rxpresenter.example.dagger.AppModule
import com.github.rongi.rxpresenter.example.dagger.DaggerRoots
import com.github.rongi.rxpresenter.example.dagger.Roots

lateinit var appRoots: Roots

class App : Application() {

  override fun onCreate() {
    super.onCreate()

    appRoots = DaggerRoots.builder()
      .appModule(AppModule())
      .build()
  }

}

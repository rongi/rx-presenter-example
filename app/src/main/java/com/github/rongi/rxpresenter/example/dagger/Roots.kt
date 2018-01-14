package com.github.rongi.rxpresenter.example.dagger

import com.github.rongi.rxpresenter.example.app.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface Roots {
  fun inject(item: MainActivity)
}

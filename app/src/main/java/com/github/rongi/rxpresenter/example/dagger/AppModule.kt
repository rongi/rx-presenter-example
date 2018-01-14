package com.github.rongi.rxpresenter.example.dagger

import android.content.Context
import com.github.rongi.rxpresenter.example.app.main.data.ArticlesProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module()
class AppModule(private val context: Context) {

  @Provides
  @Singleton
  fun articlesProvider() = ArticlesProvider()

}

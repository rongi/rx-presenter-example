package com.github.rongi.rxpresenter.example.dagger

import com.github.rongi.rxpresenter.example.app.main.data.ArticlesProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module()
class AppModule {

  @Provides
  @Singleton
  fun articlesProvider() = ArticlesProvider()

}

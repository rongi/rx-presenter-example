package com.github.rongi.rxpresenter.example.app.main.data

import com.github.rongi.rxpresenter.example.common.asObservable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit.MILLISECONDS

class ArticlesProvider {

  fun getArticles() = listOf(
    "Casting a \$20M Mirror for the World’s Largest Telescope ",
    "Nvidia press conference live at CES 2018",
    "Apple shareholders push for study of phone addiction in children",
    "Products Over Projects"
  )
    .map { Article(title = it) }
    .asObservable()
    .singleOrError()
    .subscribeOn(Schedulers.io())
    .delaySubscription(1500, MILLISECONDS)

}

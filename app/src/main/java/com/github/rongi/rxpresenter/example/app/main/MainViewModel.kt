package com.github.rongi.rxpresenter.example.app.main

import com.github.rongi.rxpresenter.example.R
import com.github.rongi.rxpresenter.example.app.main.data.Article
import com.github.rongi.rxpresenter.example.app.main.data.ArticlesProvider
import com.github.rongi.rxpresenter.example.common.*
import io.reactivex.Observable
import io.reactivex.Observable.concat
import io.reactivex.Observable.just

class MainViewModel(
  val articles: Observable<List<Article>>,
  val updateButtonIsEnabled: Observable<Boolean>,
  val emptyViewIsVisible: Observable<Boolean>,
  val progressIsVisible: Observable<Boolean>,
  val smallProgressIsVisible: Observable<Boolean>,
  val updateButtonText: Observable<Int>,
  val startDetailActivitySignals: Observable<Article>
)

fun present(
  updateButtonClicks: Observable<Unit>,
  articleClicks: Observable<Int>,
  articlesProvider: ArticlesProvider
): MainViewModel {
  // Internal states

  // This emits [Start] item when request starts and [Result] item
  // when request is completed
  val getArticlesWithStartAndEnd = articlesProvider.getArticles()
    .markStartAndEnd()

  val getArticlesEvents = updateButtonClicks.flatMapWithDrop(getArticlesWithStartAndEnd)
    .publish().autoConnect()

  val isDownloadingArticles = just(false).concatWith(
    getArticlesEvents.map { it.isRunning() }
  )

  val downloadedArticles = getArticlesEvents
    .filter { it is SingleEvent.Result }
    .map { (it as SingleEvent.Result).data }

  val articles = concat(
    just(emptyList<Article>()),
    downloadedArticles
  )

  val hasArticles = articles.map { it.isNotEmpty() }

  // View states

  val updateButtonIsEnabled = isDownloadingArticles.map { it.not() }

  val emptyViewIsVisible = combineLatest(isDownloadingArticles, hasArticles) { downloadingArticles, hasArticles ->
    !hasArticles && !downloadingArticles
  }

  val progressIsVisible = combineLatest(isDownloadingArticles, hasArticles) { downloadingArticles, hasArticles ->
    downloadingArticles && !hasArticles
  }.distinctUntilChanged()

  val smallProgressIsVisible = combineLatest(isDownloadingArticles, hasArticles) { downloadingArticles, hasArticles ->
    downloadingArticles && hasArticles
  }

  val updateButtonText = isDownloadingArticles.map { downloading ->
    if (downloading) R.string.updating else R.string.update
  }

  val startDetailActivitySignals = articleClicks.withLatestFrom(articles) { articleIndex, articles ->
    articles[articleIndex]
  }

  return MainViewModel(
    articles = articles,
    updateButtonIsEnabled = updateButtonIsEnabled,
    emptyViewIsVisible = emptyViewIsVisible,
    progressIsVisible = progressIsVisible,
    smallProgressIsVisible = smallProgressIsVisible,
    updateButtonText = updateButtonText,
    startDetailActivitySignals = startDetailActivitySignals
  )
}


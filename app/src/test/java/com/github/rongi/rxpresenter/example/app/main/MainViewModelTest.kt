package com.github.rongi.rxpresenter.example.app.main

import com.github.rongi.rxpresenter.example.app.main.data.Article
import com.github.rongi.rxpresenter.example.app.main.data.ArticlesProvider
import com.github.rongi.rxpresenter.example.assertEquals
import com.github.rongi.rxpresenter.example.testSchedulersTestRule
import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.concurrent.TimeUnit.SECONDS

class MainViewModelTest {

  @Rule
  @JvmField
  val testSchedulersRule = testSchedulersTestRule

  private val testScheduler = TestScheduler()

  private val articlesProvider = mock(ArticlesProvider::class.java)

  @Test
  fun `on update click - progress is updated`() {
    `when`(articlesProvider.getArticles()).thenReturn(Single.just(listOf(
      Article("article1"),
      Article("article2")
    )))

    val model = present(
      updateButtonClicks = just(Unit).delaySubscription(1, SECONDS, testScheduler),
      articleClicks = Observable.never(),
      articlesProvider = articlesProvider
    )

    val progressIsVisible = model.progressIsVisible.test()
    testScheduler.advanceTimeTo(20, SECONDS)

    progressIsVisible.assertValues(false, true, false)
  }

  @Test
  fun `on article click - opens article`() {
    `when`(articlesProvider.getArticles()).thenReturn(Single.just(listOf(
      Article("article1"),
      Article("article2")
    )))

    val model = present(
      updateButtonClicks = just(Unit).delaySubscription(1, SECONDS, testScheduler),
      articleClicks = just(1).delaySubscription(10, SECONDS, testScheduler),
      articlesProvider = articlesProvider
    )

    val startDetailActivitySignals = model.startDetailActivitySignals.test()
    val progressIsVisible = model.progressIsVisible.test()
    val emptyViewIsVisible = model.emptyViewIsVisible.test()

    testScheduler.advanceTimeTo(20, SECONDS)

    startDetailActivitySignals.assertResult(Article("article2"))
    progressIsVisible.values().last() assertEquals false
    emptyViewIsVisible.values().last() assertEquals false
  }

}

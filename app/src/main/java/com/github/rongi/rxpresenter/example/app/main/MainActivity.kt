package com.github.rongi.rxpresenter.example.app.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.github.rongi.rxpresenter.example.App
import com.github.rongi.rxpresenter.example.R
import com.github.rongi.rxpresenter.example.app.detail.DetailActivity
import com.github.rongi.rxpresenter.example.common.DividerItemDecoration
import com.github.rongi.rxpresenter.example.common.clicks
import com.github.rongi.rxpresenter.example.common.visible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

  private lateinit var listAdapter: ListAdapter

  private val articleClicks = BehaviorSubject.create<Int>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    initList()

    val model = present(
      updateButtonClicks = update_button.clicks(),
      articleClicks = articleClicks,
      articlesProvider = App.appModule.articlesProvider()
    )

    render(model)
  }

  private fun render(model: MainViewModel) {
    with(model) {
      articles.render { listAdapter.items = it }
      updateButtonIsEnabled.render { update_button.isEnabled = it }
      emptyViewIsVisible.render { empty_view.visible = it }
      progressIsVisible.render { progress.visible = it }
      smallProgressIsVisible.render { progress_small.visible = it }
      updateButtonText.render { update_button.setText(it) }
      startDetailActivitySignals.render { DetailActivity.launch(this@MainActivity, it) }
    }
  }

  private fun initList() {
    recycler_view.layoutManager = LinearLayoutManager(this)
    listAdapter = ListAdapter(this)
    listAdapter.onArticleClickCallback = {
      articleClicks.onNext(it)
    }
    recycler_view.adapter = listAdapter
    val divider = DividerItemDecoration(resources);
    recycler_view.addItemDecoration(divider)
  }

}

fun <T> Observable<T>.render(onNext: (T) -> Unit): Disposable {
  return this.observeOn(mainThread()).subscribe(onNext)
}
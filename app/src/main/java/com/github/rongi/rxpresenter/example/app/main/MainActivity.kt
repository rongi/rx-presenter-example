package com.github.rongi.rxpresenter.example.app.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.rongi.klaster.Klaster
import com.github.rongi.rxpresenter.example.R
import com.github.rongi.rxpresenter.example.app.detail.DetailActivity
import com.github.rongi.rxpresenter.example.app.main.data.Article
import com.github.rongi.rxpresenter.example.appRoots
import com.github.rongi.rxpresenter.example.common.DividerItemDecoration
import com.github.rongi.rxpresenter.example.common.clicks
import com.github.rongi.rxpresenter.example.common.onClick
import com.github.rongi.rxpresenter.example.common.visible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

  private lateinit var listAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>

  private val articleClicks = BehaviorSubject.create<Int>()

  private var articles = emptyList<Article>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    initList()

    val model = present(
      updateButtonClicks = update_button.clicks(),
      articleClicks = articleClicks,
      articlesProvider = appRoots.articlesProvider()
    )

    render(model)
  }

  private fun render(model: MainViewModel) {
    with(model) {
      articles.render {
        this@MainActivity.articles = it
        listAdapter.notifyDataSetChanged()
      }
      updateButtonIsEnabled.render { update_button.isEnabled = it }
      emptyViewIsVisible.render { empty_view.visible = it }
      progressIsVisible.render { progress.visible = it }
      smallProgressIsVisible.render { progress_small.visible = it }
      updateButtonText.render { update_button.setText(it) }
      startDetailActivitySignals.render { DetailActivity.launch(this@MainActivity, it) }
    }
  }

  private fun initList() {
    recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
    listAdapter = createAdapter()
    recycler_view.adapter = listAdapter
    val divider = DividerItemDecoration(resources)
    recycler_view.addItemDecoration(divider)
  }

  private fun createAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> = Klaster.get()
    .itemCount { articles.size }
    .view(R.layout.list_item, layoutInflater)
    .bind { position ->
      article_title.text = articles[position].title
      itemView.onClick {
        articleClicks.onNext(position)
      }
    }
    .build()

}

fun <T> Observable<T>.render(onNext: (T) -> Unit): Disposable {
  return this.observeOn(mainThread()).subscribe(onNext)
}

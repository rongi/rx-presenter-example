package com.github.rongi.rxpresenter.example.app.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rongi.rxpresenter.example.R
import com.github.rongi.rxpresenter.example.app.main.data.Article
import kotlinx.android.synthetic.main.detail_activity.*

class DetailActivity: AppCompatActivity() {

  companion object {
    fun launch(from: Activity, article: Article) {
      val intent = Intent(from, DetailActivity::class.java).apply {
        putExtra("article", article)
      }
      from.startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.detail_activity)

    val article = intent.getSerializableExtra("article") as Article
    article_title.text = article.title
  }

}

package com.github.rongi.rxpresenter.example.app.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.rongi.rxpresenter.example.R
import com.github.rongi.rxpresenter.example.app.main.data.Article
import com.github.rongi.rxpresenter.example.common.onClick
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter(context: Context) : RecyclerView.Adapter<ViewHolder>() {

  private val layoutInflater = LayoutInflater.from(context)

  var onArticleClickCallback: ((position: Int) -> Unit)? = null

  var items = emptyList<Article>()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun getItemCount() = items.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = layoutInflater.inflate(R.layout.list_item, parent, false)
    return object : ViewHolder(view) {}
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.itemView.article_title.text = items[position].title
    holder.itemView.onClick {
      onArticleClickCallback?.invoke(holder.adapterPosition)
    }
  }

}

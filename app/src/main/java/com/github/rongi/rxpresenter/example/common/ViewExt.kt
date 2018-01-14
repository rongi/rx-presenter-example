package com.github.rongi.rxpresenter.example.common

import android.view.View
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

fun View.onClick(function: () -> Unit) {
  this.setOnClickListener {
    function.invoke()
  }
}

fun View.clicks(): Observable<Unit> {
  val clicks = BehaviorSubject.create<Unit>()
  this.setOnClickListener {
    clicks.onNext(Unit)
  }
  return clicks
}

var View.visible: Boolean
  get() {
    return this.visibility == View.VISIBLE
  }
  set(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
  }

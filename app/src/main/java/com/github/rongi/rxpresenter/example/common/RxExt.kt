package com.github.rongi.rxpresenter.example.common

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.functions.BiFunction

fun <T> T.asObservable() = Observable.just(this)!!

/**
 * Flatmaps upstream items into [source] items.
 * Ignores upstream items if there is any [source] instance is currently running.
 *
 * ```
 * upstream ----u-----u---u-------u---------------|-->
 *              ↓                 ↓               ↓
 * source       ---s-------|->    ---s-------|->  ↓
 *                 ↓                 ↓            ↓
 * result   -------s-----------------s------------|-->
 * ```
 */
fun <T, R> Observable<T>.flatMapWithDrop(source: Observable<R>): Observable<R> {
  return this.toFlowable(BackpressureStrategy.DROP)
    .flatMap({ source.toFlowable(BackpressureStrategy.MISSING) }, 1)
    .toObservable()
}

sealed class SingleEvent<T> {
  class Start<T> : SingleEvent<T>()
  data class Result<T>(val data: T) : SingleEvent<T>()
}

fun <T> SingleEvent<T>.isRunning(): Boolean {
  return when (this) {
    is SingleEvent.Start -> true
    is SingleEvent.Result -> false
  }
}

/**
 * Maps upstream starts into [SingleEvent.Start] items.
 * Maps upstream results into [SingleEvent.Result] items.
 *
 * ```
 * upstream ----------------------u---------------|-->
 *          ↓                     ↓               ↓
 * result   Start-----------------Result(u)-------|-->
 * ```
 */
fun <T> Single<T>.markStartAndEnd(): Observable<SingleEvent<T>> {
  return this.map { SingleEvent.Result(it) as SingleEvent<T> }
    .toObservable()
    .startWith(SingleEvent.Start())
}

fun <T1, T2, R> combineLatest(
  source1: ObservableSource<T1>,
  source2: ObservableSource<T2>,
  combiner: (T1, T2) -> R
): Observable<R> {
  return Observable.combineLatest(source1, source2, BiFunction<T1, T2, R> { t1, t2 ->
    combiner(t1, t2)
  })
}

fun <T1, T2, R> Observable<T1>.withLatestFrom(
  other: Observable<T2>,
  combiner: (T1, T2) -> R
): Observable<R> {
  return this.withLatestFrom(other, BiFunction<T1, T2, R> { t1, t2 ->
    return@BiFunction combiner(t1, t2)
  })
}

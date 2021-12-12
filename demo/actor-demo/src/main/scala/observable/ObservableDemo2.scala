package observable

import rx.lang.scala.Observable

import scala.concurrent.duration.DurationInt


object ObservableDemo2 extends App {
  val nums: Observable[Int] = Observable.from(List(1, 2, 3, 4))
  private val results: Observable[Observable[Int]] = nums.map(x => Observable.interval(x.seconds).map(_ => x).take(2)) // to see impl
  //    private val flatten: Observable[Int] = results.flatten
  private val flatten: Observable[Int] = results.concat
  flatten.subscribe(println(_))
  Thread.sleep(30000)
}

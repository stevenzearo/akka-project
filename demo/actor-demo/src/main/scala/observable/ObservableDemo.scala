package observable

import rx.lang.scala.{Observable, Subscription}

import scala.concurrent.duration.DurationInt


object ObservableDemo extends App {
  val ticks: Observable[Long] = Observable.interval(1.seconds)
  private val filteredTicks: Observable[Long] = ticks.filter(_ % 2 == 0)
  private val groupedBuffer: Observable[Seq[Long]] = filteredTicks.slidingBuffer(2, 1)
  private val subscription: Subscription = groupedBuffer.subscribe(println(_))
  Thread.sleep(30000)
  subscription.unsubscribe()
  println("unsubscribe triggered")
  Thread.sleep(30000)
}

package observable

import rx.lang.scala.Subscription
import rx.lang.scala.subjects.ReplaySubject


object ReplaySubjectionDemo extends App {
  val publishSubject = ReplaySubject[Int]()
  val subscriptionA: Subscription = publishSubject.subscribe(x => println(s"a: $x"))
  val subscriptionB: Subscription = publishSubject.subscribe(x => println(s"b: $x"))
  publishSubject.onNext(1)

  subscriptionA.unsubscribe()

  publishSubject.onNext(2)

  publishSubject.onCompleted()

  val subscriptionC: Subscription = publishSubject.subscribe(x => println(s"c: $x"))

  publishSubject.onNext(3)

  Thread.sleep(3000)
}

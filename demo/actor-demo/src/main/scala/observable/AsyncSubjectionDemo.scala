package observable

import rx.lang.scala.Subscription
import rx.lang.scala.subjects.AsyncSubject


object AsyncSubjectionDemo extends App {
    val publishSubject = AsyncSubject[Int]() // cached final value
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

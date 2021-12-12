package observable

import rx.lang.scala.Subscription
import rx.lang.scala.subscriptions.MultipleAssignmentSubscription

object SubscriptionDemo extends App {
  val subscriptionA: Subscription = Subscription {
    println("A")
  }

  val subscriptionB: Subscription = Subscription {
    println("B")
  }

  val subscriptionC: Subscription = Subscription {
    println("C")
  }

  /*val compositeSubscription: CompositeSubscription = CompositeSubscription(subscriptionA, subscriptionB)

  compositeSubscription.unsubscribe()
  compositeSubscription += subscriptionC
*/
  val multipleAssignmentSubscription: MultipleAssignmentSubscription = MultipleAssignmentSubscription()
  multipleAssignmentSubscription.subscription = subscriptionA
  multipleAssignmentSubscription.subscription = subscriptionB
  multipleAssignmentSubscription.subscription = subscriptionC
  multipleAssignmentSubscription.unsubscribe()
  Thread.sleep(3000)
}

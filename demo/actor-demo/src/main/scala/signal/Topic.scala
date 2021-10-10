package signal

import scala.collection.mutable

trait Topic[T] {
    protected val subscribers: mutable.Buffer[Subscriber[T]] = mutable.Buffer.empty
    def register(subscriber: Subscriber[T]): Unit
    def remove(subscriber: Subscriber[T]): Unit
    def publish(t: T): Unit
}
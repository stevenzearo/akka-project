package signal

case class SignalTopic[T](t: T) extends Topic[T] {
    override def register(subscriber: Subscriber[T]): Unit = subscribers.+=(subscriber)

    override def remove(subscriber: Subscriber[T]): Unit = subscribers.-=(subscriber)

    override def publish(t: T): Unit = subscribers.foreach(s => s.handle(t))

    def update(t: T): Unit = publish(t)
}

package signal

class SignalSubscriber[T]() extends Subscriber[T] {
  var t: T = _

  override def handle(t: T): Unit = {
    this.t = t
  }

  def apply(): T = t
}

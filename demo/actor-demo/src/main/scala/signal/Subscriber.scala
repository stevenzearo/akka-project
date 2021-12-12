package signal

trait Subscriber[T] {
  def handle(t: T): Unit
}

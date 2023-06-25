package demo

import akka.actor.Actor

class GreetActor extends Actor {
  override def receive: Receive = {
    case message: ResponseMessage =>
      println(s"sender got message: ${message.msg}")
      message.to ! RequestMessage(s"message from ${self.path}", self)
      Thread.sleep(1000L)
    case s: String => println(s"got message: $s")
    case _ => throw new Exception("got unknown message")
  }
}

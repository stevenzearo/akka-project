package demo

import akka.actor.Actor

class GreetedActor extends Actor {
    override def receive: Receive = {
        case message: Message =>
            println(s"receiver got message: ${message.msg}")
            message.from ! Message(s"message from ${self.path}, receiver got message: ${message.msg}, got message from you!", self)
            Thread.sleep(1000L)
        case s: String => println(s"got message: $s")
        case _ => throw new Exception("got unknown message")
    }
}

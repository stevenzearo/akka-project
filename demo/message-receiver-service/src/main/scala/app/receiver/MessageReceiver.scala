package app.receiver

import akka.actor.{Actor, ActorRef, Props}
import app.receiver.api.{CloseMessage, ConnectionMessage, OpenMessage}

class MessageReceiver(val connectionRef: ActorRef) extends Actor {
    override def receive: Receive = {
        case msg: OpenMessage => send(self.path.toSerializationFormat, msg.fromPath, msg)
        case msg: ConnectionMessage => send(self.path.toSerializationFormat, msg.fromPath, msg)
        case msg: CloseMessage => send(self.path.toSerializationFormat, msg.fromPath, msg)
        case _ => println("unknown message!")
    }

    private def send(selfPath: String, fromPath: String, msg: Any): Unit = {
        println(s"$selfPath get message from $fromPath")
        connectionRef ! msg
    }
}

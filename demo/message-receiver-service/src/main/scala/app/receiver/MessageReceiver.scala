package app.receiver

import akka.actor.Actor
import app.receiver.api.{CloseMessage, ConnectionMessage, OpenMessage}

class MessageReceiver extends Actor {
    val connectionContext: ConnectionContext = new ConnectionContext()

    override def receive: Receive = {
        case msg: OpenMessage =>
            println(s"get open message from ${msg.from.path}")
            connectionContext.connect(msg.from)
        case msg: ConnectionMessage =>
            println(s"receive message from ${msg.from.path}")
            val maybeRef = connectionContext.connectionMap.get(msg.to.path.name)
            if (maybeRef.isDefined) maybeRef.get ! ConnectionMessage(from = msg.from, to = msg.to, content = msg.content)

        case msg: CloseMessage =>
            println(s"get close message from ${msg.from.path}")
            connectionContext.close(msg.from)
        case _ =>
    }
}

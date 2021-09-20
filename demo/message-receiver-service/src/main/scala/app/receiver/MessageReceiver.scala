package app.receiver

import akka.actor.Actor
import app.receiver.api.{CloseMessage, ConnectionMessage, OpenMessage}

import scala.sys.exit

class MessageReceiver extends Actor {
    val connectionContext: ConnectionContext = new ConnectionContext()

    override def receive: Receive = {
        case msg: OpenMessage =>
            println(s"get open message from ${msg.fromPath}")
            connectionContext.connect(context.system.actorSelection(msg.fromPath))
        case msg: ConnectionMessage =>
            println(s"receive message from ${msg.fromPath}")
            if (connectionContext.connectionMap.isDefinedAt(msg.fromPath)) {
                println(s"receive message from ${msg.fromPath} and send to ${msg.toPath}")
                connectionContext.send(msg.fromPath, msg.toPath, msg.content)
            } else {
                println("actorRef not found!")
            }
        case msg: CloseMessage =>
            println(s"get close message from ${msg.fromPath}")
            connectionContext.close(msg.fromPath)
            if (connectionContext.connectionMap.isEmpty) exit()
        case _ => println("unknown message!")
    }
}

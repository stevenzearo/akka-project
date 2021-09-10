package app.sender

import akka.actor.{Actor, ActorSelection}
import akka.serialization.Serialization
import app.receiver.api.{CloseMessage, ConnectionMessage}

import scala.sys.exit

class MessageSender extends Actor {
    override def receive: Receive = {
        case msg: ConnectionMessage =>
            println(s"get message from ${msg.fromPath}: ${msg.content}")
            print(s"Reply to ${msg.fromPath}:")
            val content = Console.in.readLine()
            val server: ActorSelection = context.system.actorSelection("akka://message-receiver@192.168.55.19:25520/user/receiver")
            if ("exit".equalsIgnoreCase(content)) {
                server ! new CloseMessage(Serialization.serializedActorPath(this.self))
                exit()
            } else {
                server ! new ConnectionMessage(Serialization.serializedActorPath(this.self), msg.toPath, content)
            }
        case _ => println("unknown message!")
    }
}

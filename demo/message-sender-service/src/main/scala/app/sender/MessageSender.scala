package app.sender

import akka.actor.Actor
import app.receiver.api.ConnectionMessage

class MessageSender extends Actor{
    override def receive: Receive = {
        case msg: ConnectionMessage =>
            println(s"get message from ${msg.from.path.name}: ${msg.content}")
            print(s"Reply to ${msg.from.path.name}:")
            val content = Console.in.readLine()
            msg.from ! ConnectionMessage(from = this.self, to = msg.from, content=content)
    }
}

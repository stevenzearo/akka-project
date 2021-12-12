package app.sender

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.serialization.Serialization
import app.receiver.api.{CloseMessage, ConnectionMessage}

import scala.sys.exit

class MessageSender(server: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case msg: ConnectionMessage =>
      log.info(s"get message from ${msg.fromPath}: ${msg.content}")
      log.info(s"Reply to ${msg.fromPath}:")
      val content = Console.in.readLine()
      if ("exit".equalsIgnoreCase(content)) {
        server ! new CloseMessage(Serialization.serializedActorPath(this.self))
        exit()
      } else {
        server ! new ConnectionMessage(Serialization.serializedActorPath(this.self), msg.toPath, content)
      }
    case _ => log.error("unknown message!")
  }
}

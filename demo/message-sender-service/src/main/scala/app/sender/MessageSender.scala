package app.sender

import akka.actor.{ActorLogging, ActorRef}
import akka.persistence.{AtLeastOnceDelivery, PersistentActor, RecoveryCompleted}
import akka.serialization.Serialization
import app.receiver.api.{CloseMessage, ConnectionMessage}

import scala.sys.exit

class MessageSender(server: ActorRef) extends PersistentActor with AtLeastOnceDelivery with ActorLogging {
  var state: ConnectionMessage = _

  override def receive: Receive = {
    case msg: ConnectionMessage =>
      log.info(s"get message from ${msg.fromPath}: ${msg.content}")
      // todo need to check logic
      persist[ConnectionMessage](msg)(msg => {
        state = msg
      })
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

  override def receiveRecover: Receive = {
    case msg: ConnectionMessage => state = msg
    case RecoveryCompleted => log.info(s"actor-$persistenceId recovery complete")
  }

  override def receiveCommand: Receive = receive

  override def persistenceId: String = "sender-0001"
}

package app.sender

import akka.actor.{ActorLogging, ActorRef}
import akka.persistence.{AtLeastOnceDelivery, PersistentActor, RecoveryCompleted}
import akka.serialization.Serialization
import app.protocol.message.{CloseMessage, ConnectionMessage, Message, OpenMessage}

import scala.sys.exit

class MessageSender(server: ActorRef) extends PersistentActor with AtLeastOnceDelivery with ActorLogging {
  var state: ConnectionMessage = _
  private val selfPath: String = Serialization.serializedActorPath(this.self)

  override def receive: Receive = {
    case "start" =>
      server ! OpenMessage(selfPath)
      println("Enter message to send:\n")
      val content = Console.in.readLine()
      server ! consoleInputToMessage(content, selfPath)

    case msg: ConnectionMessage =>
      log.info(s"get message from ${msg.fromPath}: ${msg.content}")
      // todo need to check logic
      persist[ConnectionMessage](msg)(msg => {
        state = msg
      })
      println(s"Reply to ${msg.fromPath}:\n")

      val content = Console.in.readLine()
      server ! consoleInputToMessage(content, msg.fromPath)
    case _ => log.error("unknown message!")
  }

  def consoleInputToMessage(input: String, toPath: String): Message = input match  {
    case "exit" =>
      server ! CloseMessage(selfPath)
      exit()
    case _ =>
      ConnectionMessage(selfPath, toPath, input)
  }
  override def receiveRecover: Receive = {
    case msg: ConnectionMessage => state = msg
    case RecoveryCompleted => log.info(s"actor-$persistenceId recovery complete")
  }

  override def receiveCommand: Receive = receive

  override def persistenceId: String = "sender-0001"
}

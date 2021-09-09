package app.receiver.api

import akka.actor.ActorRef
import app.receiver.api.MessageTypes.MessageType

abstract class Message(from: ActorRef)

object MessageTypes extends Enumeration with Serializable {
    type MessageType = Value
    val CONNECTED = Value("CONNECTED")
    val OPEN = Value("OPEN")
    val CLOSED = Value("CLOSED")
}
case class OpenMessage(messageType: MessageType = MessageTypes.OPEN, from: ActorRef) extends Message(from)
case class CloseMessage(messageType: MessageType = MessageTypes.CLOSED, from: ActorRef) extends Message(from)
case class ConnectionMessage(messageType: MessageType = MessageTypes.CONNECTED, from: ActorRef, to: ActorRef, content: Any) extends Message(from)

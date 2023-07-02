package app.receiver.protocol

import app.receiver.protocol.MessageTypes.MessageType

object MessageTypes extends Enumeration with Serializable {
  type MessageType = Value
  val CONNECTED = Value("CONNECTED")
  val OPEN = Value("OPEN")
  val CLOSED = Value("CLOSED")
}
class Message(fromPath: String, messageType: MessageType) extends Serializable
case class OpenMessage(fromPath: String, messageType: MessageType = MessageTypes.OPEN) extends Message(fromPath, messageType)
case class CloseMessage(fromPath: String, messageType: MessageType = MessageTypes.CLOSED) extends Message(fromPath, messageType)

case class ConnectionMessage(fromPath: String, toPath: String, content: Any, messageType: MessageType = MessageTypes.CLOSED) extends Message(fromPath, messageType)
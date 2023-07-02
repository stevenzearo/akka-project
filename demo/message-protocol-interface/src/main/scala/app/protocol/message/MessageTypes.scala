package app.protocol.message

import app.protocol.message.MessageTypes.MessageType

object MessageTypes extends Enumeration with Serializable {
  type MessageType = Value
  val CONNECTED: MessageTypes.Value = Value("CONNECTED")
  val OPEN: MessageTypes.Value = Value("OPEN")
  val CLOSED: MessageTypes.Value = Value("CLOSED")
}

class Message(fromPath: String, messageType: MessageType) extends Serializable

case class OpenMessage(fromPath: String, messageType: MessageType = MessageTypes.OPEN) extends Message(fromPath, messageType)

case class CloseMessage(fromPath: String, messageType: MessageType = MessageTypes.CLOSED) extends Message(fromPath, messageType)

case class ConnectionMessage(fromPath: String, toPath: String, content: Any, messageType: MessageType = MessageTypes.CONNECTED) extends Message(fromPath, messageType)

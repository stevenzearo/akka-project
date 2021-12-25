package app.receiver.protocol

import app.receiver.protocol.MessageTypes.MessageType

object MessageTypes extends Enumeration with Serializable {
  type MessageType = Value
  val CONNECTED = Value("CONNECTED")
  val OPEN = Value("OPEN")
  val CLOSED = Value("CLOSED")
}

case class OpenMessage() extends Serializable {
  val messageType: MessageType = MessageTypes.OPEN
  var fromPath: String = _

  def this(fromPath: String) = {
    this()
    this.fromPath = fromPath
  }

  def apply(fromPath: String): OpenMessage = new OpenMessage(fromPath)
}

case class CloseMessage() extends Serializable {
  val messageType: MessageType = MessageTypes.CLOSED
  var fromPath: String = _

  def this(fromPath: String) = {
    this()
    this.fromPath = fromPath
  }

  def apply(fromPath: String): CloseMessage = new CloseMessage(fromPath)
}

case class ConnectionMessage() extends Serializable {
  val messageType: MessageType = MessageTypes.CONNECTED
  var fromPath: String = _
  var toPath: String = _
  var content: Any = _

  def this(fromPath: String) = {
    this()
    this.fromPath = fromPath
  }

  def this(fromPath: String, toPath: String, content: Any) = {
    this(fromPath)
    this.toPath = toPath
    this.content = content
  }

  def apply(fromPath: String, toPath: String, content: Any): ConnectionMessage = new ConnectionMessage(fromPath, toPath, content)
}

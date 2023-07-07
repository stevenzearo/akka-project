package app.persistent.demo

import app.persistent.demo.ChatActorState.ChatActorState

case class ChatMessage(from: String, to: String, content: String)

object ChatActorState extends Enumeration {
  type ChatActorState = Value
  val OPEN: ChatActorState.Value = Value("OPEN")
  val CLOSE: ChatActorState.Value = Value("CLOSE")
  val RUNNING: ChatActorState.Value = Value("RUNNING")
}

case class ChatState(self: String, state: ChatActorState, cachedMessages: List[ChatMessage])

abstract class ChatCmd() {
  def toEvent(id: String): ChatEvent
}

case class OpenCmd() extends ChatCmd {
  override def toEvent(id: String): ChatEvent = ChatEvent(id, this, None)
}

case class CloseCmd() extends ChatCmd {
  override def toEvent(id: String): ChatEvent = ChatEvent(id, this, None)
}

case class ReceiveMessageCmd(msg: ChatMessage) extends ChatCmd {
  override def toEvent(id: String): ChatEvent = ChatEvent(id, this, msg)
}

case class ChatEvent(id: String, chatCmd: ChatCmd, payload: AnyRef)

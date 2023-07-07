package app.persistent.demo

import akka.persistence.{PersistentActor, Recovery, SnapshotOffer, SnapshotSelectionCriteria}

import java.util.UUID

class ChatPersistentActor(name: String, id: String) extends PersistentActor {
  var state: ChatState = ChatState("test-chat-actor", ChatActorState.CLOSE, List.empty[ChatMessage])

  override def preStart(): Unit = {
    println("starting...")
    super.preStart()
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("restarting...")
    super.preRestart(reason, message)
  }

  override def receiveRecover: Receive = {
    case event: ChatEvent => updateState(event.chatCmd)
    case SnapshotOffer(_, snapshot: ChatState) => state = snapshot
  }

  val snapShotInterval = 10

  override def receiveCommand: Receive = {
    case chatCmd: ChatCmd => {
      persist(chatCmd.toEvent(UUID.randomUUID().toString)) { event =>
        updateState(event.chatCmd)
        context.system.eventStream.publish(event)
        if (lastSequenceNr % snapShotInterval == 0 && lastSequenceNr != 0)
          saveSnapshot(state)
      }
    }
  }

  private def updateState(chatCmd: ChatCmd): Unit = {
    chatCmd match {
      case _: OpenCmd =>
        state = state.copy(state.self, ChatActorState.OPEN, state.cachedMessages)
      case _: CloseCmd =>
        state = state.copy(state.self, ChatActorState.CLOSE, state.cachedMessages)
      case ReceiveMessageCmd(msg) =>
        if (state.state == ChatActorState.CLOSE) return
        state = state.copy(state.self, ChatActorState.RUNNING, pushMessage(msg, state.cachedMessages))
    }
  }

  def pushMessage(msg: ChatMessage, originalMessages: List[ChatMessage]): List[ChatMessage] = {
    val newList = msg :: originalMessages
    val messages = new Array[ChatMessage](10)
    val bound = if (messages.length > newList.length) newList.length else messages.length
    for (i <- 0 until bound) {
      messages(i) = newList(i)
    }
    messages.toList
  }

  override def persistenceId: String = id

  override def recovery: Recovery = Recovery.create(SnapshotSelectionCriteria.Latest)
}
package app.receiver

import akka.actor.{Actor, ActorLogging, ActorSelection, Props, Stash}
import app.receiver.ConnectionContext.{AvailableMessage, RecoveringMessage}
import app.receiver.api.{CloseMessage, ConnectionMessage, OpenMessage}

import scala.collection.mutable
import scala.sys.exit

class ConnectionContext extends Actor with ActorLogging with Stash {
    val connectionMap: mutable.HashMap[String, ActorSelection] = mutable.HashMap.empty // todo these data should store in db

    override def receive: Receive = {
        case msg: OpenMessage =>
            log.info(s"${self.path.toSerializationFormat} get open message from ${msg.fromPath}")
            connect(context.system.actorSelection(msg.fromPath))
        case msg: ConnectionMessage =>
            log.info(s"${self.path.toSerializationFormat} receive message from ${msg.fromPath}")
            if (connectionMap.isDefinedAt(msg.fromPath)) {
                log.info(s"${self.path.toSerializationFormat} receive message from ${msg.fromPath} and send to ${msg.toPath}")
                send(msg.fromPath, msg.toPath, msg.content)
            } else {
                log.error("actorRef not found!")
            }
        case msg: CloseMessage =>
            log.info(s"${self.path.toSerializationFormat} get close message from ${msg.fromPath}")
            close(msg.fromPath)
            if (connectionMap.isEmpty) exit()
        case _ => log.error("unknown message!")
    }

    def connect(actorSelection: ActorSelection): Unit = {
        connectionMap.put(actorSelection.toSerializationFormat, actorSelection)
        log.info(s"${actorSelection.pathString} connect to server")
    }

    def send(fromPath: String, toPath: String, content: Any): Unit = {
        val maybeRef = connectionMap.get(toPath)
        if (maybeRef.isDefined) maybeRef.get ! new ConnectionMessage(fromPath, toPath, content)
    }

    def handle: Receive = {
        case AvailableMessage =>
            unstashAll()
            context.become(handle)
        case RecoveringMessage => context.become(recovering)

    }

    def recovering: Receive = {
        case _ => stash()
    }

    def close(path: String): Unit = {
        connectionMap.remove(path)
        log.info(s"$path disconnect to server")
    }
}

object ConnectionContext {
    final case object AvailableMessage
    final case object RecoveringMessage
    final case class Message()

    def connectionContextProps: Props = Props[ConnectionContext]()
}

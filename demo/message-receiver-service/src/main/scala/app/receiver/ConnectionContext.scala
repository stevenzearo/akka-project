package app.receiver

import akka.actor.ActorSelection
import app.receiver.api.ConnectionMessage

import scala.collection.mutable

class ConnectionContext {
    val connectionMap: mutable.HashMap[String, ActorSelection] = mutable.HashMap.empty

    def connect(actorSelection: ActorSelection): Unit = {
        connectionMap.put(actorSelection.toSerializationFormat, actorSelection)
        println(s"${actorSelection.pathString} connect to server")
    }

    def send(fromPath: String, toPath: String, content: Any): Unit = {
        val maybeRef = connectionMap.get(toPath)
        if (maybeRef.isDefined) maybeRef.get ! new ConnectionMessage(fromPath, toPath, content)
    }

    def close(path: String): Unit = {
        connectionMap.remove(path)
        println(s"$path disconnect to server")
    }
}

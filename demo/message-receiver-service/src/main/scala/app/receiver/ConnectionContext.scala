package app.receiver

import akka.actor.ActorRef

import scala.collection.mutable

class ConnectionContext {
    val connectionMap: mutable.HashMap[String, ActorRef] = mutable.HashMap.empty

    def connect(actorRef: ActorRef): Unit = {
        connectionMap.put(actorRef.path.name, actorRef)
        println(s"${actorRef.path.name} connect to server" )
    }

    def send(actorRef: ActorRef, msg: Any): Unit = {
        val maybeRef = connectionMap.get(actorRef.path.name)
        if (maybeRef.isDefined) maybeRef.get ! msg
    }

    def close(actorRef: ActorRef): Unit = {
        connectionMap.remove(actorRef.path.name)
        println(s"${actorRef.path.name} disconnect to server" )
    }
}

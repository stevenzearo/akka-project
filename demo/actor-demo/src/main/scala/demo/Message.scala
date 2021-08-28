package demo

import akka.actor.ActorRef

final case class Message(msg: String, from: ActorRef)

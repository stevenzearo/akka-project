package demo

import akka.actor.ActorRef

sealed trait MessageProtocol

final case class RequestMessage(msg: String, from: ActorRef) extends MessageProtocol

final case class ResponseMessage(msg: String, to: ActorRef) extends MessageProtocol

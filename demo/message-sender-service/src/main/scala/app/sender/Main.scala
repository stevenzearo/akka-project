package app.sender

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.event.LoggingAdapter
import akka.routing.FromConfig
import akka.serialization.Serialization
import app.receiver.protocol.{ConnectionMessage, OpenMessage}
import com.typesafe.config.{Config, ConfigFactory}

import java.io.File


object Main extends App {
  private val file: File = new File("./demo/message-sender-service/src/main/resources/application.conf")
  private val value: Config = ConfigFactory.parseFile(file)
  private val system: ActorSystem = ActorSystem("message-sender", value)
  private val log: LoggingAdapter = system.log
  private val receiverRef: ActorRef = system.actorOf(FromConfig.props(), "receiver")
  private val senderRef: ActorRef = system.actorOf(Props(classOf[MessageSender], receiverRef), "sender")
  log.info(senderRef.path.toString)
  private val senderPath: String = Serialization.serializedActorPath(senderRef)
  log.info(senderPath)
  receiverRef ! OpenMessage(senderPath)
  senderRef ! new ConnectionMessage(senderPath, senderPath, "hello")
}

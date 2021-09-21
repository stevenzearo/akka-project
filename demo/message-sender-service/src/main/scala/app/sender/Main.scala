package app.sender

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.FromConfig
import akka.serialization.Serialization
import app.receiver.api.{ConnectionMessage, OpenMessage}
import com.typesafe.config.{Config, ConfigFactory}

import java.io.File


object Main extends App {
    private val file: File = new File("./demo/message-sender-service/src/main/resources/application.conf")
    private val value: Config = ConfigFactory.parseFile(file)
    private val system: ActorSystem = ActorSystem("message-sender", value)
    private val receiverRef: ActorRef = system.actorOf(FromConfig.props(), "receiver")
    private val senderRef: ActorRef = system.actorOf(Props(classOf[MessageSender], receiverRef), "sender")
    println(senderRef.path)
    private val senderPath: String = Serialization.serializedActorPath(senderRef)
    println(senderPath)
    receiverRef ! new OpenMessage(senderPath)
    senderRef ! new ConnectionMessage(senderPath, senderPath, "hello")
}

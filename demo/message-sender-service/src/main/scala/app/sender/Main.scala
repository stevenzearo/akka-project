package app.sender

import akka.actor.{ActorRef, ActorSelection, ActorSystem, Props}
import akka.serialization.Serialization
import app.receiver.api.{ConnectionMessage, OpenMessage}
import com.typesafe.config.{Config, ConfigFactory}

import java.io.File


object Main extends App {
    private val file: File = new File("./demo/message-sender-service/src/main/resources/properties.yaml")
    private val value: Config = ConfigFactory.parseFile(file)
    private val system: ActorSystem = ActorSystem("message-sender", value)
    private val server: ActorSelection = system.actorSelection("akka://message-receiver@192.168.55.19:25520/user/receiver")
    private val senderRef: ActorRef = system.actorOf(Props[MessageSender](), "sender")
    println(senderRef.path)
    private val senderPath: String = Serialization.serializedActorPath(senderRef)
    println(senderPath)
    server ! new OpenMessage(senderPath)
    senderRef ! new ConnectionMessage(senderPath, senderPath, "hello")
}

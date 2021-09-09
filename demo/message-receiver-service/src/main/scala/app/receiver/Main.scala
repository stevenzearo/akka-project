package app.receiver

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

import java.io.File

object Main extends App {
    private val file: File = new File("./demo/message-receiver-service/src/main/resources/properties.yaml")
    private val value: Config = ConfigFactory.parseFile(file)
    private val system: ActorSystem = ActorSystem("message-receiver", value)
    private val receiverRef: ActorRef = system.actorOf(Props[MessageReceiver](), "receiver")
    println(receiverRef.path)
}

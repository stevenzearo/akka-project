package app.sender

import akka.actor.{ActorRef, ActorSelection, ActorSystem, Props}
import app.receiver.api.{CloseMessage, ConnectionMessage, OpenMessage}
import com.typesafe.config.{Config, ConfigFactory}

import java.io.File


object Main extends App {
    private val file: File = new File("./demo/message-sender-service/src/main/resources/properties.yaml")
    private val value: Config = ConfigFactory.parseFile(file)
    private val system: ActorSystem = ActorSystem("message-receiver", value)
    private val senderRef: ActorRef = system.actorOf(Props[MessageSender](), "sender")
    println(senderRef.path)
    private val server: ActorSelection = system.actorSelection("akka://message-receiver@192.168.55.19:25520/user/receiver")
    server ! OpenMessage(from = senderRef)
    server ! ConnectionMessage(from = senderRef, to = senderRef, content = "hello")
    var isClose = false
    while (!isClose) {
        print("Print Exit to exit:")
        val msg = Console.in.readLine()
        isClose = "exit".equalsIgnoreCase(msg)
    }
    senderRef ! CloseMessage(from = senderRef)
}

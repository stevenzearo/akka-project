package app.receiver

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.FromConfig
import com.typesafe.config.{Config, ConfigFactory}

import java.io.File

object Main extends App {
    private val file: File = new File("./demo/message-receiver-service/src/main/resources/application.conf")
    private val value: Config = ConfigFactory.parseFile(file)
    private val system: ActorSystem = ActorSystem("message-receiver", value)
    val connectionRef: ActorRef = system.actorOf(Props[ConnectionContext](), "connection-context")
    private val receiverProps = Props(classOf[MessageReceiver], connectionRef)
    private val routerProps: Props = FromConfig.props(receiverProps)
    private val receiverRef: ActorRef = system.actorOf(routerProps, "receiver")
    println(receiverRef.path)
}

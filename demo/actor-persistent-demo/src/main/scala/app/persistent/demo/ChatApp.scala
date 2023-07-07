package app.persistent.demo

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout

import java.time.LocalDateTime
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt

/**
 * @author Steve Zou
 */
object ChatApp extends App {
  private val system: ActorSystem = ActorSystem.create("chat-system")
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = 3.seconds
  private val chatProps: Props = Props.create(classOf[ChatPersistentActor], "chat-actor", "chat-actor-001")
  private val chatActorRef: ActorRef = system.actorOf(chatProps, "chat-actor")
  chatActorRef ! OpenCmd
  sendMsg()

  private def sendMsg(): Unit = {
    var count = 0
    var colse = false
    while (true) {
      count += 1
      chatActorRef ! ReceiveMessageCmd(ChatMessage("system", "chat-actor", s"hello, time:${LocalDateTime.now()}"))
      Thread.sleep(1000)
      if (count >= 10 && !colse) {
        chatActorRef ! CloseCmd()
        colse = true
      }
    }
  }
}

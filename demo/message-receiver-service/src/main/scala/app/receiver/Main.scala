package app.receiver

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.event.LoggingAdapter
import akka.routing.FromConfig

object Main extends App {
  //  private val file: File = new File("./demo/message-receiver-service/src/main/resources/application.conf")
  //  private val value: Config = ConfigFactory.parseFile(file)
  private val system: ActorSystem = ActorSystem("message-receiver") // will automatically load application.conf
  val connectionRef: ActorRef = system.actorOf(ConnectionContext.connectionContextProps, "connection-context")
  private val receiverProps = Props(classOf[MessageReceiver], connectionRef)
  // config file def of route strategy, pool strategy will create a parent actor as supervisor
  private val routerProps: Props = FromConfig.props(receiverProps)

  private val receiverRef: ActorRef = system.actorOf(routerProps, "receiver")
  // code def of route strategy
  //    private val receiverRef: ActorRef = system.actorOf(RoundRobinPool(5).props(receiverProps), "receiver")
  /*val paths = List(
      "receiver1",
      "receiver2",
      "receiver3")
  private val actorPaths: List[String] = paths.map(p => system.actorOf(receiverProps, p).path.toString) // todo
  private val receiverRef: ActorRef = system.actorOf(RoundRobinGroup(actorPaths).props(), "receiver") // todo
*/
  private val log: LoggingAdapter = system.log
  log.info(receiverRef.path.toSerializationFormat)
}

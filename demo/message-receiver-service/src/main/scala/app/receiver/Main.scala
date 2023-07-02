package app.receiver

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.event.LoggingAdapter
import akka.http.scaladsl.Http
import akka.routing.FromConfig
import app.receiver.api.MessageConnectionWebServiceImpl
import app.receiver.service.{ConnectionContext, MessageReceiver}
import com.typesafe.config.{Config, ConfigFactory}

import java.io.File
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

//  private val file: File = new File("./demo/message-receiver-service/src/main/resources/application.conf")
//  private val value: Config = ConfigFactory.parseFile(file)
// config file def of route strategy, pool strategy will create a parent actor as supervisor
// code def of route strategy
//    private val receiverRef: ActorRef = system.actorOf(RoundRobinPool(5).props(receiverProps), "receiver")
/*val paths = List(
    "receiver1",
    "receiver2",
    "receiver3")
private val actorPaths: List[String] = paths.map(p => system.actorOf(receiverProps, p).path.toString) // todo
private val receiverRef: ActorRef = system.actorOf(RoundRobinGroup(actorPaths).props(), "receiver") // todo
*/

object Main extends App {
  private val file: File = new File("./demo/message-receiver-service/src/main/resources/application.conf")
  private val value: Config = ConfigFactory.parseFile(file)
  private implicit val system: ActorSystem = ActorSystem("message-receiver", value) // will automatically load application.conf
  private implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  val connectionRef: ActorRef = system.actorOf(ConnectionContext.connectionContextProps, "connection-context")
  private val receiverProps = Props(classOf[MessageReceiver], connectionRef)
  private val routerProps: Props = FromConfig.props(receiverProps)
  private val receiverRef: ActorRef = system.actorOf(routerProps, "receiver")
  private val log: LoggingAdapter = system.log
  log.info(receiverRef.path.toSerializationFormat)
  private val messageConnectionWebService = new MessageConnectionWebServiceImpl()
  private val bindingFuture: Future[Http.ServerBinding] = Http().newServerAt("localhost", 6060).bind(messageConnectionWebService.routes)
  println(s"Server now online. Please navigate to http://localhost:6060/connection\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}

package app.receiver.api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.{MethodDirectives, PathDirectives, RouteDirectives}
import akka.pattern.AskSupport
import app.receiver.api.connection.{ConnectionResponse, ListConnectionResponse}
import play.api.libs.json.{Json, OWrites}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Random

class MessageConnectionWebServiceImpl(implicit ec: ExecutionContext) extends MessageConnectionWebService with AskSupport {
  implicit final val connectionResponseWriter: OWrites[ConnectionResponse] = Json.writes[ConnectionResponse]
  implicit final val listConnectionResponseWriter: OWrites[ListConnectionResponse] = Json.writes[ListConnectionResponse]

  override def listConnections(): ListConnectionResponse = {
    new ListConnectionResponse(List(new ConnectionResponse("test-0001", "/from", "/to")))
  }

  def routes: Route = PathDirectives.path("connection") {
    val resultFuture: Future[String] = futureResponse
    MethodDirectives.get {
      RouteDirectives.complete({
        try {
          val str = Await.result(resultFuture, 2.seconds)
          HttpEntity(ContentTypes.`application/json`, str)
        } catch {
          case e: Throwable => StatusCodes.ServerError(500)("SERVER_ERROR", e.getMessage)
        }
      })
    }
  }

  // mock random time out
  private def futureResponse: Future[String] = {
    val resultFuture = Future[ListConnectionResponse] {
      if (Random.nextBoolean()) Thread.sleep(3000)
      listConnections()
    }.map(value => {
      Json.toJson(value)(listConnectionResponseWriter).toString()
    })
    resultFuture
  }
}
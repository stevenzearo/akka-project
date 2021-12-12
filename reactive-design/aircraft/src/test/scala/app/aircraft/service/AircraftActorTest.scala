package app.aircraft.service

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import app.aircraft.domain.AircraftProtocol._
import app.aircraft.domain.{Passenger, Weather}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.{Test, TestInstance}

import scala.concurrent.duration.DurationInt

@Test
@TestInstance(Lifecycle.PER_CLASS)
class AircraftActorTest extends TestKit(ActorSystem()) with ImplicitSender {
  @Test
  def changeAltitudeTest(): Unit = {
    val id = "aircraft-0001"
    val callSign = "callSign-0001"
    val altitude = 0
    val speed = 0
    val heading = 0
    val passengers = List()
    val weathers = List()
    val props = AircraftActor.props(id, callSign, altitude, speed, heading, passengers, weathers)
    val actorRef = system.actorOf(props)

    actorRef ! ChangeAltitude(1.5)
    expectMsg(3.seconds, Ok)

    actorRef ! ChangeSpeed(2.5)
    expectMsg(3.seconds, Ok)

    actorRef ! ChangeHeading(1.2)
    expectMsg(3.seconds, Ok)

    actorRef ! BoardPassenger(Passenger("passenger-0001", "steve", 1, "seat-0001"))
    expectMsg(3.seconds, Ok)

    actorRef ! AddWeather(Weather(1.5, 1.2, 1.3, 2, 3, 23.5, 8.5, 0.95, 3.5, 1.5))
    expectMsg(3.seconds, Ok)
  }
}
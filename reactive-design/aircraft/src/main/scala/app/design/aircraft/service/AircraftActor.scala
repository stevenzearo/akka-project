package app.design.aircraft.service

import akka.actor.Actor

class AircraftActor() extends Actor{
  private var currentState: AircraftActor = _
  import app.design.aircraft.domain.AircraftProtocol._
  override def receive: Receive = {
    case ChangeAltitude =>
    case ChangeHeading =>
    case ChangeSpeed =>
    case BoardPassenger =>
    case AddWeather =>
  }
}

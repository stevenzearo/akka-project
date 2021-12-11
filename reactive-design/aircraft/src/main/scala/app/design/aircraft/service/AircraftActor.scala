package app.design.aircraft.service

import akka.actor.Actor
import app.design.aircraft.domain.{Aircraft, Passenger, Weather}

class AircraftActor(id: String,
                    callSign: String,
                    altitude: Double,
                    speed: Double,
                    heading: Double,
                    passengers: List[Passenger],
                    weathers: List[Weather]) extends Actor {

  import app.design.aircraft.domain.AircraftProtocol._

  private var currentState: Aircraft = Aircraft(id, callSign, altitude, speed, heading, passengers, weathers)

  override def receive: Receive = {
    case ChangeAltitude(altitude) =>
      currentState = currentState.copy(altitude = altitude)
      sender() ! Ok
    case ChangeHeading(heading) =>
      currentState = currentState.copy(heading = heading)
      sender() ! Ok
    case ChangeSpeed(speed) =>
      currentState = currentState.copy(speed = speed)
      sender() ! Ok
    case BoardPassenger(passenger) =>
      currentState = currentState.copy(passengers = currentState.passengers :+ passenger)
      sender() ! Ok
    case AddWeather(weather) =>
      currentState = currentState.copy(weathers = currentState.weathers :+ weather)
      sender() ! Ok
  }
}

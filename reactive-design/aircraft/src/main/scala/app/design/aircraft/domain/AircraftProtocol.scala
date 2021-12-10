package app.design.aircraft.domain

object AircraftProtocol {

  sealed trait AircraftProtocolMessage

  final case class ChangeAltitude(altitude: Double) extends AircraftProtocolMessage

  final case class ChangeSpeed(speed: Double) extends AircraftProtocolMessage

  final case class ChangeHeading(speed: Double) extends AircraftProtocolMessage

  final case class BoardPassenger(passenger: Passenger) extends AircraftProtocolMessage

  final case class AddWeather(weather: Weather) extends AircraftProtocolMessage

  final case object Ok

}

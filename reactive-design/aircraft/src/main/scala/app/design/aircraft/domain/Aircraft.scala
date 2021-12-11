package app.design.aircraft.domain

final case class Aircraft(
                           id: String,
                           callSign: String,
                           altitude: Double,
                           speed: Double,
                           heading: Double,
                           passengers: List[Passenger],
                           weathers: List[Weather]
                         )

package app.aircraft.domain

case class Weather(altitude: Double,
                   longitude: Double,
                   latitude: Double,
                   floor: Double,
                   celling: Double,
                   temperature: Double,
                   visibility: Double,
                   precipitation: Double,
                   windSpeed: Double,
                   windDirection: Double)

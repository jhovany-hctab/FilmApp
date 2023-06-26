package org.bedu.filmapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val name: String
)

@Serializable
data class Weather(
    val main: String,
    val description: String
)

@Serializable
data class Main(
    val temp: Double,
    val humidity: Int
)

package org.bedu.filmapp.domain.use_cases.user

import org.bedu.filmapp.domain.repository.UserRepository
import javax.inject.Inject

class WeatherTime @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(lat: Double, lon: Double) = repository.weather(lat, lon)
}
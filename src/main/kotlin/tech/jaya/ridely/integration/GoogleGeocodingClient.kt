package tech.jaya.ridely.integration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class GoogleGeocodingClient(
    @Value("\${google.maps.api.key}") private val apiKey: String,
    private val restTemplate: RestTemplate
) : GeocodingGateway {

    override fun reverseGeocode(lat: Double, lng: Double): String {
        val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=$lat,$lng&key=$apiKey"
        val response = restTemplate.getForObject(url, GoogleGeocodeResponse::class.java)
        return response?.results?.firstOrNull()?.formatted_address ?: "Address not found"
    }
}

data class GoogleGeocodeResponse(
    val results: List<Result>
)

data class Result(
    val formatted_address: String
)
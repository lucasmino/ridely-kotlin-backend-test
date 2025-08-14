package tech.jaya.ridely.integration.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import tech.jaya.ridely.common.logging.Loggable
import tech.jaya.ridely.integration.gateway.GeocodingGateway
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Component
class GoogleGeocodingClient(
    @Value("\${google.maps.api.key}") private val apiKey: String,
    @Value("\${google.maps.api.url}") private val url: String,
    private val restTemplate: RestTemplate
) : GeocodingGateway, Loggable() {

    override fun reverseGeocode(lat: Double, lng: Double): String {
        val url = "$url+$apiKey"
        val response = restTemplate.getForObject(url, GoogleGeocodeResponse::class.java)
        val adress = response?.results?.firstOrNull()?.formatted_address ?: "Address not found"
        log.info("the adress formated is:"+adress)
        return adress
    }

    override fun geocodeAddress(address: String): Pair<Double, Double> {
        val encoded = URLEncoder.encode(address, StandardCharsets.UTF_8)
        val url = "https://maps.googleapis.com/maps/api/geocode/json?address=$encoded&key=$apiKey"
        log.info("Calling Google Maps Geocode API: $url")
        val response = restTemplate.getForObject(url, GoogleGeocodeResponse::class.java)
            ?: throw RuntimeException("Failed to call Google Geocoding API")


        val location = response.results.firstOrNull()?.geometry?.location
            ?: throw RuntimeException("Address not found")

        log.info("the location formated is:"+Pair(location.lat, location.lng))

        return Pair(location.lat, location.lng)
    }
    }


data class GoogleGeocodeResponse(
    val results: List<Result>
)

data class Result(
    val formatted_address: String,
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)
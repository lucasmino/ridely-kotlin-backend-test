package tech.jaya.ridely.integration.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import tech.jaya.ridely.dto.route.DirectionsResponse
import tech.jaya.ridely.dto.route.RouteInfo
import tech.jaya.ridely.integration.gateway.MapsGateway

@Service

class GoogleMapsClient(@Value("\${google.maps.api.key}")
                       private val apiKey: String,
                       private val restTemplate: RestTemplate
) : MapsGateway {
    override fun getRouteInfo(
        originLat: Double,
        originLon: Double,
        destLat: Double,
        destLon: Double
    ): RouteInfo {
        val url = UriComponentsBuilder
            .fromHttpUrl("https://maps.googleapis.com/maps/api/directions/json")
            .queryParam("origin", "$originLat,$originLon")
            .queryParam("destination", "$destLat,$destLon")
            .queryParam("key", apiKey)
            .build()
            .toUriString()

        val response = restTemplate.getForEntity(url, DirectionsResponse::class.java)

        val route = response.body?.routes?.firstOrNull()
            ?: throw IllegalStateException("No route found")

        val leg = route.legs.first()

        return RouteInfo(
            estimatedTimeMinutes = leg.duration.value / 60,
            distanceKm = leg.distance.value / 1000.0

        )
    }
}
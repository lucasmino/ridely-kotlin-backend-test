package tech.jaya.ridely.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.jaya.ridely.common.logging.Loggable
import tech.jaya.ridely.dto.trip.LatLng
import tech.jaya.ridely.dto.trip.RouteInfo
import tech.jaya.ridely.dto.trip.TripEstimationWithDriversResponse
import tech.jaya.ridely.integration.gateway.GeocodingGateway
import tech.jaya.ridely.integration.gateway.MapsGateway
import tech.jaya.ridely.service.trip.TripEstimationService

@RestController
@RequestMapping("/trips")
class TripController(
    private val mapsGateway: MapsGateway,
    private val geocodingGateway: GeocodingGateway,
    private val tripEstimationService: TripEstimationService
) : Loggable() {

    @GetMapping("/estimate")
    @PreAuthorize("hasRole('PASSENGER')")
    fun estimateTrip(
        @RequestParam originLat: Double,
        @RequestParam originLng: Double,
        @RequestParam destLat: Double,
        @RequestParam destLng: Double
    ): TripEstimationWithDriversResponse {
        val origin = LatLng(latitude = originLat, longitude = originLng)
        val dest = LatLng(latitude = destLat, longitude = destLng)

        log.info("Request to estimate trip from $origin to $dest")

        return tripEstimationService.estimateTrip(origin, dest)
    }


    @GetMapping("/route")
    fun getRoute(
        @RequestParam originLat: Double,
        @RequestParam originLng: Double,
        @RequestParam destLat: Double,
        @RequestParam destLng: Double
    ): RouteInfo {
        return mapsGateway.getRouteInfo(originLat, originLng, destLat, destLng)
    }

    @GetMapping("/geocode")
    fun geocodeAddress(@RequestParam address: String): ResponseEntity<Map<String, Any>> {
        val (lat, lng) = geocodingGateway.geocodeAddress(address)
        return ResponseEntity.ok(mapOf("lat" to lat, "lng" to lng))
    }
}


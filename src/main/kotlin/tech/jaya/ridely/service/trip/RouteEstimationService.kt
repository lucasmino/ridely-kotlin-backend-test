package tech.jaya.ridely.service.trip

import org.springframework.stereotype.Service
import tech.jaya.ridely.common.logging.Loggable
import tech.jaya.ridely.dto.trip.LatLng
import tech.jaya.ridely.dto.trip.TripEstimationResponse
import tech.jaya.ridely.integration.gateway.MapsGateway

@Service
class RouteEstimationService(
    private val gateway: MapsGateway,
    private val priceCalculatorService: PriceCalculatorService
) : Loggable() {
    fun estimateTrip(origin: LatLng, destination: LatLng): TripEstimationResponse {
        log.info("Estimating trip from origin=($origin) to destination=($destination)")
        return try {
            val routeInfo = gateway.getRouteInfo(
                originLat = origin.latitude,
                originLon = origin.longitude,
                destLat = destination.latitude,
                destLon = destination.longitude
            )
            val tripPrice = priceCalculatorService.calculatePrice(routeInfo)
            log.debug("Route info received: duration=${routeInfo.estimatedTimeMinutes} min, distance=${routeInfo.distanceKm} km, price=$tripPrice")
            TripEstimationResponse(
                routeInfo.estimatedTimeMinutes,
                routeInfo.distanceKm,
                tripPrice
            )
        } catch (ex: Exception) {
            log.error("Failed to estimate trip for origin=$origin and destination=$destination", ex)
            throw RuntimeException("Could not calculate trip estimation", ex)
        }

    }
}


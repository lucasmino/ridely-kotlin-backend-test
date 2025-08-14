package tech.jaya.ridely.service.ride

import org.springframework.stereotype.Service
import tech.jaya.ridely.common.logging.Loggable
import tech.jaya.ridely.dto.route.LatLng
import tech.jaya.ridely.dto.ride.RideEstimationResponse
import tech.jaya.ridely.integration.gateway.MapsGateway

@Service
class RideRouteEstimationService(
    private val gateway: MapsGateway,
    private val ridePriceCalculatorService: RidePriceCalculatorService
) : Loggable() {
    fun estimateRoute(origin: LatLng, destination: LatLng): RideEstimationResponse {
        log.info("Estimating route from origin=($origin) to destination=($destination)")
        return try {
            val routeInfo = gateway.getRouteInfo(
                originLat = origin.latitude,
                originLon = origin.longitude,
                destLat = destination.latitude,
                destLon = destination.longitude
            )
            val routePrice = ridePriceCalculatorService.calculatePrice(routeInfo)
            log.debug("Route info received: duration=${routeInfo.estimatedTimeMinutes} min, distance=${routeInfo.distanceKm} km, price=$routePrice")
            RideEstimationResponse(
                routeInfo.estimatedTimeMinutes,
                routeInfo.distanceKm,
                routePrice
            )
        } catch (ex: Exception) {
            log.error("Failed to estimate route for origin=$origin and destination=$destination", ex)
            throw RuntimeException("Could not calculate route estimation", ex)
        }
    }

}
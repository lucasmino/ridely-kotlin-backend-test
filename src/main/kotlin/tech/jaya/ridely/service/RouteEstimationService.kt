package tech.jaya.ridely.service

import org.springframework.stereotype.Service
import tech.jaya.ridely.dto.trip.LatLng
import tech.jaya.ridely.dto.trip.TripEstimationResponse
import tech.jaya.ridely.integration.MapsGateway

@Service
class RouteEstimationService(
    private val gateway: MapsGateway,
    private val priceCalculatorService: PriceCalculatorService
) {
    fun estimateTrip(origin: LatLng, destination: LatLng): TripEstimationResponse {
        val routeInfo = gateway.getRouteInfo(
            originLat = origin.latitude,
            originLon = origin.longitude,
            destLat = destination.latitude,
            destLon = destination.longitude
        )
        val tripPrice = priceCalculatorService.calculatePrice(routeInfo)
        return TripEstimationResponse(routeInfo.estimatedTimeMinutes, routeInfo.distanceKm, tripPrice)
    }


}
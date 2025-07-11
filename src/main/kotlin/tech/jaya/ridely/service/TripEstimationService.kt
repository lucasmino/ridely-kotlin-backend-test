package tech.jaya.ridely.service

import tech.jaya.ridely.dto.trip.LatLng
import tech.jaya.ridely.dto.trip.RouteInfo
import tech.jaya.ridely.dto.trip.TripEstimationResponse
import tech.jaya.ridely.dto.trip.TripEstimationWithDriversResponse

class TripEstimationService(
    private val routeEstimation: RouteEstimationService,
    private val findDriversNearService: FindDriversNearService,
    private val nearbyDriverAssemblerService: NearbyDriverAssemblerService,
    private val estimationPriceCalculatorService: PriceCalculatorService
) {

    fun estimateTrip(origin: LatLng, dest: LatLng): TripEstimationWithDriversResponse {
        val route = routeEstimation.estimateTrip(origin, dest)
        val driverLocations = findDriversNearService.findDriversNear(origin.latitude, origin.longitude)
        val drivers = nearbyDriverAssemblerService.retrieveNearbyDrivers(driverLocations)
        val price = estimationPriceCalculatorService.calculatePrice(
            RouteInfo(
                route.estimatedTimeMinutes,
                route.distanceKm
            )
        )

        return TripEstimationWithDriversResponse(
            trip = TripEstimationResponse(
                estimatedTimeMinutes = route.estimatedTimeMinutes,
                distanceKm = route.distanceKm,
                estimatedPrice = price
            ),
            nearbyDrivers = drivers,

            )
    }
}
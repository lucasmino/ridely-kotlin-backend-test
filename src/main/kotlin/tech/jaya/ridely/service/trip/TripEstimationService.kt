package tech.jaya.ridely.service.trip

import org.springframework.stereotype.Component
import tech.jaya.ridely.common.logging.Loggable
import tech.jaya.ridely.dto.trip.LatLng
import tech.jaya.ridely.dto.trip.RouteInfo
import tech.jaya.ridely.dto.trip.TripEstimationResponse
import tech.jaya.ridely.dto.trip.TripEstimationWithDriversResponse
import tech.jaya.ridely.service.driver.FindDriversNearService
import tech.jaya.ridely.service.driver.NearbyDriverAssemblerService
@Component
class TripEstimationService(
    private val routeEstimation: RouteEstimationService,
    private val findDriversNearService: FindDriversNearService,
    private val nearbyDriverAssemblerService: NearbyDriverAssemblerService,
    private val estimationPriceCalculatorService: PriceCalculatorService
) : Loggable() {

    fun estimateTrip(origin: LatLng, dest: LatLng): TripEstimationWithDriversResponse {
        log.info("Starting trip estimation from origin=$origin to destination=$dest")

        return try {
            val route = routeEstimation.estimateTrip(origin, dest)

            val driverLocations = findDriversNearService.findDriversNear(
                origin.latitude,
                origin.longitude
            )
            log.debug("Found : ${driverLocations.size} nearby driver")

            val drivers = nearbyDriverAssemblerService.retrieveNearbyDrivers(driverLocations)
            log.debug("Driver data assembled for ${drivers.size} drivers")

            val price = estimationPriceCalculatorService.calculatePrice(
                RouteInfo(
                    route.estimatedTimeMinutes,
                    route.distanceKm
                )
            )
            log.info("Trip estimation completed. Price: $price")
            TripEstimationWithDriversResponse(
                trip = TripEstimationResponse(
                    estimatedTimeMinutes = route.estimatedTimeMinutes,
                    distanceKm = route.distanceKm,
                    estimatedPrice = price
                ),
                nearbyDrivers = drivers,

                )
        } catch (ex: Exception) {
            log.error("Error during trip estimation from $origin to $dest", ex)
            throw RuntimeException("Failed to estimate trip", ex)
        }
    }
}
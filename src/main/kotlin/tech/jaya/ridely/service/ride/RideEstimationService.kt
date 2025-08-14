package tech.jaya.ridely.service.ride

import org.springframework.stereotype.Component
import tech.jaya.ridely.common.logging.Loggable
import tech.jaya.ridely.dto.route.LatLng
import tech.jaya.ridely.dto.ride.RideEstimationResponse
import tech.jaya.ridely.dto.ride.RideEstimationWithDriversResponse
import tech.jaya.ridely.integration.repository.DriverLocationRepository
import tech.jaya.ridely.service.driver.NearbyDriverAssemblerService

@Component
class RideEstimationService(
    private val routeEstimation: RideRouteEstimationService,
    private val driverLocationRepository: DriverLocationRepository,
    private val nearbyDriverAssemblerService: NearbyDriverAssemblerService,
) : Loggable() {

    fun estimateTrip(origin: LatLng, dest: LatLng): RideEstimationWithDriversResponse {
        log.info("Starting route estimation from origin=$origin to destination=$dest")

        return try {
            val route = routeEstimation.estimateRoute(origin, dest)

            val driverLocations = driverLocationRepository.findDriversNear(
                origin.latitude,
                origin.longitude
            )
            log.debug("Found : ${driverLocations.size} nearby driver")

            val drivers = nearbyDriverAssemblerService.retrieveNearbyDrivers(driverLocations)
            log.debug("Driver data assembled for ${drivers.size} drivers")

            val price = route.estimatedPrice
            log.info("Trip estimation completed. Price: $price")
            RideEstimationWithDriversResponse(
                trip = RideEstimationResponse(
                    estimatedTimeMinutes = route.estimatedTimeMinutes,
                    distanceKm = route.distanceKm,
                    estimatedPrice = price
                ),
                nearbyDrivers = drivers,

                )
        } catch (ex: Exception) {
            log.error("Error during route estimation from $origin to $dest", ex)
            throw RuntimeException("Failed to estimate route", ex)
        }
    }
}
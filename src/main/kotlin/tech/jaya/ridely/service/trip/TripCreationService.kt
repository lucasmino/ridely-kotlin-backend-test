package tech.jaya.ridely.service.trip

import org.springframework.stereotype.Component
import tech.jaya.ridely.domain.model.Ride
import tech.jaya.ridely.domain.repository.DriverRepo
import tech.jaya.ridely.dto.trip.LatLng
import tech.jaya.ridely.integration.gateway.GeocodingGateway
import tech.jaya.ridely.service.driver.FindDriversNearService
import tech.jaya.ridely.service.driver.NearbyDriverAssemblerService

@Component
class TripCreationService(
    private val routeEstimationService: RouteEstimationService,
    private val findDriversNearService: FindDriversNearService,
    private val nearbyDriverAssemblerService: NearbyDriverAssemblerService,
    private val addressFormater: GeocodingGateway,
    private val driverRepo: DriverRepo
) {
    fun createTrip(origin: LatLng, dest: LatLng, chosenDriverId: Long): Ride {
        val route = routeEstimationService.estimateTrip(origin, dest)
        val pickup = addressFormater.reverseGeocode(origin.latitude, origin.longitude)
        val dropoff = addressFormater.reverseGeocode(dest.latitude, dest.longitude)
        val driver = driverRepo.findById(chosenDriverId).orElseThrow()

        return Ride(
            pickUp = pickup,
            dropOff = dropoff,
            driver = driver,
            // outros campos: passageiro, status etc
        )
    }

}

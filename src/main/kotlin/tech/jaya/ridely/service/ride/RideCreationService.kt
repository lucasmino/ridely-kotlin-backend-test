package tech.jaya.ridely.service.ride

import org.springframework.stereotype.Service
import tech.jaya.ridely.controller.DriverUnavailable
import tech.jaya.ridely.domain.model.Ride
import tech.jaya.ridely.domain.repository.DriverRepo
import tech.jaya.ridely.domain.repository.RideRepo
import tech.jaya.ridely.dto.ride.RequestDriver
import tech.jaya.ridely.dto.ride.RequestDriverResponse
import tech.jaya.ridely.dto.route.LatLng
import tech.jaya.ridely.integration.client.GoogleGeocodingClient

@Service
class RideCreationService(
    private val geocodingGateway: GoogleGeocodingClient,
    private val rideEstimationService: RideEstimationService,
    private val driverRepo: DriverRepo,
    private val rideRepo: RideRepo
) {
    fun createFromRequest(request: RequestDriver): RequestDriverResponse {
        val origin = geocodingGateway.geocodeAddress(request.pickUp)
        val dest = geocodingGateway.geocodeAddress(request.dropOff)
        val trip = rideEstimationService.estimateTrip(
            LatLng(origin.first, origin.second),
            LatLng(dest.first, dest.second)
        )
        val selectedDriver = trip.nearbyDrivers.firstOrNull()
            ?: throw DriverUnavailable("No drivers available")

        selectedDriver.becomeBusy()
        driverRepo.save(selectedDriver)

        val ride = Ride(
            pickUp = request.pickUp,
            dropOff = request.dropOff,
            passengerName = request.passenger.name,
            passengerEmail = request.passenger.email,
            driver = selectedDriver,
            price = trip.trip.estimatedPrice
        )

        return RequestDriverResponse.fromRide(rideRepo.save(ride))
    }
}
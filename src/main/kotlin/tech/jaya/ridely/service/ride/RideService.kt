package tech.jaya.ridely.service.ride

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import tech.jaya.ridely.common.logging.Loggable
import tech.jaya.ridely.controller.DriverUnavailable
import tech.jaya.ridely.controller.RideNotFoundException
import tech.jaya.ridely.domain.model.Ride
import tech.jaya.ridely.domain.repository.DriverRepo
import tech.jaya.ridely.domain.repository.RideRepo
import tech.jaya.ridely.dto.ride.AcceptResponse
import tech.jaya.ridely.dto.ride.ActionRideRequest
import tech.jaya.ridely.dto.ride.CancelResponse
import tech.jaya.ridely.dto.ride.FinishResponse
import tech.jaya.ridely.dto.ride.FinishRideRequest
import tech.jaya.ridely.dto.ride.RefuseResponse
import tech.jaya.ridely.dto.ride.RequestDriver
import tech.jaya.ridely.dto.ride.RequestDriverResponse
import tech.jaya.ridely.dto.route.LatLng
import tech.jaya.ridely.integration.client.GoogleGeocodingClient

@Service
class RideService(
    private val driverRepo: DriverRepo,
    private val rideRepo: RideRepo,
    private val geocodingGateway: GoogleGeocodingClient,
    private val rideEstimationService: RideEstimationService
) : Loggable() {

    fun createRideFromRequest(request: RequestDriver): RequestDriverResponse {
        log.info("📥 Request received for pickup='${request.pickUp}', dropoff='${request.dropOff}'")
        val origin = geocodingGateway.geocodeAddress(request.pickUp)
        val dest = geocodingGateway.geocodeAddress(request.dropOff)
        val trip = rideEstimationService.estimateTrip(
            LatLng(origin.first, origin.second),
            LatLng(dest.first, dest.second)
        )
        val selectedDriver = trip.nearbyDrivers.firstOrNull() ?: throw DriverUnavailable("No drivers available")
        selectedDriver.becomeBusy()
        driverRepo.save(selectedDriver);

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

    fun getRide(id: Long): FinishResponse {
        val ride = rideRepo.findRideById(id).orElseThrow {
            throw RideNotFoundException("You don't have any Ride")
        }
        return FinishResponse.fromRide(ride)
    }

    fun refuseRide(req: ActionRideRequest): RefuseResponse {
        val id = req.id
        val ride = rideRepo.findById(id).orElseThrow { RideNotFoundException("No ride found with id $id") }
        ride.refuse()
        return RefuseResponse.Companion.fromRide(rideRepo.save(ride))
    }

    fun deleteRide(req: ActionRideRequest): CancelResponse {
        val id = req.id
        val ride = rideRepo.findById(id).orElseThrow { RideNotFoundException("No ride found with id $id") }
        ride.cancel()
        return CancelResponse.Companion.fromRide(rideRepo.save(ride))
    }

    fun finishRide(@RequestBody req: FinishRideRequest): FinishResponse {
        val (id) = req
        val ride = rideRepo.findById(id).orElseThrow { RideNotFoundException("No ride found with id $id") }
        ride.complete()
        return FinishResponse.Companion.fromRide(rideRepo.save(ride))
    }


    fun acceptRide(req: ActionRideRequest): AcceptResponse {
        val id = req.id
        val ride = rideRepo.findById(id).orElseThrow { RideNotFoundException("No ride found with id $id") }
        ride.accept()
        return AcceptResponse.Companion.fromRide(rideRepo.save(ride))
    }

    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        return rideRepo.deleteById(id).let {
            ResponseEntity.noContent().build()
        }
    }
}
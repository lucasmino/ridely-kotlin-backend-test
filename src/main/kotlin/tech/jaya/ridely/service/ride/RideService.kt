package tech.jaya.ridely.service.ride

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import tech.jaya.ridely.controller.DriverUnavailable
import tech.jaya.ridely.controller.RideNotFoundException
import tech.jaya.ridely.domain.repository.DriverRepo
import tech.jaya.ridely.domain.repository.RideRepo
import tech.jaya.ridely.dto.driver.AcceptResponse
import tech.jaya.ridely.dto.driver.ActionRideRequest
import tech.jaya.ridely.dto.driver.CancelResponse
import tech.jaya.ridely.dto.driver.FinishResponse
import tech.jaya.ridely.dto.driver.FinishRideRequest
import tech.jaya.ridely.dto.driver.RefuseResponse
import tech.jaya.ridely.dto.driver.RequestDriver
import tech.jaya.ridely.dto.driver.RequestDriverResponse

@Service
class RideService(
    private val driverRepo: DriverRepo,
    private val rideRepo: RideRepo
) {
    fun requestRide(req: RequestDriver): RequestDriverResponse {
        val driver = driverRepo.findAvailableDriver().orElseThrow {
            throw DriverUnavailable("We do not have drivers available")
        }
        val ride = req.toRide(driver)
        ride.request(driver)
        return RequestDriverResponse.Companion.fromRide(rideRepo.save(ride))
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
        val (id, price) = req
        val ride = rideRepo.findById(id).orElseThrow { RideNotFoundException("No ride found with id $id") }
        ride.complete(price)
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
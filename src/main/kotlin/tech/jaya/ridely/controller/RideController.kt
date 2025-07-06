package tech.jaya.ridely.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.jaya.ridely.domain.repository.DriverRepo
import tech.jaya.ridely.domain.repository.RideRepo
import tech.jaya.ridely.service.RideService

@RestController
@RequestMapping("/rides")
class RideController(
    private val rideService: RideService
) {
    @PostMapping("/request-driver")
    fun requestDriver(@RequestBody req: RequestDriver): RequestDriverResponse {
        return rideService.requestRide(req)
    }

    @PostMapping("/refuse-ride")
    fun refuseRide(@RequestBody req: ActionRideRequest): RefuseResponse {
        return rideService.refuseRide(req)
    }

    @PostMapping("/cancel-ride")
    fun deleteRide(@RequestBody req: ActionRideRequest): CancelResponse {
        return rideService.deleteRide(req)
    }

    @PostMapping("/finish-ride")
    fun finishRide(@RequestBody req: FinishRideRequest): FinishResponse {
        return rideService.finishRide(req)
    }

    @PostMapping("/accept-ride")
    fun acceptRide(@RequestBody req: ActionRideRequest): AcceptResponse {
       return rideService.acceptRide(req)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        return rideService.delete(id)
    }
}
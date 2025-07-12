package tech.jaya.ridely.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.jaya.ridely.dto.driver.AcceptResponse
import tech.jaya.ridely.dto.driver.ActionRideRequest
import tech.jaya.ridely.dto.driver.CancelResponse
import tech.jaya.ridely.dto.driver.FinishResponse
import tech.jaya.ridely.dto.driver.FinishRideRequest
import tech.jaya.ridely.dto.driver.RefuseResponse
import tech.jaya.ridely.dto.driver.RequestDriver
import tech.jaya.ridely.dto.driver.RequestDriverResponse
import tech.jaya.ridely.dto.trip.LatLng
import tech.jaya.ridely.dto.trip.TripEstimationResponse
import tech.jaya.ridely.service.ride.RideService
import tech.jaya.ridely.service.trip.RouteEstimationService

@RestController
@RequestMapping("/rides")
class RideController(
    private val rideService: RideService,
    private val routeEstimationService: RouteEstimationService
) {

    @GetMapping
    fun estimateTrip(
        @RequestParam originLat: Double,
        @RequestParam originLng: Double,
        @RequestParam destLat: Double,
        @RequestParam destLng: Double
    ): TripEstimationResponse {
        val origin = LatLng(originLat, originLng)
        val dest = LatLng(destLat, destLng)
        return routeEstimationService.estimateTrip(origin, dest)
    }

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
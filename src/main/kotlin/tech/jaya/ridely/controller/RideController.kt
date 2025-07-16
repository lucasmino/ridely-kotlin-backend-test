package tech.jaya.ridely.controller

import tech.jaya.ridely.service.ride.RideService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import tech.jaya.ridely.common.logging.Loggable
import tech.jaya.ridely.dto.ride.*
import tech.jaya.ridely.dto.route.LatLng
import tech.jaya.ridely.service.ride.RideEstimationService

@RestController
@RequestMapping("/rides")
class RideController(
    private val rideService: RideService,
    private val rideEstimationService: RideEstimationService
) : Loggable() {

    @GetMapping("/estimate")
    @PreAuthorize("hasRole('PASSENGER')")
    fun estimateTrip(
        @RequestParam originLat: Double,
        @RequestParam originLng: Double,
        @RequestParam destLat: Double,
        @RequestParam destLng: Double
    ): RideEstimationWithDriversResponse {
        val origin = LatLng(latitude = originLat, longitude = originLng)
        val dest = LatLng(latitude = destLat, longitude = destLng)

        log.info("Request to estimate route from $origin to $dest")

        return rideEstimationService.estimateTrip(origin, dest)
    }

    @GetMapping("/{id}/get-rides")
    fun getRide(@PathVariable id: Long): FinishResponse {
        return rideService.get(id)
    }


    @PostMapping("/request-driver")
    fun requestRide(@RequestBody req: RequestDriver): RequestDriverResponse {
        return rideService.createRide(req)
    }

    @PostMapping("/refuse-ride")
    fun refuseRide(@RequestBody req: ActionRideRequest): RefuseResponse {
        return rideService.refuse(req)
    }

    @PostMapping("/cancel-ride")
    fun deleteRide(@RequestBody req: ActionRideRequest): CancelResponse {
        return rideService.cancel(req)
    }

    @PostMapping("/finish-ride")
    fun finishRide(@RequestBody req: FinishRideRequest): FinishResponse {
        return rideService.finish(req)
    }

    @PostMapping("/accept-ride")
    fun acceptRide(@RequestBody req: ActionRideRequest): AcceptResponse {
        return rideService.accept(req)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        rideService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
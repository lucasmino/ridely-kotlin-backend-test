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
import tech.jaya.ridely.domain.model.Driver
import tech.jaya.ridely.domain.repository.DriverRepo
import tech.jaya.ridely.domain.repository.RideRepo
import tech.jaya.ridely.dto.driver.AcceptResponse
import tech.jaya.ridely.dto.driver.DriverLocationDto
import tech.jaya.ridely.dto.trip.DriverCreation
import tech.jaya.ridely.dto.trip.DriverResponse
import tech.jaya.ridely.dto.trip.LatLng
import tech.jaya.ridely.dto.trip.toResponse
import tech.jaya.ridely.integration.producer.DriverLocationProducer
import tech.jaya.ridely.service.driver.FindDriversNearService

@RestController
@RequestMapping("/drivers")
class DriverController(
    private val driverRepo: DriverRepo,
    private val rideRepo: RideRepo,
    private val driverLocationProducer: DriverLocationProducer,
    private val findDriversNearService: FindDriversNearService
) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<DriverResponse> {
        return driverRepo.findById(id).orElseThrow {
            DriverNotFound("Drive not found $id")
        }.let {
            ResponseEntity.ok(it.toResponse())
        }
    }

    @GetMapping("/{id}/get-rides")
    fun getRide(@PathVariable id: Long): AcceptResponse {
        val ride = rideRepo.findLastRideByDriveId(id).orElseThrow {
            throw RideNotFoundException("You don't have any Ride")
        }
        return AcceptResponse.fromRide(ride)
    }

    @PostMapping
    fun save(@RequestBody driverRequest: DriverCreation): ResponseEntity<DriverResponse> {
        println("🚗 RECEIVED: $driverRequest")
        return driverRepo.save(driverRequest.toDriver()).let {
            ResponseEntity.ok(it.toResponse())
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        return driverRepo.deleteById(id).let {
            ResponseEntity.noContent().build()
        }
    }

    @PostMapping("/drivers/{id}/location")
    fun updateLocation(@PathVariable id: String, @RequestParam lat: Double, @RequestParam long: Double) {
        driverLocationProducer.sendLocation(id, LatLng(lat, long))
    }

    @GetMapping("/drivers/near")
    fun getDriversNearByLocation(@RequestParam lat: Double, @RequestParam long: Double ):List<DriverLocationDto> {
        return findDriversNearService.findDriversNear(lat, long)
    }
}

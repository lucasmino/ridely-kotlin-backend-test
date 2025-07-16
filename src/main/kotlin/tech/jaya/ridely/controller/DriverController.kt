package tech.jaya.ridely.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.jaya.ridely.domain.repository.DriverRepo
import tech.jaya.ridely.domain.repository.RideRepo
import tech.jaya.ridely.dto.route.DriverCreation
import tech.jaya.ridely.dto.route.DriverResponse
import tech.jaya.ridely.dto.route.LatLng
import tech.jaya.ridely.dto.route.toResponse
import tech.jaya.ridely.integration.producer.DriverLocationProducer
import tech.jaya.ridely.integration.repository.DriverLocationRepository
import tech.jaya.ridely.service.driver.DriverLocationService
import tech.jaya.ridely.service.driver.DriverService

@RestController
@RequestMapping("/drivers")
class DriverController(
    private val driverService: DriverService,
    private val driverLocationService: DriverLocationService
) {

    @PostMapping
    fun save(@RequestBody driverRequest: DriverCreation): ResponseEntity<DriverResponse> {
        val driver = driverService.save(driverRequest)
        return ResponseEntity.ok(driver.toResponse())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<DriverResponse> {
        val driver = driverService.findById(id)
        return ResponseEntity.ok(driver.toResponse())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        driverService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/location")
    fun updateLocation(
        @PathVariable id: String,
        @RequestParam lat: Double,
        @RequestParam long: Double
    ) {
        driverLocationService.updateLocation(id, lat, long)
    }

    @GetMapping("/near")
    fun getDriversNear(
        @RequestParam lat: Double,
        @RequestParam long: Double
    ): List<Long> {
        return driverLocationService.findDriversNear(lat, long)
    }
}

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

@RestController
@RequestMapping("/drivers")
class DriverController(
    private val driverRepo: DriverRepo,
    private val driverLocationProducer: DriverLocationProducer,
    private val driverLocationRepository: DriverLocationRepository
) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<DriverResponse> {
        return driverRepo.findById(id).orElseThrow {
            DriverNotFound("Drive not found $id")
        }.let {
            ResponseEntity.ok(it.toResponse())
        }
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
    fun getDriversNearByLocation(@RequestParam lat: Double, @RequestParam long: Double): List<Long> {
        return driverLocationRepository.findDriversNear(lat, long)
    }
}

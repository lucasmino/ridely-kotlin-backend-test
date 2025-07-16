package tech.jaya.ridely.service.driver

import org.springframework.stereotype.Service
import tech.jaya.ridely.dto.route.LatLng
import tech.jaya.ridely.integration.producer.DriverLocationProducer
import tech.jaya.ridely.integration.repository.DriverLocationRepository

@Service
class DriverLocationService(
    private val driverLocationProducer: DriverLocationProducer,
    private val driverLocationRepository: DriverLocationRepository
) {
    fun updateLocation(id: String, lat: Double, long: Double) {
        driverLocationProducer.sendLocation(id, LatLng(lat, long))
    }

    fun findDriversNear(lat: Double, long: Double): List<Long> {
        return driverLocationRepository.findDriversNear(lat, long)
    }
}

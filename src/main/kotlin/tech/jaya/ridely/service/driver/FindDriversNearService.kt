package tech.jaya.ridely.service.driver

import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point
import org.springframework.data.redis.domain.geo.Metrics
import org.springframework.stereotype.Component
import tech.jaya.ridely.common.logging.Loggable
import tech.jaya.ridely.dto.driver.DriverLocationDto
import tech.jaya.ridely.integration.repository.DriverLocationRepository


@Component
class FindDriversNearService(
    private val driverLocationRepository: DriverLocationRepository,
) : Loggable() {

    private val searchRadiusInMeters = 150.0

    fun findDriversNear(lat: Double, lng: Double): List<DriverLocationDto> {
        log.info("🔍 Finding drivers near lat=$lat, lng=$lng")
        val circle = Circle(Point(lng, lat), Distance(searchRadiusInMeters, Metrics.METERS))

        return try {
            log.info("📍 Circle center: x=${circle.center.x}, y=${circle.center.y}, radius=${circle.radius.value}")

            val geoResults = driverLocationRepository.findDriversNear(circle)

            log.debug("📦 Geo results raw content: ${geoResults?.content}")

            val drivers = geoResults?.content
                ?.sortedBy {
                    log.debug("🚗 Driver ${it.content.name} found at distance: ${it.distance.value}")
                    it.distance.value
                }
                ?.take(3)
                ?.mapNotNull { result ->
                    val name = result.content.name              // Ex: "driver:1"
                    val point = result.content.point            // Point(lng, lat)
                    val driverId = name?.removePrefix("driver:")?.toLongOrNull()

                    log.debug("✅ Driver result -> name=$name, point=$point")

                    if (driverId != null && point != null) {
                        DriverLocationDto(driverId, lat = point.y, lng = point.x)
                    } else {
                        log.warn("⚠️ Driver skipped: driverId=$driverId, point=$point")
                        null
                    }
                }

            log.debug("✅ Found ${drivers?.size ?: 0} drivers")
            drivers ?: emptyList()

        } catch (ex: Exception) {
            log.error("❌ Error while searching nearby drivers", ex)
            throw RuntimeException("Failed to search nearby drivers", ex)
        }
    }
}
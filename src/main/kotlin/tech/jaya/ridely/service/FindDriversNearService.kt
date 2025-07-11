package tech.jaya.ridely.service

import org.slf4j.LoggerFactory
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point
import org.springframework.data.redis.domain.geo.Metrics
import org.springframework.stereotype.Component
import tech.jaya.ridely.dto.driver.DriverLocationDto
import tech.jaya.ridely.integration.DriverLocationRepository

@Component
class FindDriversNearService(
    private val driverLocationRepository: DriverLocationRepository,
) {
    private val logger = LoggerFactory.getLogger(FindDriversNearService::class.java)
    val searchRadiusInMeters = 3000.0
    fun findDriversNear(lat: Double, lng: Double): List<DriverLocationDto> {
        logger.info("Finding drivers near lat=$lat, lng=$lng")
        val circle = Circle(Point(lng, lat), Distance(searchRadiusInMeters, Metrics.METERS))
        try {
        val geoResults = driverLocationRepository.redisTemplate.opsForGeo()
            .radius(driverLocationRepository.key, circle)

            val drivers = geoResults?.content
            ?.sortedBy { it.distance.value }
            ?.take(3)
            ?.mapNotNull { result ->
                val driverId = result.content.name?.toLongOrNull()
                val point = result.content.point
                if (driverId != null && point != null) {
                    DriverLocationDto(
                        driverId = driverId,
                        lat = point.y,
                        lng = point.x
                    )
                } else null
            }
            logger.debug("Found ${drivers?.size ?: 0} drivers")
            return drivers ?: emptyList()
        } catch (ex: Exception) {
            logger.error("Error while searching nearby drivers", ex)
            throw RuntimeException("Failed to search nearby drivers", ex)
        }
    }


    }



package tech.jaya.ridely.integration.repository

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.domain.geo.Metrics
import org.springframework.stereotype.Component


@Component
class DriverLocationRepository (
    @Qualifier("redisTemplate")
    private val geoRedisTemplate: RedisTemplate<String, String>
) {
    private val searchRadiusInMeters = 150.0
    val key = "driver:locations"

    fun updateLocation(driverId: Long, lat: Double, lng: Double) {
        val point = Point(lng, lat)
        val member = driverId.toString()
        geoRedisTemplate.opsForGeo().add(key, point, member)

    }

    fun findDriversNear(
        lat: Double,
        lng: Double,
    ): List<Long> {
        val circle = Circle(Point(lng, lat), Distance(searchRadiusInMeters, Metrics.METERS))

        val geoResults = geoRedisTemplate.opsForGeo().radius(key, circle)

        return geoResults?.content
            ?.mapNotNull { it.content.name?.toLongOrNull() }
            ?: emptyList()
    }
}



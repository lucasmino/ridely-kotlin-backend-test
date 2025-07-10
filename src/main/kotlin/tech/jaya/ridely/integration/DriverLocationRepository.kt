package tech.jaya.ridely.integration

import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.domain.geo.Metrics
import org.springframework.stereotype.Component

@Component
class DriverLocationRepository (
    private val redisTemplate: StringRedisTemplate,
){
    private val key = "driver:locations"

    fun updateLocation(driverId: String, lat: Double, lng: Double) {
        val point = Point(lng, lat) // Redis espera (longitude, latitude)
        val member = "driver:$driverId"
        println("🔍 Salvando: key=$key | member=$member | point=${point.x}, ${point.y}")
        redisTemplate.opsForGeo().add(key, point, member)
        println("✅ Gravado no Redis")
    }

    fun findDriversNear(
        lat: Double,
        lng: Double,
        radiusMeters: Double
    ): List<Long> {
        val circle = Circle(Point(lng, lat), Distance(radiusMeters, Metrics.METERS))

        val geoResults = redisTemplate.opsForGeo().radius(key, circle)

        return geoResults?.content
            ?.mapNotNull { it.content.name?.toLongOrNull() }
            ?: emptyList()
    }
}
package tech.jaya.ridely.integration.repository

import org.springframework.data.geo.Circle
import org.springframework.data.geo.GeoResults
import org.springframework.data.geo.Point
import org.springframework.data.redis.connection.RedisGeoCommands
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.domain.geo.GeoLocation
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode


@Component
class DriverLocationRepository (
    private val geoRedisTemplate: RedisTemplate<String, String>
) {
    val key = "driver:locations"

    fun updateLocation(driverId: Long, lat: Double, lng: Double) {
        val point = Point(lng, lat)
        val member: String = "driver:$driverId"
        geoRedisTemplate.opsForGeo().add(key, point, member)

    }

    fun findDriversNear(circle: Circle): GeoResults<RedisGeoCommands.GeoLocation<String>>? {
        return geoRedisTemplate.opsForGeo().radius(key, circle)
    }
}

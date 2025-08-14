package tech.jaya.ridely.integration.gateway

import org.springframework.stereotype.Component

@Component
interface GeocodingGateway {
    fun reverseGeocode(lat: Double, lng: Double): String
    fun geocodeAddress(address: String): Pair<Double, Double>
}
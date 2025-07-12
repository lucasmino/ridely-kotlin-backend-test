package tech.jaya.ridely.integration.gateway

interface GeocodingGateway {
    fun reverseGeocode(lat: Double, lng: Double): String
    fun geocodeAddress(address: String): Pair<Double, Double>
}
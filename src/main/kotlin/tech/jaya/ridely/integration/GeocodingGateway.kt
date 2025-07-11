package tech.jaya.ridely.integration

interface GeocodingGateway {
    fun reverseGeocode(lat: Double, lng: Double): String
}
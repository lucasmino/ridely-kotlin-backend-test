package tech.jaya.ridely.integration

interface MapsGateway {
    fun getRouteInfo(
        originLat: Double,
        originLon: Double,
        destLat: Double,
        destLon: Double
    ): RouteInfo
}
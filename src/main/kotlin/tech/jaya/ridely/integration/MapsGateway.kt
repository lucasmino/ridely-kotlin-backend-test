package tech.jaya.ridely.integration

import tech.jaya.ridely.dto.RouteInfo

interface MapsGateway {
    fun getRouteInfo(
        originLat: Double,
        originLon: Double,
        destLat: Double,
        destLon: Double
    ): RouteInfo
}
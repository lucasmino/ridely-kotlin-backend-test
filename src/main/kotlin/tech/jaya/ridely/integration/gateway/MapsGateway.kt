package tech.jaya.ridely.integration.gateway

import tech.jaya.ridely.dto.route.RouteInfo

interface MapsGateway {
    fun getRouteInfo(
        originLat: Double,
        originLon: Double,
        destLat: Double,
        destLon: Double
    ): RouteInfo
}
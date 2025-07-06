package tech.jaya.ridely.service

import org.springframework.stereotype.Service
import tech.jaya.ridely.dto.LatLng
import tech.jaya.ridely.integration.MapsGateway
import tech.jaya.ridely.integration.RouteInfo
@Service
class RouteEstimationService(private val gateway: MapsGateway) {
    fun createRoute(origin: LatLng,destination: LatLng): RouteInfo {
        return gateway.getRouteInfo(originLat = origin.latitude, originLon = origin.longitude, destLat = destination.latitude, destLon = destination.longitude)
    }


}
package tech.jaya.ridely.dto.trip

import tech.jaya.ridely.dto.driver.NearbyDriverDto

data class TripEstimationWithDriversResponse(
    val trip: TripEstimationResponse,
    val nearbyDrivers: List<NearbyDriverDto>
)
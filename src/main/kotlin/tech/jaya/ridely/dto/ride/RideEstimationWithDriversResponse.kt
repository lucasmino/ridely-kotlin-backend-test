package tech.jaya.ridely.dto.ride

import tech.jaya.ridely.domain.model.Driver

data class RideEstimationWithDriversResponse(
    val trip: RideEstimationResponse,
    val nearbyDrivers: List<Driver>
)
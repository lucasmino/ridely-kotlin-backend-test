package tech.jaya.ridely.dto.ride

import java.math.BigDecimal

data class RideEstimationResponse (
    val estimatedTimeMinutes: Int,
    val distanceKm: Double,
    val estimatedPrice: BigDecimal
)
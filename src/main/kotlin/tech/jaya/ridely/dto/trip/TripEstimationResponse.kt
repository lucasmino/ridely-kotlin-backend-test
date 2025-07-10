package tech.jaya.ridely.dto.trip

import java.math.BigDecimal

data class TripEstimationResponse (
    val estimatedTimeMinutes: Int,
    val distanceKm: Double,
    val estimatedPrice: BigDecimal
)

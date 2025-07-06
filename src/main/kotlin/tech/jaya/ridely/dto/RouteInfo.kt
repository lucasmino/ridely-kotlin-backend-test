package tech.jaya.ridely.dto

import java.math.BigDecimal

data class RouteInfo(
    val estimatedTimeMinutes: Int,
    val distanceKm: Double,
    val estimatedPrice: BigDecimal
)

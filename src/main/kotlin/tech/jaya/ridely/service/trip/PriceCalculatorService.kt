package tech.jaya.ridely.service.trip

import org.springframework.stereotype.Service
import tech.jaya.ridely.common.logging.Loggable
import tech.jaya.ridely.dto.trip.RouteInfo
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class PriceCalculatorService : Loggable() {
    fun calculatePrice(routeInfo: RouteInfo): BigDecimal {
        log.info("Calculating price for route: distance=${routeInfo.distanceKm}, time=${routeInfo.estimatedTimeMinutes}")
        try {
            val kmCost = BigDecimal(routeInfo.distanceKm).multiply(BigDecimal("3.00"))
            val minCost = BigDecimal(routeInfo.estimatedTimeMinutes).multiply(BigDecimal("2.00"))
            val total = kmCost.add(minCost)

            val fee = total.multiply(BigDecimal("0.01"))

            val totalWithFee = total.add(fee)
            val finalPrice = totalWithFee.setScale(2, RoundingMode.HALF_UP)

            log.debug("Calculated price: $finalPrice (total=$total, fee=$fee)")

            return finalPrice
        } catch (ex: Exception) {
            log.error("Failed to calculate price for route: $routeInfo", ex)
            throw IllegalArgumentException("Could not calculate trip price", ex)
        }
    }
}

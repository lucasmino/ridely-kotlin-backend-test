package tech.jaya.ridely.service

import org.springframework.stereotype.Service
import tech.jaya.ridely.dto.trip.RouteInfo
import java.math.BigDecimal
import java.math.RoundingMode
@Service
class PriceCalculatorService {
    fun calculatePrice(routeInfo: RouteInfo): BigDecimal {
        val kmCost = BigDecimal(routeInfo.distanceKm).multiply(BigDecimal("3.00"))
        val minCost = BigDecimal(routeInfo.estimatedTimeMinutes).multiply(BigDecimal("2.00"))
        val total = kmCost.add(minCost)

        // Aplica a taxa de 1% (dividir por 100)
        val fee = total.multiply(BigDecimal("0.01"))

        // Valor total com taxa (caso queira somar)
        val totalWithFee = total.add(fee)

        return totalWithFee.setScale(2, RoundingMode.HALF_UP)
    }
}
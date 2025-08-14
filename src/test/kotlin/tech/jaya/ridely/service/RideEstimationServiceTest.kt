package tech.jaya.ridely.service

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.jaya.ridely.domain.model.Driver
import tech.jaya.ridely.dto.ride.RideEstimationResponse
import tech.jaya.ridely.dto.route.LatLng
import tech.jaya.ridely.integration.repository.DriverLocationRepository
import tech.jaya.ridely.service.driver.NearbyDriverAssemblerService
import tech.jaya.ridely.service.ride.RideEstimationService
import tech.jaya.ridely.service.ride.RideRouteEstimationService
import java.math.BigDecimal
import kotlin.test.assertEquals

class RideEstimationServiceTest {

    private lateinit var routeEstimation: RideRouteEstimationService
    private lateinit var driverLocationRepository: DriverLocationRepository
    private lateinit var nearbyDriverAssemblerService: NearbyDriverAssemblerService
    private lateinit var rideEstimationService: RideEstimationService

    @BeforeEach
    fun setUp() {
        println("Inicializando mocks")
        routeEstimation = mockk()
        driverLocationRepository = mockk()
        nearbyDriverAssemblerService = mockk()

        rideEstimationService = RideEstimationService(
            routeEstimation,
            driverLocationRepository,
            nearbyDriverAssemblerService
        )
    }


    @Test
    fun `should return estimation and nearby drivers`() {
        val origin = LatLng(latitude = 1.0, longitude = 2.0)
        val dest = LatLng(latitude = 3.0, longitude = 4.0)

        val routeInfo = RideEstimationResponse(
            estimatedTimeMinutes = 15,
            distanceKm = 8.5,
            estimatedPrice = BigDecimal("22.50")
        )

        val geoResults = listOf(mockk<Long>())
        val drivers = listOf(mockk<Driver>())

        every { routeEstimation.estimateRoute(origin, dest) } returns routeInfo
        every { driverLocationRepository.findDriversNear(1.0, 2.0) } returns geoResults
        every { nearbyDriverAssemblerService.retrieveNearbyDrivers(geoResults) } returns drivers

        val result = rideEstimationService.estimateTrip(origin, dest)

        assertEquals(15, result.trip.estimatedTimeMinutes)
        assertEquals(8.5, result.trip.distanceKm)
        assertEquals(BigDecimal("22.50"), result.trip.estimatedPrice)
        assertEquals(drivers, result.nearbyDrivers)
    }
}

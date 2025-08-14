package tech.jaya.ridely.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tech.jaya.ridely.domain.model.Driver

class DriverTest {

    @Test
    fun `when a drive is available and try to be unavailable`() {
        val driver = Driver(
            name = "test jaya",
            carLicensePlate = "AASS",
            carModel = "SPIN",
            carColor = "Black"
        )
        driver.becomeBusy()
        assertEquals(false, driver.available)
    }

    @Test
    fun `when a drive is unavailable and try to be available`() {
        val driver = Driver(
            name = "test jaya",
            carLicensePlate = "AASS",
            carModel = "SPIN",
            carColor = "Black",
            available = false
        )

        driver.becomeAvailable()
        assertEquals(true, driver.available)
    }
}
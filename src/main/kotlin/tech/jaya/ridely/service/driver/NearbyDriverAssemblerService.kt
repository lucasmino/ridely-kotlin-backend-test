package tech.jaya.ridely.service.driver

import org.springframework.stereotype.Component
import tech.jaya.ridely.common.logging.Loggable
import tech.jaya.ridely.domain.model.Driver
import tech.jaya.ridely.domain.repository.DriverRepo

@Component
class NearbyDriverAssemblerService(
    private val driverRepo: DriverRepo
) : Loggable() {

    fun retrieveNearbyDrivers(driversId: List<Long>): List<Driver> {
        log.info("Retrieving nearby drivers for ${driversId.size} locations")
            val drivers = driverRepo.findAllAvailableById(driversId)

            drivers.forEach {
                log.debug("✅ Available driver found: ${it.name} (ID: ${it.id})")
            }

            return drivers
}
    }
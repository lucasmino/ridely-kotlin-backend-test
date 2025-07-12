package tech.jaya.ridely.service.driver

import org.springframework.stereotype.Component
import tech.jaya.ridely.common.logging.Loggable
import tech.jaya.ridely.domain.repository.DriverRepo
import tech.jaya.ridely.dto.driver.DriverLocationDto
import tech.jaya.ridely.dto.driver.NearbyDriverDto
import java.lang.IllegalStateException

@Component
class NearbyDriverAssemblerService(
    private val driverRepo: DriverRepo
) : Loggable() {

    fun retrieveNearbyDrivers(driversLocationList: List<DriverLocationDto>): List<NearbyDriverDto> {
        log.info("Retrieving nearby drivers for ${driversLocationList.size} locations")
        return driversLocationList.map { driverLocation ->
            val driverData = driverRepo.findById(driverLocation.driverId).orElseThrow {
                val message = "Driver with ID ${driverLocation.driverId} not found"
                log.warn(message)
                NoSuchElementException(message)
            }

            log.debug("Mapping driver data for ID: ${driverData.id}")

            NearbyDriverDto(
                driverData.id?: throw IllegalStateException("Driver with ID ${driverData.id} not found"),
                driverData.name,
                driverData.carModel,
                driverData.carLicensePlate,
                driverLocation.lat,
                driverLocation.lng
            )
        }
    }
}
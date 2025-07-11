package tech.jaya.ridely.service

import org.springframework.stereotype.Component
import tech.jaya.ridely.domain.repository.DriverRepo
import tech.jaya.ridely.dto.driver.DriverLocationDto
import tech.jaya.ridely.dto.driver.NearbyDriverDto

@Component
class NearbyDriverAssemblerService(
    private val driverRepo: DriverRepo
) {

    fun retrieveNearbyDrivers(driversLocationList: List<DriverLocationDto>): List<NearbyDriverDto> {
        return driversLocationList.map { driverLocation ->
            val driverData = driverRepo.findById(driverLocation.driverId).get()
            NearbyDriverDto(
                driverData.id!!,
                driverData.name,
                driverData.carModel,
                driverData.carLicensePlate,
                driverLocation.lat,
                driverLocation.lng
            )
        }
    }
}
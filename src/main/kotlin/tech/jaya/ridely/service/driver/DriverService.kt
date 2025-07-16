package tech.jaya.ridely.service.driver

import org.springframework.stereotype.Service
import tech.jaya.ridely.controller.DriverNotFound
import tech.jaya.ridely.domain.model.Driver
import tech.jaya.ridely.domain.repository.DriverRepo
import tech.jaya.ridely.dto.route.DriverCreation

@Service
class DriverService(
    private val driverRepo: DriverRepo
) {
    fun save(driverRequest: DriverCreation): Driver {
        return driverRepo.save(driverRequest.toDriver())
    }

    fun findById(id: Long): Driver {
        return driverRepo.findById(id).orElseThrow {
            DriverNotFound("Driver not found with id $id")
        }
    }

    fun deleteById(id: Long) {
        driverRepo.deleteById(id)
    }
}

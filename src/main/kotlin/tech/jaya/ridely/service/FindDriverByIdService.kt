package tech.jaya.ridely.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import tech.jaya.ridely.domain.model.Driver
import tech.jaya.ridely.domain.repository.DriverRepo
import tech.jaya.ridely.dto.driver.DriverLocationDto
import java.util.Optional

@Component
class FindDriverByIdService(private val driverRepo: DriverRepo) {
    fun findDriverById(id: Long): Optional<Driver> {
        return driverRepo.findById(id)
    }
}
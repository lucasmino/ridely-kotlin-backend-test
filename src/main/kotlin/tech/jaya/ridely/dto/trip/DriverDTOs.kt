package tech.jaya.ridely.dto.trip

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.ridely.domain.model.Driver

data class DriverCreation(
    @JsonProperty(required = true)
    val name: String,
    @JsonProperty(required = true)
    val available: Boolean,
    @JsonProperty(required = true)
    val car: CarDto
) {
    fun toDriver(): Driver {
        return Driver(
            name = this.name,
            available = this.available,
            carLicensePlate = this.car.licensePlate,
            carModel = this.car.model,
            carColor = this.car.color
        )
    }
}

data class CarDto(
    @JsonProperty(required = true)
    val licensePlate: String,
    @JsonProperty(required = true)
    val model: String,
    @JsonProperty(required = true)
    val color: String
)

data class DriverResponse(
    val id: Long,
    val name: String,
    val available: Boolean,
    val activationDate: String,
    val car: CarDto
)

fun Driver.toResponse(): DriverResponse {
    return DriverResponse(
        id = this.id!!,
        name = this.name,
        available = this.available,
        activationDate = this.activationDate.toString(),
        car = CarDto(
            licensePlate = this.carLicensePlate,
            model = this.carModel,
            color = this.carColor
        )
    )
}
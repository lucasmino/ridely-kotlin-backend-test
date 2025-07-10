package tech.jaya.ridely.dto.driver

import com.fasterxml.jackson.annotation.JsonProperty
import tech.jaya.ridely.domain.model.Driver
import tech.jaya.ridely.domain.model.Ride
import tech.jaya.ridely.domain.model.Status
import java.math.BigDecimal

class PassengerRequest(
    @JsonProperty(required = true)
    val name: String,
    @JsonProperty(required = true)
    val email: String
)

class RequestDriver(
    @JsonProperty(required = true)
    val passenger: PassengerRequest,
    @JsonProperty(required = true)
    val pickUp: String,
    @JsonProperty(required = true)
    val dropOff: String
) {
    fun toRide(driver: Driver) = Ride(
        pickUp = this.pickUp,
        dropOff = this.dropOff,
        passengerName = passenger.name,
        passengerEmail = passenger.email,
        driver = driver
    )
}

data class FinishRideRequest(
    @JsonProperty(required = true)
    val id: Long,
    @JsonProperty(required = true)
    val price: BigDecimal
)

data class ActionRideRequest(
    @JsonProperty(required = true)
    val id: Long
)

class PassengerResponse(
    val name: String,
    val email: String
)

class RequestDriverResponse private constructor(
    val id: Long,
    val driver: DriverDto,
    val status: Status,
    val dropOff: String,
    val pickUp: String,
) {
    data class DriverDto(
        val name: String,
        val car: CarDto
    ) {
        data class CarDto(
            val licensePlate: String,
            val model: String,
            val color: String
        )
    }

    companion object {
        fun fromRide(ride: Ride) = RequestDriverResponse(
            id = ride.id!!,
            dropOff = ride.dropOff!!,
            pickUp = ride.pickUp!!,
            status = ride.status!!,
            driver = DriverDto(
                name = ride.driver!!.name,
                car = DriverDto.CarDto(
                    licensePlate = ride.driver!!.carLicensePlate,
                    model = ride.driver!!.carModel,
                    color = ride.driver!!.carColor
                )
            ),
        )
    }
}


class FinishResponse private constructor(
    val id: Long,
    val passenger: PassengerResponse,
    val dropOff: String,
    val status: Status,
    val price: BigDecimal
) {

    companion object {
        fun fromRide(ride: Ride): FinishResponse {
            return FinishResponse(
                id = ride.id!!,
                passenger = PassengerResponse(ride.passengerName!!, ride.passengerEmail!!),
                dropOff = ride.dropOff!!,
                status = ride.status!!,
                price = ride.price!!
            )
        }
    }
}

class RefuseResponse private constructor(
    val id: Long,
    val passenger: PassengerResponse,
    val pickUp: String,
    val dropOff: String,
    val status: Status
) {
    companion object {
        fun fromRide(ride: Ride) = RefuseResponse(
            id = ride.id!!,
            passenger = PassengerResponse(ride.passengerName!!, ride.passengerEmail!!),
            pickUp = ride.pickUp!!,
            dropOff = ride.dropOff!!,
            status = ride.status!!
        )
    }
}

class CancelResponse private constructor(
    val id: Long,
    val pickUp: String,
    val dropOff: String,
    val status: Status
) {
    companion object {
        fun fromRide(ride: Ride) = CancelResponse(
            id = ride.id!!,
            pickUp = ride.pickUp!!,
            dropOff = ride.dropOff!!,
            status = ride.status!!
        )
    }
}

class AcceptResponse private constructor(
    val id: Long,
    val passenger: PassengerResponse,
    val pickUp: String,
    val dropOff: String,
    val status: Status,
) {
    companion object {
        fun fromRide(ride: Ride) = AcceptResponse(
            id = ride.id!!,
            passenger = PassengerResponse(ride.passengerName!!, ride.passengerEmail!!),
            pickUp = ride.pickUp!!,
            dropOff = ride.dropOff!!,
            status = ride.status!!
        )
    }
}
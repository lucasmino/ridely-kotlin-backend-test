package tech.jaya.ridely.dto.driver

data class NearbyDriverDto(
    val id: Long,
    val name: String,
    val carModel: String,
    val licensePlate: String
)
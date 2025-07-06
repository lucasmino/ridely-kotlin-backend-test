package tech.jaya.ridely.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tech.jaya.ridely.domain.model.Ride
import java.util.Optional

@Repository
interface RideRepo : JpaRepository<Ride, Long> {

    @Query("SELECT r FROM Ride r WHERE r.driver.id=:driverId AND r.status = 'REQUESTED'")
    fun findLastRideByDriveId(driverId: Long): Optional<Ride>
}
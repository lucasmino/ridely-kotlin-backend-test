package tech.jaya.ridely.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tech.jaya.ridely.domain.model.Driver
import java.util.Optional

@Repository
interface DriverRepo : JpaRepository<Driver, Long> {
    @Query("SELECT e FROM Driver e WHERE e.available=true order by e.activationDate asc limit 1")
    fun findAvailableDriver(): Optional<Driver>
}
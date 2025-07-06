package tech.jaya.ridely

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import tech.jaya.ridely.service.RouteEstimationService

@SpringBootApplication
class RidelyApplication

fun main(args: Array<String>) {
	runApplication<RidelyApplication>(*args)
}

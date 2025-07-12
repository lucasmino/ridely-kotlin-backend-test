package tech.jaya.ridely

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RidelyApplication

fun main(args: Array<String>) {
	runApplication<RidelyApplication>(*args)
}

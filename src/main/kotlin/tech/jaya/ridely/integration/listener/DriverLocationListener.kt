package tech.jaya.ridely.integration.listener

import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Component
import tech.jaya.ridely.dto.driver.DriverLocationDto
import tech.jaya.ridely.integration.repository.DriverLocationRepository

@Component
class DriverLocationListener(
    private val driverLocationRepository: DriverLocationRepository,
    private val objectMapper: ObjectMapper,

    ) {
    @SqsListener(value = ["\${aws.queue_url}"], pollTimeoutSeconds = "5", maxMessagesPerPoll = "5")
    fun receiveMessage(payload: String) {
        try {
            val data = objectMapper.readValue(payload, DriverLocationDto::class.java)
            driverLocationRepository.updateLocation(data.driverId, data.lat, data.lng)
        } catch (ex: Exception) {
            println("Erro ao processar mensagem: $payload - ${ex.message}")
        }
    }
}
package tech.jaya.ridely.integration.producer

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import tech.jaya.ridely.dto.route.LatLng

@Component
class DriverLocationProducer(
    private val sqsAsyncClient: SqsAsyncClient,
    @Value("\${aws.queue_url}")
    private val queueUrl: String
) {
    fun sendLocation(driverId: String, location: LatLng) {
        val messageBody = """{"driverId":"$driverId","lat":${location.latitude},"lng":${location.longitude}}"""
        val request = SendMessageRequest.builder()
            .queueUrl(queueUrl)
            .messageBody(messageBody)
            .build()
        sqsAsyncClient.sendMessage(request)
    }
}
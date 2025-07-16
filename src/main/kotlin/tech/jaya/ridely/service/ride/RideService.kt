import org.springframework.stereotype.Service
import tech.jaya.ridely.dto.ride.ActionRideRequest
import tech.jaya.ridely.dto.ride.FinishRideRequest
import tech.jaya.ridely.dto.ride.RequestDriver
import tech.jaya.ridely.service.ride.RideCreationService

@Service
class RideService(
    private val rideCreationService: RideCreationService,
    private val rideLifecycleService: RideLifecycleService
) {
    fun createRide(req: RequestDriver) = rideCreationService.createFromRequest(req)

    fun accept(req: ActionRideRequest) = rideLifecycleService.acceptRide(req.id)

    fun refuse(req: ActionRideRequest) = rideLifecycleService.refuseRide(req.id)

    fun finish(req: FinishRideRequest) = rideLifecycleService.finishRide(req.id)

    fun cancel(req: ActionRideRequest) = rideLifecycleService.cancelRide(req.id)

    fun delete(id: Long) = rideLifecycleService.deleteRide(id)

    fun get(id: Long) = rideLifecycleService.getRide(id)
}

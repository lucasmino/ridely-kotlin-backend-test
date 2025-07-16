package tech.jaya.ridely.service.ride

import org.springframework.stereotype.Service
import tech.jaya.ridely.controller.RideNotFoundException
import tech.jaya.ridely.domain.model.Ride
import tech.jaya.ridely.domain.repository.RideRepo
import tech.jaya.ridely.dto.ride.AcceptResponse
import tech.jaya.ridely.dto.ride.CancelResponse
import tech.jaya.ridely.dto.ride.FinishResponse
import tech.jaya.ridely.dto.ride.RefuseResponse

@Service
class RideLifecycleService(
    private val rideRepo: RideRepo
) {
    fun acceptRide(id: Long): AcceptResponse {
        val ride = findRide(id)
        ride.accept()
        return AcceptResponse.fromRide(rideRepo.save(ride))
    }

    fun refuseRide(id: Long): RefuseResponse {
        val ride = findRide(id)
        ride.refuse()
        return RefuseResponse.fromRide(rideRepo.save(ride))
    }

    fun finishRide(id: Long): FinishResponse {
        val ride = findRide(id)
        ride.complete()
        return FinishResponse.fromRide(rideRepo.save(ride))
    }

    fun cancelRide(id: Long): CancelResponse {
        val ride = findRide(id)
        ride.cancel()
        return CancelResponse.fromRide(rideRepo.save(ride))
    }

    fun getRide(id: Long): FinishResponse {
        return FinishResponse.fromRide(findRide(id))
    }

    fun deleteRide(id: Long) {
        rideRepo.deleteById(id)
    }

    private fun findRide(id: Long): Ride {
        return rideRepo.findById(id).orElseThrow {
            RideNotFoundException("No ride found with id $id")
        }
    }
}

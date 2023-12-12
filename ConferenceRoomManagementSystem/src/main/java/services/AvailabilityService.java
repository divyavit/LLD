package services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;


import db.Storage;

@RequiredArgsConstructor
public class AvailabilityService {

    private Boolean isNotOverlapping(final Integer startTime, final Integer endTime,
                                     final Integer bookStartTime, final Integer bookEndTime) {
        return endTime < bookStartTime || bookEndTime < startTime;
    }

    public Boolean isAvailable(@NonNull final String roomId, @NonNull final Integer startTime, @NonNull Integer endTime) {
        if(!Storage.getBookings().containsKey(roomId)) return Boolean.TRUE;
        val booking  = Storage.getBookings().get(roomId);
        return isNotOverlapping(startTime, endTime, booking.getStartTime(), booking.getEndTime());
    }
}

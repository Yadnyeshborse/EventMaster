package com.rungroup.web.repository;

import com.rungroup.web.models.Booking;
import com.rungroup.web.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepo extends JpaRepository<Booking,Long> {
    List<Booking> findByEvent(Event event);
}

package com.rungroup.web.service;


import com.rungroup.web.models.Booking;
import com.rungroup.web.models.Club;
import com.rungroup.web.models.Event;
import com.rungroup.web.repository.BookingRepo;
import com.rungroup.web.repository.EventRepositery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {


    private BookingRepo bookingRepo;
    private EventRepositery eventRepositery;

    @Autowired
    public BookingService(BookingRepo bookingRepo,EventRepositery eventRepositery) {
        this.bookingRepo = bookingRepo;
        this.eventRepositery=eventRepositery;
    }


    public void createBooking(Long id, Booking booking) {
        Event event = eventRepositery.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        booking.setEvent(event);  // Associate the event with the booking
        bookingRepo.save(booking);  // Save the booking (not the event)
    }

}

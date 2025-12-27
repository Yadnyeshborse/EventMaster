package com.rungroup.web.service;


import com.rungroup.web.models.Booking;
import com.rungroup.web.models.Event;
import com.rungroup.web.repository.BookingRepo;
import com.rungroup.web.repository.EventRepositery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    // New feature: Find all bookings
    public List<Booking> findAllBookings() {
        return bookingRepo.findAll();
    }

    // New feature: Find bookings by event ID
    public List<Booking> findBookingsByEventId(Long eventId) {
        Event event = eventRepositery.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return bookingRepo.findByEvent(event);
    }

    // New feature: Find booking by ID
    public Optional<Booking> findBookingById(Long bookingId) {
        return bookingRepo.findById(bookingId);
    }

    // New feature: Delete booking
    public void deleteBooking(Long bookingId) {
        if (!bookingRepo.existsById(bookingId)) {
            throw new RuntimeException("Booking not found");
        }
        bookingRepo.deleteById(bookingId);
    }


}

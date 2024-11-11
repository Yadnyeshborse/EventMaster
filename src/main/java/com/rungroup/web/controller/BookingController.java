package com.rungroup.web.controller;

import com.rungroup.web.models.Booking;
import com.rungroup.web.models.Event;
import com.rungroup.web.service.BookingService;
import com.rungroup.web.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class BookingController {
    private BookingService bookingService;
    private EventService eventService;

    @Autowired
    public BookingController(BookingService bookingService,EventService eventService) {
        this.bookingService = bookingService;
        this.eventService=eventService;
    }




    @GetMapping("/booking/{id}/booking/new")
    public String showBookingForm(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event", event);  // Pass the Event, not Optional
        model.addAttribute("booking", new Booking());  // Add an empty Booking object for the form
        return "booking-create";  // Template name
    }



    @PostMapping("/booking/{id}/booking")
    public String createBooking(@PathVariable("id") Long id, @ModelAttribute("event") Booking booking) {
        bookingService.createBooking(id, booking);
        return "redirect:/events/" + id;  // Redirect back to the club page
    }

}

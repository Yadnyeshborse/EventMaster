package com.rungroup.web.controller;

import com.rungroup.web.models.Booking;
import com.rungroup.web.models.Event;
import com.rungroup.web.service.BookingService;
import com.rungroup.web.service.EmailService;
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
    private EmailService emailService;

    @Autowired
    public BookingController(BookingService bookingService,EventService eventService,EmailService emailService) {
        this.bookingService = bookingService;
        this.eventService=eventService;
        this.emailService=emailService;
    }




    @GetMapping("/booking/{id}/booking/new")
    public String showBookingForm(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event", event);  // Pass the Event, not Optional
        model.addAttribute("booking", new Booking());  // Add an empty Booking object for the form
        return "booking-create";  // Template name
    }



//    @PostMapping("/booking/{id}/booking")
//    public String createBooking(@PathVariable("id") Long id, @ModelAttribute("event") Booking booking) {
//        bookingService.createBooking(id, booking);
//        return "booking/{id}/booking";  // Redirect back to the club page
//    }

    @PostMapping("/booking/{id}/booking")
    public String createBooking(@PathVariable("id") Long id, @ModelAttribute("booking") Booking booking) {
        bookingService.createBooking(id, booking);

        // Send confirmation email
        Event event = eventService.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        String subject = "Booking Confirmation";
        String text = "Thank you for booking! Here are the event details:\n" +
                "Event Name: " + event.getName() + "\n" +
                "Date: " + event.getType() + "\n" +
                "Price: " + event.getPrice()+ "\n" +
                " Start Time "+event.getStarttime()+ "\n" +
                " End Time "+event.getEndtime();

        // Assuming Booking has an email field
        emailService.sendEmail(booking.getEmail(), subject, text);

        return "redirect:/events/" + id;
    }

}

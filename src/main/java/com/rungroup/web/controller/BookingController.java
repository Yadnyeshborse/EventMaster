package com.rungroup.web.controller;

import com.rungroup.web.models.Booking;
import com.rungroup.web.models.Event;
import com.rungroup.web.service.BookingService;
import com.rungroup.web.service.EmailService;
import com.rungroup.web.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
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
    public String createBooking(@PathVariable("id") Long id, 
                                @Valid @ModelAttribute("booking") Booking booking,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            Event event = eventService.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
            model.addAttribute("event", event);
            return "booking-create";
        }

        try {
            bookingService.createBooking(id, booking);

            Event event = eventService.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
            String subject = "Booking Confirmation";
            String text = "Thank you for booking! Here are the event details:\n" +
                    "Event Name: " + event.getName() + "\n" +
                    "Type: " + event.getType() + "\n" +
                    "Price: " + event.getPrice() + "\n" +
                    "Start Time: " + event.getStarttime() + "\n" +
                    "End Time: " + event.getEndtime();

            emailService.sendEmail(booking.getEmail(), subject, text);

            return "redirect:/events/" + id + "?bookingSuccess=true";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create booking: " + e.getMessage());
            Event event = eventService.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
            model.addAttribute("event", event);
            return "booking-create";
        }
    }

    // New feature: List all bookings
    @GetMapping("/bookings")
    public String getAllBookings(Model model) {
        List<Booking> bookings = bookingService.findAllBookings();
        model.addAttribute("bookings", bookings);
        return "bookings-list";
    }

    // New feature: List bookings for a specific event
    @GetMapping("/events/{eventId}/bookings")
    public String getEventBookings(@PathVariable("eventId") Long eventId, Model model) {
        Event event = eventService.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        List<Booking> bookings = bookingService.findBookingsByEventId(eventId);
        model.addAttribute("event", event);
        model.addAttribute("bookings", bookings);
        return "event-bookings";
    }

    // New feature: View booking details
    @GetMapping("/bookings/{bookingId}")
    public String getBookingDetails(@PathVariable("bookingId") Long bookingId, Model model) {
        try {
            Optional<Booking> bookingOptional = bookingService.findBookingById(bookingId);
            if (bookingOptional.isPresent()) {
                model.addAttribute("booking", bookingOptional.get());
            } else {
                model.addAttribute("error", "Booking not found with ID: " + bookingId);
            }
            return "booking-details";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading booking: " + e.getMessage());
            return "booking-details";
        }
    }

    // New feature: Delete booking
    @GetMapping("/bookings/{bookingId}/delete")
    public String deleteBooking(@PathVariable("bookingId") Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return "redirect:/bookings?deleted=true";
    }

}

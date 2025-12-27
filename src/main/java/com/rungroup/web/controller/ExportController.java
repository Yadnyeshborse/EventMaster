package com.rungroup.web.controller;

import com.rungroup.web.models.Booking;
import com.rungroup.web.repository.BookingRepo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ExportController {

    private final BookingRepo bookingRepo;

    @Autowired
    public ExportController(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    @GetMapping("/bookings/export")
    public void exportBookingsToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=bookings.csv");
        
        List<Booking> bookings = bookingRepo.findAll();
        
        PrintWriter writer = response.getWriter();
        
        // Write CSV header
        writer.println("ID,First Name,Last Name,Email,Phone,Alternate Phone,Gender,Address,Event Name,Event Type,Event Price,Event Start Time,Event End Time");
        
        // Write CSV data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Booking booking : bookings) {
            writer.print(booking.getId() + ",");
            writer.print((booking.getFirstName() != null ? booking.getFirstName() : "") + ",");
            writer.print((booking.getLastName() != null ? booking.getLastName() : "") + ",");
            writer.print((booking.getEmail() != null ? booking.getEmail() : "") + ",");
            writer.print((booking.getPhoneNo() != null ? booking.getPhoneNo() : "") + ",");
            writer.print((booking.getAlterNatePhoneNo() != null ? booking.getAlterNatePhoneNo() : "") + ",");
            writer.print((booking.getGender() != null ? booking.getGender() : "") + ",");
            writer.print((booking.getAddress() != null ? booking.getAddress().replace(",", ";") : "") + ",");
            
            if (booking.getEvent() != null) {
                writer.print((booking.getEvent().getName() != null ? booking.getEvent().getName() : "") + ",");
                writer.print((booking.getEvent().getType() != null ? booking.getEvent().getType() : "") + ",");
                writer.print((booking.getEvent().getPrice() != null ? booking.getEvent().getPrice() : "") + ",");
                writer.print((booking.getEvent().getStarttime() != null ? booking.getEvent().getStarttime().format(formatter) : "") + ",");
                writer.print((booking.getEvent().getEndtime() != null ? booking.getEvent().getEndtime().format(formatter) : ""));
            } else {
                writer.print(",,,,");
            }
            
            writer.println();
        }
        
        writer.flush();
        writer.close();
    }
}


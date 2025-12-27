package com.rungroup.web.controller;

import com.rungroup.web.models.Event;
import com.rungroup.web.repository.BookingRepo;
import com.rungroup.web.repository.ClubRepository;
import com.rungroup.web.repository.EventRepositery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final ClubRepository clubRepository;
    private final EventRepositery eventRepositery;
    private final BookingRepo bookingRepo;

    @Autowired
    public DashboardController(ClubRepository clubRepository, EventRepositery eventRepositery, BookingRepo bookingRepo) {
        this.clubRepository = clubRepository;
        this.eventRepositery = eventRepositery;
        this.bookingRepo = bookingRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalClubs = clubRepository.count();
        long totalEvents = eventRepositery.count();
        long totalBookings = bookingRepo.count();
        
        List<Event> allEvents = eventRepositery.findAll();
        
        // Get recent events (last 5)
        List<Event> recentEvents = allEvents.stream()
                .sorted((e1, e2) -> {
                    if (e1.getCreateOn() != null && e2.getCreateOn() != null) {
                        return e2.getCreateOn().compareTo(e1.getCreateOn());
                    }
                    return 0;
                })
                .limit(5)
                .collect(Collectors.toList());
        
        // Get upcoming events (events with start time in the future)
        LocalDateTime now = LocalDateTime.now();
        List<Event> upcomingEvents = allEvents.stream()
                .filter(event -> event.getStarttime() != null && 
                        event.getStarttime().isAfter(now))
                .sorted(Comparator.comparing(Event::getStarttime))
                .limit(5)
                .collect(Collectors.toList());

        model.addAttribute("totalClubs", totalClubs);
        model.addAttribute("totalEvents", totalEvents);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("recentEvents", recentEvents);
        model.addAttribute("upcomingEvents", upcomingEvents);
        
        return "dashboard";
    }
}


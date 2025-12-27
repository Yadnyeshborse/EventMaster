package com.rungroup.web.controller;

import com.rungroup.web.models.Club;
import com.rungroup.web.models.Event;
import com.rungroup.web.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
public class EventController {

    private EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping("/clubs/{id}/events/new")
    public String showCreateEventForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("id", id);
        return "event-create";  // A new HTML page for creating an event
    }

    @PostMapping("/clubs/{id}/events")
    public String createEvent(@PathVariable("id") Long id, 
                             @Valid @ModelAttribute("event") Event event,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("id", id);
            return "event-create";
        }
        
        try {
            eventService.createEvent(id, event);
            return "redirect:/clubs/" + id + "?eventCreated=true";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create event: " + e.getMessage());
            model.addAttribute("id", id);
            return "event-create";
        }
    }

    //________________________________________________________________________________________

    //particular event id
    @GetMapping("/events/{eventId}")
    public String getEventDetails(@PathVariable("eventId") Long eventId, Model model) {
        try {
            Event event = eventService.findEventById(eventId);
            if (event == null) {
                model.addAttribute("error", "Event not found");
                return "redirect:/getAllEvents";
            }
            model.addAttribute("event", event);
            // Add booking count for the event
            int bookingCount = event.getBookings() != null ? event.getBookings().size() : 0;
            model.addAttribute("bookingCount", bookingCount);
            return "event-details";  // This should be the name of your Thymeleaf template for showing event details
        } catch (Exception e) {
            model.addAttribute("error", "Error loading event: " + e.getMessage());
            return "redirect:/getAllEvents";
        }
    }

    //______________________________________________________________________________________________________

    @GetMapping("/getAllEvents")
    public String getAllEvents(@RequestParam(value = "sort", required = false, defaultValue = "name") String sort,
                              @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
                              @RequestParam(value = "type", required = false) String type,
                              @RequestParam(value = "minPrice", required = false) String minPrice,
                              @RequestParam(value = "maxPrice", required = false) String maxPrice,
                              Model model){
        List<Event> events = eventService.findAll();
        
        // Filter by type if provided
        if (type != null && !type.trim().isEmpty()) {
            events = events.stream()
                    .filter(e -> e.getType() != null && e.getType().equalsIgnoreCase(type))
                    .toList();
        }
        
        // Filter by price range if provided
        if (minPrice != null && !minPrice.trim().isEmpty()) {
            try {
                double min = Double.parseDouble(minPrice);
                events = events.stream()
                        .filter(e -> {
                            if (e.getPrice() != null && !e.getPrice().trim().isEmpty()) {
                                try {
                                    double price = Double.parseDouble(e.getPrice().replaceAll("[^0-9.]", ""));
                                    return price >= min;
                                } catch (NumberFormatException ex) {
                                    return true;
                                }
                            }
                            return true;
                        })
                        .toList();
            } catch (NumberFormatException e) {
                // Invalid price, ignore filter
            }
        }
        
        if (maxPrice != null && !maxPrice.trim().isEmpty()) {
            try {
                double max = Double.parseDouble(maxPrice);
                events = events.stream()
                        .filter(e -> {
                            if (e.getPrice() != null && !e.getPrice().trim().isEmpty()) {
                                try {
                                    double price = Double.parseDouble(e.getPrice().replaceAll("[^0-9.]", ""));
                                    return price <= max;
                                } catch (NumberFormatException ex) {
                                    return true;
                                }
                            }
                            return true;
                        })
                        .toList();
            } catch (NumberFormatException e) {
                // Invalid price, ignore filter
            }
        }
        
        // Sort events
        final String sortField = sort;
        final String sortOrder = order;
        events = events.stream().sorted((e1, e2) -> {
            int result = 0;
            switch (sortField) {
                case "name":
                    result = (e1.getName() != null ? e1.getName() : "").compareToIgnoreCase(e2.getName() != null ? e2.getName() : "");
                    break;
                case "date":
                    if (e1.getStarttime() != null && e2.getStarttime() != null) {
                        result = e1.getStarttime().compareTo(e2.getStarttime());
                    }
                    break;
                case "price":
                    try {
                        double p1 = e1.getPrice() != null && !e1.getPrice().trim().isEmpty() ? 
                                Double.parseDouble(e1.getPrice().replaceAll("[^0-9.]", "")) : 0;
                        double p2 = e2.getPrice() != null && !e2.getPrice().trim().isEmpty() ? 
                                Double.parseDouble(e2.getPrice().replaceAll("[^0-9.]", "")) : 0;
                        result = Double.compare(p1, p2);
                    } catch (NumberFormatException ex) {
                        result = 0;
                    }
                    break;
                default:
                    result = 0;
            }
            return "desc".equalsIgnoreCase(sortOrder) ? -result : result;
        }).toList();
        
        // Get unique event types for filter dropdown
        List<String> eventTypes = eventService.findAll().stream()
                .map(Event::getType)
                .filter(t -> t != null && !t.trim().isEmpty())
                .distinct()
                .sorted()
                .toList();
        
        model.addAttribute("event", events);
        model.addAttribute("eventTypes", eventTypes);
        model.addAttribute("currentSort", sort);
        model.addAttribute("currentOrder", order);
        model.addAttribute("selectedType", type);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "getAllEvent";
    }

    //____________________________________________________________________________________________

    @GetMapping("/event/search")
    public String searchEvents(@RequestParam(value = "query", required = false) String query, Model model) {
        if (query == null || query.trim().isEmpty()) {
            return "redirect:/getAllEvents";
        }
        List<Event> events = eventService.searchEvents(query);
        model.addAttribute("events", events);
        model.addAttribute("searchQuery", query);
        return "getAllEvent"; // Fixed: should return getAllEvent, not getAllClub
    }

    //______________________________________________________________________________________________________

    @GetMapping("/event/{id}/edit")
    public String getAllEvent(@PathVariable("id") Long id,Model model){
        Event event=eventService.findEventById(id);
        model.addAttribute("event",event);
        return "event-edit";
    }

    @PostMapping("/event/{id}/edit")
    public String editEventForm(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("event") Event event,
            BindingResult result) {

        if (result.hasErrors()) {
            return "event-edit";
        }

        event.setId(id);
        eventService.updateEvent(event);

        return "redirect:/events/" + id;
    }


    //________________________________________________________________________________________________________

    @GetMapping("/event/{id}/delete")
    public String deleteEvent(@PathVariable("id") Long id) {
        eventService.deleteEvent(id);
        return "redirect:/getAllEvents"; // Redirect to the event listing page
    }




}

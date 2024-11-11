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
    public String createEvent(@PathVariable("id") Long id, @ModelAttribute("event") Event event) {
        eventService.createEvent(id, event);
        return "redirect:/clubs/" + id;  // Redirect back to the club page
    }

    //________________________________________________________________________________________

    //particular event id
    @GetMapping("/events/{eventId}")
    public String getEventDetails(@PathVariable("eventId") Long eventId, Model model) {
        Event event = eventService.findEventById(eventId);
        model.addAttribute("event", event);
        return "event-details";  // This should be the name of your Thymeleaf template for showing event details
    }

    //______________________________________________________________________________________________________

    @GetMapping("/getAllEvents")
    public String getAllEvents(Model model){
        List<Event> ev=eventService.findAll();
        model.addAttribute("event",ev);
        return "getAllEvent";
    }

    //____________________________________________________________________________________________

    @GetMapping("/event/search")
    public String searchEvents(@RequestParam(value = "query") String query, Model model) {
        List<Event> events = eventService.searchEvents(query);
        model.addAttribute("events", events); // Ensure this matches with the Thymeleaf variable
        return "getAllClub";
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

package com.rungroup.web.service;

import com.rungroup.web.models.Club;
import com.rungroup.web.models.Event;
import com.rungroup.web.repository.ClubRepository;
import com.rungroup.web.repository.EventRepositery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private EventRepositery eventRepositery;

    private ClubRepository clubRepository;

    @Autowired
    public EventService(EventRepositery eventRepositery, ClubRepository clubRepository) {
        this.eventRepositery = eventRepositery;
        this.clubRepository = clubRepository;
    }

    public void createEvent(Long  id, Event event) {
        Club club = clubRepository.findById(id).orElseThrow(() -> new RuntimeException("Club not found"));
        event.setClub(club);  // Associate the event with the club
        eventRepositery.save(event);  // Save the event with the association
    }



    public Event findEventById(Long eventId) {
        return eventRepositery.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event with ID " + eventId + " not found"));
    }

    public List<Event> findAll() {
        return eventRepositery.findAll();
    }

    public List<Event> searchEvents(String query) {
        return eventRepositery.searchEvents(query);
    }

    public Event findClubById(Long id) {
        return eventRepositery.findEventById(id);
    }

    public Event updateEvent(Event event) {
        Optional<Event> existingEvent = eventRepositery.findById(event.getId());

        if (existingEvent.isPresent()) {
            Event eventToUpdate = existingEvent.get();
            eventToUpdate.setName(event.getName());
            eventToUpdate.setType(event.getType());
            eventToUpdate.setStarttime(event.getStarttime());
            eventToUpdate.setEndtime(event.getEndtime());
            // Add any other fields that need updating here

            return eventRepositery.save(eventToUpdate);
        } else {
            throw new IllegalArgumentException("Event not found with id: " + event.getId());
        }
    }

    public void deleteEvent(Long id) {
        eventRepositery.deleteById(id);
    }

}

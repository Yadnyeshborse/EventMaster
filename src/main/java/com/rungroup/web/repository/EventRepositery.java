package com.rungroup.web.repository;

import com.rungroup.web.models.Club;
import com.rungroup.web.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepositery extends JpaRepository<Event, Long> {
    List<Event> findByClub(Club club); // Custom method to fetch events by club

    @Query("select c from Event c where c.name LIKE CONCAT('%',:query,'%')")
    List<Event> searchEvents(String query);

    Event findEventById(Long id);
}

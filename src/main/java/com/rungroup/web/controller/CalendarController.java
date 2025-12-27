package com.rungroup.web.controller;

import com.rungroup.web.models.Event;
import com.rungroup.web.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CalendarController {

    private final EventService eventService;

    @Autowired
    public CalendarController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/calendar")
    public String calendarView(@RequestParam(value = "year", required = false) Integer year,
                              @RequestParam(value = "month", required = false) Integer month,
                              Model model) {
        LocalDate today = LocalDate.now();
        int currentYear = year != null ? year : today.getYear();
        int currentMonth = month != null ? month : today.getMonthValue();
        
        YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        
        // Get all events
        List<Event> allEvents = eventService.findAll();
        
        // Filter events for the current month
        List<Event> monthEvents = allEvents.stream()
                .filter(event -> event.getStarttime() != null)
                .filter(event -> {
                    LocalDate eventDate = event.getStarttime().toLocalDate();
                    return !eventDate.isBefore(firstDay) && !eventDate.isAfter(lastDay);
                })
                .collect(Collectors.toList());
        
        // Group events by date
        Map<LocalDate, List<Event>> eventsByDate = monthEvents.stream()
                .collect(Collectors.groupingBy(event -> event.getStarttime().toLocalDate()));
        
        // Build calendar grid
        List<List<LocalDate>> calendarGrid = new ArrayList<>();
        List<LocalDate> currentWeek = new ArrayList<>();
        
        // Add empty cells for days before the first day of month
        DayOfWeek firstDayOfWeek = firstDay.getDayOfWeek();
        int daysToAdd = firstDayOfWeek.getValue() % 7; // Convert to Sunday = 0
        for (int i = 0; i < daysToAdd; i++) {
            currentWeek.add(null);
        }
        
        // Add all days of the month
        LocalDate currentDate = firstDay;
        while (!currentDate.isAfter(lastDay)) {
            currentWeek.add(currentDate);
            if (currentWeek.size() == 7) {
                calendarGrid.add(new ArrayList<>(currentWeek));
                currentWeek.clear();
            }
            currentDate = currentDate.plusDays(1);
        }
        
        // Add remaining empty cells for the last week
        while (currentWeek.size() < 7 && !currentWeek.isEmpty()) {
            currentWeek.add(null);
        }
        if (!currentWeek.isEmpty()) {
            calendarGrid.add(currentWeek);
        }
        
        model.addAttribute("year", currentYear);
        model.addAttribute("month", currentMonth);
        model.addAttribute("yearMonth", yearMonth);
        model.addAttribute("firstDay", firstDay);
        model.addAttribute("lastDay", lastDay);
        model.addAttribute("eventsByDate", eventsByDate);
        model.addAttribute("monthEvents", monthEvents);
        model.addAttribute("monthName", yearMonth.getMonth().toString());
        model.addAttribute("calendarGrid", calendarGrid);
        model.addAttribute("today", today);
        
        // Calculate previous and next month
        YearMonth prevMonth = yearMonth.minusMonths(1);
        YearMonth nextMonth = yearMonth.plusMonths(1);
        model.addAttribute("prevYear", prevMonth.getYear());
        model.addAttribute("prevMonth", prevMonth.getMonthValue());
        model.addAttribute("nextYear", nextMonth.getYear());
        model.addAttribute("nextMonth", nextMonth.getMonthValue());
        
        return "calendar";
    }
}


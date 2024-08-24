package org.rares.eventservice.controller;

import jakarta.validation.Valid;
import org.rares.eventservice.model.Event;
import org.rares.eventservice.service.EventService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable int id) {
        Optional<Event> event = eventService.getEventById(id);
        return event.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/organizer/all-events/{id}")
    public ResponseEntity<List<Event>> getEventsByOrganizerId(@PathVariable int id) {
        List<Event> events = eventService.getEventsByOrganizerId(id);
        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(events, HttpStatus.OK);
        }
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Event>> getEventsByTitle(@PathVariable String title) {
        List<Event> events = eventService.getEventsByTitle(title);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Event>> getEventsByLocation(@PathVariable String location) {
        List<Event> events = eventService.getEventsByLocation(location);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/before-date")
    public ResponseEntity<List<Event>> getEventsBeforeDate(@RequestParam("date") String endDate) {
        try {
            // Parse date using OffsetDateTime
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(endDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            // Convert to LocalDateTime
            LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();

            List<Event> events = eventService.getEventsByDateBefore(localDateTime);
            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(events, HttpStatus.OK);
            }
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<Event>> getRecommendedEvents() {
        List<Event> allEvents = eventService.getAllEvents();
        Collections.shuffle(allEvents); // Amestecă lista de evenimente
        List<Event> recommendedEvents = allEvents.stream().limit(4).collect(Collectors.toList());
        return new ResponseEntity<>(recommendedEvents, HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity<Event> updateEvent(@RequestBody Event event) {
        try {
            Event updatedEvent = eventService.updateEvent(event);
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable int id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

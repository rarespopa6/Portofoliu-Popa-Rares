package org.rares.eventservice.service;

import org.rares.eventservice.model.Event;
import org.rares.eventservice.model.TicketCategory;
import org.rares.eventservice.repository.EventRepo;
import org.rares.eventservice.repository.TicketCategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class EventService {

    @Autowired
    private EventRepo eventRepository;

    @Autowired
    private TicketCategoryRepo ticketCategoryRepo;

    public Event createEvent(Event eventDTO) {
        Event event = new Event();
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setEventDate(eventDTO.getEventDate());
        event.setOrganizerId(eventDTO.getOrganizerId());
        event.setBanner(eventDTO.getBanner());
        event.setLocation(eventDTO.getLocation());
        event.setTicketCategories(eventDTO.getTicketCategories());

        Event savedEvent = eventRepository.save(event);

        // Setează referința către Event în fiecare TicketCategory
        for (TicketCategory ticketCategory : eventDTO.getTicketCategories()) {
            ticketCategory.setEventId(savedEvent.getId());
        }

        // Salvează TicketCategory-urile
        ticketCategoryRepo.saveAll(eventDTO.getTicketCategories());

        return savedEvent;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(int id) {
        return eventRepository.findById(id);
    }

    public List<Event> getEventsByTitle(String title) {
        return eventRepository.findEventsByTitleContainingIgnoreCase(title);
    }

    public List<Event> getEventsByLocation(String location) {
        return eventRepository.findEventsByLocationContainingIgnoreCase(location);
    }

    public Event updateEvent(Event updatedEvent) {
        // Caută evenimentul existent în baza de date
        Event existingEvent = eventRepository.findById(updatedEvent.getId())
                .orElseThrow(() -> new IllegalStateException("Event not found"));

        // Actualizează câmpurile individuale
        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setEventDate(updatedEvent.getEventDate());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setBanner(updatedEvent.getBanner());
        existingEvent.setStatus(updatedEvent.getStatus());

        // Actualizează colecția de categorii de bilete
        // Șterge categoriile de bilete care nu mai sunt în lista actualizată
        existingEvent.getTicketCategories().removeIf(existingCategory ->
                updatedEvent.getTicketCategories().stream()
                        .noneMatch(updatedCategory -> updatedCategory.getId() == existingCategory.getId())
        );

        // Actualizează sau adaugă categoriile de bilete
        for (TicketCategory updatedCategory : updatedEvent.getTicketCategories()) {
            Optional<TicketCategory> existingCategoryOpt = existingEvent.getTicketCategories().stream()
                    .filter(existingCategory -> existingCategory.getId() == updatedCategory.getId())
                    .findFirst();

            if (existingCategoryOpt.isPresent()) {
                TicketCategory existingCategory = existingCategoryOpt.get();
                existingCategory.setCategory(updatedCategory.getCategory());
                existingCategory.setNumberOfTickets(updatedCategory.getNumberOfTickets());
                existingCategory.setPrice(updatedCategory.getPrice());
            } else {
                updatedCategory.setEventId(existingEvent.getId()); // Asigură-te că relația este setată corect
                existingEvent.getTicketCategories().add(updatedCategory);
            }
        }

        // Salvează modificările
        return eventRepository.save(existingEvent);
    }

    public void deleteEvent(int id) {
        eventRepository.deleteById(id);
    }

    public List<Event> getEventsByOrganizerId(int id) {
        return eventRepository.findEventsByOrganizerId(id);
    }

    public List<Event> getEventsByDateBefore(LocalDateTime endDate) {
        return eventRepository.findEventsByEventDateBefore(endDate);
    }
}

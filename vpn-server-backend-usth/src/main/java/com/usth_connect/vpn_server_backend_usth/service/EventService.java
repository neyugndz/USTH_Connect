package com.usth_connect.vpn_server_backend_usth.service;

import com.usth_connect.vpn_server_backend_usth.entity.Notification;
import com.usth_connect.vpn_server_backend_usth.entity.Organizer;
import com.usth_connect.vpn_server_backend_usth.dto.EventDTO;
import com.usth_connect.vpn_server_backend_usth.entity.MapLocation;
import com.usth_connect.vpn_server_backend_usth.entity.schedule.Event;
import com.usth_connect.vpn_server_backend_usth.repository.EventRepository;
import com.usth_connect.vpn_server_backend_usth.repository.MapLocationRepository;
import com.usth_connect.vpn_server_backend_usth.repository.OrganizerRepository;
import com.usth_connect.vpn_server_backend_usth.repository.ScheduleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private OrganizerService organizerService;

    @Autowired
    private MapLocationRepository mapLocationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(EventService.class.getName());
    @Autowired
    private OrganizerRepository organizerRepository;

    /**
     * Save or update an event based on its Google Calendar ID.
     *
     */
    @Transactional
    public void saveOrUpdateEvent(com.google.api.services.calendar.model.Event event) {
        // Validate event object
        if (event == null) {
            LOGGER.warning("Received a null event. Skipping processing.");
            return;
        }

        // Validate required fields
        if (event.getId() == null || event.getSummary() == null) {
            LOGGER.warning("Skipping event due to missing required fields: GoogleEventId or EventName is null.");
            return;
        }

        // Check if the event already exists by googleEventId
        Optional<Event> existingEventOptional = eventRepository.findByGoogleEventId(event.getId());

        if (existingEventOptional.isPresent()) {
            Event existingEvent = existingEventOptional.get();

            LOGGER.info("Event already exists: " + event.getSummary());

            // Update the existing event with new information
            existingEvent.setEventName(event.getSummary());
            existingEvent.setEventDescription(event.getDescription() != null ? event.getDescription() : "No description");

            // Parse and set start and end time
            if (event.getStart().getDateTime() != null) {
                OffsetDateTime startTime = OffsetDateTime.parse(event.getStart().getDateTime().toStringRfc3339(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                existingEvent.setEventStart(startTime.toLocalDateTime());
            } else {
                LOGGER.warning("Event start time is missing or null. Skipping update for start time.");
            }

            if (event.getEnd().getDateTime() != null) {
                OffsetDateTime endDateTime = OffsetDateTime.parse(event.getEnd().getDateTime().toStringRfc3339(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                existingEvent.setEventEnd(endDateTime.toLocalDateTime());
            } else {
                LOGGER.warning("Event end time is missing or null. Skipping update for end time.");
            }

            // Update location, organizer
            String locationString = event.getLocation();
            LOGGER.info("Processing location: " + locationString);

            // Set the locationValue in the event
            existingEvent.setLocationValue(locationString);

            // Find the MapLocation by the location string (or create it if it doesn't exist)
            MapLocation mapLocation = mapLocationRepository.findByLocation(locationString)
                    .orElseGet(() -> {
                        MapLocation newLocation = new MapLocation();
                        newLocation.setLocation(locationString);
                        newLocation.setLocationValue(locationString);
                        return mapLocationRepository.save(newLocation);
                    });

            // Assign the MapLocation to the event
            existingEvent.setLocation(mapLocation);
            LOGGER.info("Location assigned: " + mapLocation.getLocation());

//
            // Update organizer
            if (event.getOrganizer() != null && event.getOrganizer().getEmail() != null) {
                Organizer organizer = organizerRepository.findByEmail(event.getOrganizer().getEmail())
                        .orElseGet(() -> {
                            Organizer newOrganizer = new Organizer();
                            newOrganizer.setEmail(event.getOrganizer().getEmail());
                            newOrganizer.setName(event.getOrganizer().getDisplayName() != null ? event.getOrganizer().getDisplayName() : "Unknown");
                            return organizerRepository.save(newOrganizer);
                        });
                existingEvent.setOrganizer(organizer);
            }

            // Save the updated event to the database
            eventRepository.save(existingEvent);
        } else {
            LOGGER.info("Event does not exist. Creating new event: " + event.getSummary());

            // Create a new event entity and set values
            Event newEvent = new Event();
            newEvent.setEventName(event.getSummary());
            newEvent.setEventDescription(event.getDescription() != null ? event.getDescription() : "No description");
            newEvent.setGoogleEventId(event.getId());
            LOGGER.info("Google Event ID: " + newEvent.getGoogleEventId());

            if (event.getStart().getDateTime() != null) {
                OffsetDateTime startTime = OffsetDateTime.parse(event.getStart().getDateTime().toStringRfc3339(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                newEvent.setEventStart(startTime.toLocalDateTime());
            } else {
                LOGGER.warning("Skipping event creation due to missing start time.");
                return; // Stop processing incomplete events
            }

            if (event.getEnd().getDateTime() != null) {
                OffsetDateTime endDateTime = OffsetDateTime.parse(event.getEnd().getDateTime().toStringRfc3339(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                newEvent.setEventEnd(endDateTime.toLocalDateTime());
            } else {
                LOGGER.warning("Skipping event creation due to missing end time.");
                return; // Stop processing incomplete events
            }

            // Set location and organizer as above
            String locationString = event.getLocation();
            LOGGER.info("Processing location: " + locationString);

            // Set the locationValue in the event
            newEvent.setLocationValue(locationString);

            // Find the MapLocation by the location string (or create it if it doesn't exist)
            MapLocation mapLocation = mapLocationRepository.findByLocation(locationString)
                    .orElseGet(() -> {
                        MapLocation newLocation = new MapLocation();
                        newLocation.setLocation(locationString);
                        newLocation.setLocationValue(locationString);
                        return mapLocationRepository.save(newLocation);
                    });

            // Assign the MapLocation to the event
            newEvent.setLocation(mapLocation);
            LOGGER.info("Location assigned: " + mapLocation.getLocation());

            if (event.getOrganizer() != null && event.getOrganizer().getEmail() != null) {
                Organizer organizer = organizerRepository.findByEmail(event.getOrganizer().getEmail())
                        .orElseGet(() -> {
                            Organizer newOrganizer = new Organizer();
                            newOrganizer.setEmail(event.getOrganizer().getEmail());
                            newOrganizer.setName(event.getOrganizer().getDisplayName() != null ? event.getOrganizer().getDisplayName() : "Unknown");
                            return organizerRepository.save(newOrganizer);
                        });
                newEvent.setOrganizer(organizer);
            }

            // Save the new event to the database
            eventRepository.save(newEvent);
        }
    }


    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private EventDTO convertToDTO(com.usth_connect.vpn_server_backend_usth.entity.schedule.Event event) {
        EventDTO dto = new EventDTO();
        dto.setEventId(event.getEventId());
        dto.setEventName(event.getEventName());
        dto.setEventDescription(event.getEventDescription());
        dto.setEventStart(event.getEventStart());
        dto.setEventEnd(event.getEventEnd());

        // Set the location from the MapLocation entity
        dto.setLocation(event.getLocation() != null ? event.getLocation() : null);

        // Set the organizer details from the Organizer entity
        if(event.getOrganizer() != null) {
            dto.setOrganizer_email(event.getOrganizer().getEmail());
            dto.setOrganizer_name(event.getOrganizer().getName());
        }
        return dto;
    }




    public List<Event> getEventsByOrganizer(String studyYear, String major) {
        int organizerId = organizerService.calculateOrganizerId(studyYear, major);

        return eventRepository.findByOrganizerId(organizerId);
    }

}

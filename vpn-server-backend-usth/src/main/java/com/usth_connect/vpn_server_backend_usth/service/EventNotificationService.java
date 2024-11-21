package com.usth_connect.vpn_server_backend_usth.service;

import com.usth_connect.vpn_server_backend_usth.dto.EventDTO;
import com.usth_connect.vpn_server_backend_usth.entity.MapLocation;
import com.usth_connect.vpn_server_backend_usth.entity.Notification;
import com.usth_connect.vpn_server_backend_usth.entity.schedule.Event;
import com.usth_connect.vpn_server_backend_usth.repository.EventRepository;
import com.usth_connect.vpn_server_backend_usth.repository.MapLocationRepository;
import com.usth_connect.vpn_server_backend_usth.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Logger;

@Service
public class EventNotificationService {
    private static final Logger LOGGER = Logger.getLogger(EventNotificationService.class.getName());
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MapLocationRepository mapLocationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public void notifyUserOnChange(com.google.api.services.calendar.model.Event googleEvent, Event localEvent) {
        // Map the Google event to the DTO
        EventDTO googleEventDTO = mapToEventDTO(googleEvent);

        // Compare oldEvent and newEvent for changes
        StringBuilder changeMessage = new StringBuilder("The following changes were made to the event: "
                + googleEventDTO.getEventName() + " ");

        // Compare event name (summary)
        if (googleEventDTO.getEventName() != null && !googleEventDTO.getEventName().equals(localEvent.getEventName())) {
            changeMessage.append("Event name changed from '").append(localEvent.getEventName())
                    .append("' to '").append(googleEventDTO.getEventName()).append("'. ");
        }

        // Compare event description
        if (googleEventDTO.getEventDescription() != null && !googleEventDTO.getEventDescription().equals(localEvent.getEventDescription())) {
            changeMessage.append("Event description changed from '")
                    .append(localEvent.getEventDescription() != null ? localEvent.getEventDescription() : "N/A")
                    .append("' to '")
                    .append(googleEventDTO.getEventDescription()).append("'. ");
        }

        // Compare event start time
        if (googleEventDTO.getEventStart() != null && !googleEventDTO.getEventStart().equals(localEvent.getEventStart())) {
            changeMessage.append("Event start time changed from '")
                    .append(localEvent.getEventStart() != null ? localEvent.getEventStart().toString() : "N/A")
                    .append("' to '")
                    .append(googleEventDTO.getEventStart()).append("'. ");
        }

        // Compare event end time
        if (googleEventDTO.getEventEnd() != null && !googleEventDTO.getEventEnd().equals(localEvent.getEventEnd())) {
            changeMessage.append("Event end time changed from '")
                    .append(localEvent.getEventEnd() != null ? localEvent.getEventEnd().toString() : "N/A")
                    .append("' to '")
                    .append(googleEventDTO.getEventEnd()).append("'. ");
        }

        // Compare event location
        if (googleEventDTO.getLocationValue() != null && !googleEventDTO.getLocationValue().trim().equals(localEvent.getLocationValue().trim())) {
            changeMessage.append("Event location changed from '")
                    .append(localEvent.getLocationValue())
                    .append("' to '")
                    .append(googleEventDTO.getLocationValue()).append("'. ");
        }

        if (!changeMessage.toString().equals("The following changes were made to the event: "
                + googleEventDTO.getEventName() + " ")) {
            // Create the notification
            Notification notification = new Notification();
            notification.setMessage(changeMessage.toString());
            notification.setCreatedAt(LocalDateTime.now());
            notification.setRead(false);

            // Save notification
            notificationRepository.save(notification);

            // Directly associate the notification with the event
            localEvent.getNotifications().add(notification);

            // Save the updated event
            eventRepository.save(localEvent);
        }
    }

    public EventDTO mapToEventDTO(com.google.api.services.calendar.model.Event googleEvent) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventName(googleEvent.getSummary());
        eventDTO.setEventDescription(googleEvent.getDescription());
        eventDTO.setEventStart(convertToLocalDateTime(googleEvent.getStart().getDateTime()));
        eventDTO.setEventEnd(convertToLocalDateTime(googleEvent.getEnd().getDateTime()));
//        eventDTO.setLocation(getMapLocationFromString(googleEvent.getLocation()));
        eventDTO.setLocationValue(googleEvent.getLocation() != null ? googleEvent.getLocation() : "No Location Provided");
        return eventDTO;
    }

//    private MapLocation getMapLocationFromString(String locationName) {
//        // Check if location exists in the database
//        return mapLocationRepository.findByLocation(locationName)
//                .orElseGet(() -> {
//                    // If not found, create a new MapLocation
//                    MapLocation newLocation = new MapLocation();
//                    newLocation.setLocation(locationName);
//                    mapLocationRepository.save(newLocation);
//                    return newLocation;
//                });
//    }

    // Convert from LocalDateTime to Date and Reverse
    private LocalDateTime convertToLocalDateTime(com.google.api.client.util.DateTime googleDateTime) {
        if (googleDateTime == null) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(googleDateTime.getValue());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}

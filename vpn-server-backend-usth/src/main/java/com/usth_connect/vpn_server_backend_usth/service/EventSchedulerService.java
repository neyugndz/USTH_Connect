package com.usth_connect.vpn_server_backend_usth.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.auto.value.AutoOneOf;
import com.usth_connect.vpn_server_backend_usth.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class EventSchedulerService {
    private static final Logger LOGGER = Logger.getLogger(EventSchedulerService.class.getName());

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventNotificationService eventNotificationService;

    @Scheduled(fixedRate = 600000)
    public void syncGoogleCalendar() {
        LOGGER.info("Starting Google Calendar sync...");

        try {
            // Define the calendar IDs to fetch events from
            List<String> calendarIds = Arrays.asList(
                    "ict.usthedu@gmail.com",
                    "i7p4ol56gi8sqe0dq7uc3m0o4c@group.calendar.google.com",
                    "b69a46455e0f40497a09a7bdb1fb4a3651ee872c8371238487159d5b17944e54@group.calendar.google.com"
            );

            // Get the current time to define the time range
            DateTime timeMin = googleCalendarService.getStartOfWeek();
            DateTime timeMax = googleCalendarService.getEndOfWeek();

            LOGGER.info("Fetching events from " + timeMin + " to " + timeMax);

            // Fetch events from Google Calendar
            List<Event> googleEvents = googleCalendarService.getMultipleCalendarEvents(calendarIds, timeMin, timeMax);

            // Process the events and save them
            int savedEventsCount = 0;
            for (Event googleEvent : googleEvents) {
                // Check if the event already exists in the db
                Optional<com.usth_connect.vpn_server_backend_usth.entity.schedule.Event> existingEvent = eventRepository.findByGoogleEventId(googleEvent.getId());

                if (existingEvent.isPresent()) {
                    // Event exists, compare with existing local event and notify on changes
                    com.usth_connect.vpn_server_backend_usth.entity.schedule.Event localEvent = existingEvent.get();
                    eventNotificationService.notifyUserOnChange(googleEvent, localEvent);

                    // Update the existing event with new data
                    eventService.saveOrUpdateEvent(googleEvent);
                    LOGGER.info("Updated Event ID: " + googleEvent.getId());
                } else {
                    // If not exist, create a new one
                    eventService.saveOrUpdateEvent(googleEvent);
                    LOGGER.info("Created new Event ID: "+ googleEvent.getId());
                }

                savedEventsCount++;
            }

            // Log the number of events processed
            LOGGER.info("Sync complete. Total events processed: " + savedEventsCount);

        } catch (IOException e) {
            LOGGER.severe("Error during Google Calendar sync: " + e.getMessage());
        }
    }
}

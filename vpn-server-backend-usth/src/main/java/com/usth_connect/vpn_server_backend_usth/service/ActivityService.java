package com.usth_connect.vpn_server_backend_usth.service;

import com.usth_connect.vpn_server_backend_usth.entity.moodle.Activity;
import com.usth_connect.vpn_server_backend_usth.entity.moodle.Resource;
import com.usth_connect.vpn_server_backend_usth.repository.ActivityRepository;
import com.usth_connect.vpn_server_backend_usth.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.logging.Logger;

@Service
public class ActivityService {
    private static final Logger LOGGER = Logger.getLogger(ActivityService.class.getName());

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private MoodleService moodleService;

    // Save activity from the Course
    public List<Activity> saveActivity(Long courseId) {
        LOGGER.info("Started saving activities for course ID: " + courseId);

        List<Map<String, Object>> activities = moodleService.fetchActivities(courseId);
        if (activities == null || activities.isEmpty()) {
            LOGGER.warning("No activities found for course ID: " + courseId);
            throw new IllegalArgumentException("No activities found for course ID: " + courseId);
        }

        List<Activity> savedActivities = new ArrayList<>();

        for (Map<String, Object> activityData : activities) {
            LOGGER.info("Processing activity ID: " + activityData.get("id"));

            Long activityId = ((Number) activityData.get("id")).longValue();

            // Check if the activity already exists in the database
            Optional<Activity> existingActivityOpt = activityRepository.findByActivityIdAndCourseId(activityId, courseId);
            Activity activity;
            if (existingActivityOpt.isPresent()) {
                // If activity exists, use the existing one
                activity = existingActivityOpt.get();
                LOGGER.info("Activity exists in the database, updating it.");
            } else {
                // If not, create a new Activity
                activity = new Activity();
                activity.setActivityId(activityId);
                activity.setCourseId(courseId);  // Set the courseId directly
                activity.setResources(new ArrayList<>());
                LOGGER.info("Creating new activity with ID: " + activityId);
            }

            try {
                // Set activity properties
                activity.setActivityName((String) activityData.get("name"));
                activity.setActivityType((String) activityData.get("modname"));
                activity.setCompletionStatus((String) activityData.get("completionstatus"));

                // Save resources related to this activity
                List<Map<String, Object>> resourceDataList = (List<Map<String, Object>>) activityData.getOrDefault("modules", new ArrayList<>());

                // Update or create new resources and safely replace the existing collection
                LOGGER.info("Resource data for activity ID: " + activityId + " - " + resourceDataList);
                activity.setResources(saveResources(activity, resourceDataList, courseId));

                // Save the activity to the database
                savedActivities.add(activityRepository.save(activity));
                LOGGER.info("Activity saved successfully with ID: " + activityId);
            } catch (Exception e) {
                LOGGER.severe("Error occurred while processing activity ID: " + activityId + ". Error: " + e.getMessage());
            }
        }
        LOGGER.info("Finished saving activities for course ID: " + courseId);
        return savedActivities;
    }

    // Save resources for an activity
    private List<Resource> saveResources(Activity activity, List<Map<String, Object>> resourceDataList, Long courseId) {
        List<Resource> resources = new ArrayList<>();
        for (Map<String, Object> resourceData : resourceDataList) {
            try {
                LOGGER.info("Saving resource: " + resourceData.get("name"));

                // Extract data from resourceData Map
                Long resourceId = ((Number) resourceData.get("id")).longValue();
                String type = (String) resourceData.get("modname");
                String name = (String) resourceData.get("name");
                String fileUrl = (String) resourceData.get("url");

                // Log for debugging
                LOGGER.info("Resource ID: " + resourceId);
                LOGGER.info("Resource Type: " + type);
                LOGGER.info("Resource Name: " + name);
                LOGGER.info("Resource File URL: " + fileUrl);

                // Create a new Resource entity and set the values
                Resource resource = new Resource();
                resource.setActivity(activity); // Associate resource with the activity
                resource.setId(resourceId);  // Set resource ID
                resource.setType(type);  // Set resource type
                resource.setName(name);  // Set resource name
                resource.setFileUrl(fileUrl);  // Set file URL for the resource
                resource.setCourseId(courseId);

                resources.add(resourceRepository.save(resource));
            } catch (Exception e) {
                LOGGER.severe("Error occurred while saving resource: " + resourceData.get("name") + ". Error: " + e.getMessage());
            }
        }
        return resources;
    }

    // Fetch resources related to an activity
    public List<Resource> getResourcesForActivity(Long activityId, Long courseId) {
        LOGGER.info("Fetching resources for activity ID: " + activityId + " and course ID: " + courseId);

        // Fetch the resources from the database
        List<Resource> resources = resourceRepository.findByActivity_ActivityIdAndActivity_CourseId(activityId, courseId);

        if (resources == null || resources.isEmpty()) {
            LOGGER.warning("No resources found for activity ID: " + activityId + " and course ID: " + courseId);
            throw new IllegalArgumentException("No resources found for activity ID: " + activityId + " and course ID: " + courseId);
        }

        LOGGER.info("Successfully fetched " + resources.size() + " resources for activity ID: " + activityId);
        return resources;
    }

    // Get all activities
    public List<Activity> getAllActivities(Long courseId) {
        return activityRepository.findByCourseId(courseId);
    }

    // Get activity based on id of activity and course from Database
    public Activity getActivity(Long activityId, Long courseId) {
        Optional<Activity> activity = activityRepository.findByActivityIdAndCourseId(activityId, courseId);
        if (activity.isPresent()) {
            return activity.get();
        } else {
            throw new IllegalArgumentException("Activity not found for ID: " + activityId + " and Course ID: " + courseId);
        }
    }
}

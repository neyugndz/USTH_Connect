package com.usth_connect.vpn_server_backend_usth.service;

import com.google.api.client.util.Value;
import com.usth_connect.vpn_server_backend_usth.component.MoodleProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
@ConfigurationProperties(prefix = "moodle")
public class MoodleService {
    private static final Logger LOGGER = Logger.getLogger(MoodleService.class.getName());
    private final MoodleProperties moodleProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public MoodleService(MoodleProperties moodleProperties) {
        this.moodleProperties = moodleProperties;
    }

    public Map<String, Object> fetchSiteInfo() {
        String url = UriComponentsBuilder.fromHttpUrl(moodleProperties.getBaseUrl() + "/webservice/rest/server.php")
                .queryParam("wstoken",moodleProperties.getToken())
                .queryParam("moodlewsrestformat", "json")
                .queryParam("wsfunction", "core_webservice_get_site_info")
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

    // Moodle Service to fetch course info based on course ID
    public Map<String, Object> fetchCourses(Long courseId) {
        String url = UriComponentsBuilder.fromHttpUrl(moodleProperties.getBaseUrl() + "/webservice/rest/server.php")
                .queryParam("wstoken", moodleProperties.getToken())
                .queryParam("moodlewsrestformat", "json")
                .queryParam("wsfunction", "core_course_get_courses_by_field")
                .queryParam("field", "id")
                .queryParam("value", courseId)
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

    // Moodle Service to fetch list of courses info
    public List<Map<String, Object>> fetchAllCourses() {
        String url = UriComponentsBuilder.fromHttpUrl(moodleProperties.getBaseUrl() + "/webservice/rest/server.php")
                .queryParam("wstoken", moodleProperties.getToken())
                .queryParam("moodlewsrestformat", "json")
                .queryParam("wsfunction", "core_course_get_courses")
                .toUriString();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        ).getBody();
    }

    // Moodle Service to fetch the activity of the course
    public List<Map<String, Object>> fetchActivities(Long courseId){
        String url = UriComponentsBuilder.fromHttpUrl(moodleProperties.getBaseUrl() + "/webservice/rest/server.php")
                .queryParam("wstoken", moodleProperties.getToken())
                .queryParam("moodlewsrestformat", "json")
                .queryParam("wsfunction", "core_course_get_contents")
                .queryParam("courseid", courseId)
                .toUriString();

        // Fetch the activities
        List<Map<String, Object>> activities = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        ).getBody();

        // Log the raw response body
        LOGGER.info("Raw response from Moodle for activities: " + activities);

        return activities;
    }
}

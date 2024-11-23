package com.usth_connect.vpn_server_backend_usth.service;

import com.google.api.client.util.Value;
import com.usth_connect.vpn_server_backend_usth.component.MoodleProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@ConfigurationProperties(prefix = "moodle")
public class MoodleService {
//    @Value("${moodle.base-url}")
//    private String baseUrl;
//
//    @Value("${moodle.token}")
//    private String token;
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

    public Map<String, Object> fetchCourses() {
        String url = UriComponentsBuilder.fromHttpUrl(moodleProperties.getBaseUrl() + "/webservice/rest/server.php")
                .queryParam("wstoken", moodleProperties.getToken())
                .queryParam("moodlewsrestformat", "json")
                .queryParam("wsfunction", "core_course_get_courses")
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

}

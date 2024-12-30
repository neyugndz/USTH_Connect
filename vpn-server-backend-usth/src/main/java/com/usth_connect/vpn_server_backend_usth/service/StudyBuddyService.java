package com.usth_connect.vpn_server_backend_usth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usth_connect.vpn_server_backend_usth.entity.Student;
import com.usth_connect.vpn_server_backend_usth.entity.studyBuddy.StudyBuddy;
import com.usth_connect.vpn_server_backend_usth.entity.studyBuddy.StudyBuddyRecommendation;
import com.usth_connect.vpn_server_backend_usth.repository.StudyBuddyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.print.attribute.standard.JobKOctets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class StudyBuddyService {
    private final Logger LOGGER = Logger.getLogger(StudyBuddyService.class.getName());
    private final RestTemplate template = new RestTemplate();
    private final String flaskUrl = "http://127.0.0.1:5000";

    @Autowired
    private StudyBuddyRepository studyBuddyRepository;

    @Transactional
    public StudyBuddy save(StudyBuddy studyBuddy) {
        return studyBuddyRepository.save(studyBuddy);
    }

    public List<StudyBuddy> saveAll(List<StudyBuddy> studyBuddies) {
        return studyBuddyRepository.saveAll(studyBuddies);
    }


    public StudyBuddy findById(String studentId) {
        return studyBuddyRepository.findById(studentId).orElseThrow(() -> new RuntimeException("StudyBuddy not found!"));
    }

    public List<String> getRecommendations(String studentId) {

        // Fetch StudyBuddy from database
        StudyBuddy studyBuddy = findById(studentId);

        // Fetch related Student details
        Student student = studyBuddy.getStudent();

        // Prepare data for Flask as URL parameters
        String name = studyBuddy.getName();
        String gender = studyBuddy.getGender();
        String major = student.getMajor();
        String interest = String.join(",", studyBuddy.getInterests());
        String communication = studyBuddy.getCommunicationStyle();
        String lookingFor = studyBuddy.getLookingFor();
        String favSubject = String.join(",", studyBuddy.getFavoriteSubjects());
        String location = String.join(",", studyBuddy.getPreferredPlaces());
        String time = String.join(",", studyBuddy.getPreferredTimes());
        String personality = studyBuddy.getPersonality();

        LOGGER.info("Prepared data for Flask: " + name + ", " + gender + ", " + major);

        // Construct URL with parameters
        // Step 1: Load the sample data first
        String sampleUrl = UriComponentsBuilder
                .fromHttpUrl(flaskUrl)
                .path("/train/sample/{name}/{gender}/{major}/{interest}/{communication}/{looking_for}/{fav_subject}/{location}/{time}/{personality}")
                .buildAndExpand(name, gender, major, interest, communication, lookingFor, favSubject, location, time, personality)
                .toUriString();

        // Send GET request to load sample data
        ResponseEntity<String> loadSampleResponse = template.getForEntity(sampleUrl, String.class);
        LOGGER.info("Sample data load response: " + loadSampleResponse.getBody());

        // Step 2: Proceed to get recommendations if sample data is loaded
        String recommendUrl = UriComponentsBuilder
                .fromHttpUrl(flaskUrl)
                .path("/train/sample/{name}/{gender}/{major}/{interest}/{communication}/{looking_for}/{fav_subject}/{location}/{time}/{personality}/recommend")
                .buildAndExpand(name, gender, major, interest, communication, lookingFor, favSubject, location, time, personality)
                .toUriString();

        // Send GET request for recommendations
        ResponseEntity<String> response = template.getForEntity(recommendUrl, String.class);

        LOGGER.info("Response received from Flask: " + response.getBody());

        // Parse the JSON response into a List of names
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> recommendations;
        try {
            // Deserialize the response into a List of recommendations (which are objects with various fields)
            recommendations = objectMapper.readValue(response.getBody(), new TypeReference<List<Map<String, Object>>>() {});

            LOGGER.info("Recommendations: " + recommendations);

            // Extract the names from the recommendations
            List<String> recommendationNames = new ArrayList<>();
            for (Map<String, Object> recommendation : recommendations) {
                recommendationNames.add((String) recommendation.get("FullName"));
            }

            return recommendationNames;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing recommendations response from Flask: " + e.getMessage(), e);
        }
    }

    // Method to get detailed information of recommended study buddies
    public List<StudyBuddy> getRecommendationWithDetails(String studentId) {

        // Get the names of recommended study buddies
        List<String> recommendationNames = getRecommendations(studentId);

        // Fetch StudyBuddy entities from the db based on names
        List<StudyBuddy> recommendedStudyBuddies = new ArrayList<>();
        for (String name : recommendationNames) {
            StudyBuddy studyBuddy = studyBuddyRepository.findByName(name);
            if (studyBuddy != null) {
                recommendedStudyBuddies.add(studyBuddy);
            }
        }
        return recommendedStudyBuddies;
    }
}

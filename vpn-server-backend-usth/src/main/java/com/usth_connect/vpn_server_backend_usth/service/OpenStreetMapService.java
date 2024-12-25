package com.usth_connect.vpn_server_backend_usth.service;

import com.usth_connect.vpn_server_backend_usth.entity.MapLocation;
import com.usth_connect.vpn_server_backend_usth.repository.MapLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Service
public class OpenStreetMapService {
    private static final Logger LOGGER = Logger.getLogger(OpenStreetMapService.class.getName());
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    @Autowired
    private MapLocationRepository mapLocationRepository;

    // Call OpenStreetMap's Nominatim API to get latitude and longitude from an address
    public String getCoordinatesFromAddress(String address) {
        try {
            // Encode address to handle spaces and special characters
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            LOGGER.info("Encoded address: " + encodedAddress);

            String url = UriComponentsBuilder.fromHttpUrl(NOMINATIM_URL)
                    .queryParam("q", encodedAddress) // Pass the encoded address
                    .queryParam("format", "json")
                    .toUriString();

            LOGGER.info("Request URL: " + url);

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            LOGGER.info("API Response: " + response);

            // Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(response);

            if (jsonResponse.isArray() && jsonResponse.size() > 0) {
                JsonNode firstResult = jsonResponse.get(0);
                String lat = firstResult.get("lat").asText();
                String lon = firstResult.get("lon").asText();
                return "Latitude: " + lat + ", Longitude: " + lon;
            } else {
                return "No results found for the given address.";
            }
        } catch (Exception e) {
            return "An error occurred while fetching coordinates: " + e.getMessage();
        }
    }

    // Method to save address to the database
    public void saveLocation(String location, Double latitude, Double longtitude) {
        MapLocation mapLocation = new MapLocation();
        mapLocation.setLocation(location);
        mapLocation.setLatitude(latitude);
        mapLocation.setLongitude(longtitude);

        // Save to db
        mapLocationRepository.save(mapLocation);
    }
}

package com.usth_connect.vpn_server_backend_usth.service;

import com.usth_connect.vpn_server_backend_usth.entity.MapLocation;
import com.usth_connect.vpn_server_backend_usth.repository.MapLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Service
public class MapboxGeocodingService {
    private static final Logger LOGGER = Logger.getLogger(MapboxGeocodingService.class.getName());
//    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    @Value("${mapbox.access-token}")
    private String accessToken;

    private static final String MAPBOX_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places";


    @Autowired
    private MapLocationRepository mapLocationRepository;

    // Call OpenStreetMap's Nominatim API to get latitude and longitude from an address
    public String getCoordinatesFromAddress(String address) {
        try {
            // Encode address to handle spaces and special characters
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            LOGGER.info("Encoded address: " + encodedAddress);

//            String url = UriComponentsBuilder.fromHttpUrl(NOMINATIM_URL)
//                    .queryParam("q", encodedAddress) // Pass the encoded address
//                    .queryParam("format", "json")
//                    .toUriString();
            URI uri = UriComponentsBuilder.fromHttpUrl(MAPBOX_URL + "/" + encodedAddress + ".json")
                    .queryParam("access_token", accessToken) // Use the injected token
                    .queryParam("limit", 1) // Limit results to the most relevant
                    .build()
                    .toUri();

            LOGGER.info("Request URL: " + uri);


            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(uri, String.class);

            LOGGER.info("API Response: " + response);

            // Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(response);

            if (jsonResponse.has("features") && jsonResponse.get("features").size() > 0) {
                JsonNode firstFeature = jsonResponse.get("features").get(0);
                String lat = firstFeature.get("geometry").get("coordinates").get(1).asText();
                String lon = firstFeature.get("geometry").get("coordinates").get(0).asText();
                return "Latitude: " + lat + ", Longitude: " + lon;
            } else {
                return "No results found for the given address.";
            }
        } catch (Exception e) {
            return "An error occurred while fetching coordinates: " + e.getMessage();
        }
//            if (jsonResponse.isArray() && jsonResponse.size() > 0) {
//                JsonNode firstResult = jsonResponse.get(0);
//                String lat = firstResult.get("lat").asText();
//                String lon = firstResult.get("lon").asText();
//                return "Latitude: " + lat + ", Longitude: " + lon;
//            } else {
//                return "No results found for the given address.";
//            }
//        } catch (Exception e) {
//            return "An error occurred while fetching coordinates: " + e.getMessage();
//        }
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

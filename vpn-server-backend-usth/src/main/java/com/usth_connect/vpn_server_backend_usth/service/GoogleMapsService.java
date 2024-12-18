package com.usth_connect.vpn_server_backend_usth.service;

import com.google.api.client.util.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GoogleMapsService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    // Call Google Geocoding API to get latitude and longitude from an address
    public String getCoordinatesFromAddress(String address) {
        String url = UriComponentsBuilder.fromHttpUrl(GEOCODE_URL)
                .queryParam("address", address)
                .queryParam("key", apiKey)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }
}

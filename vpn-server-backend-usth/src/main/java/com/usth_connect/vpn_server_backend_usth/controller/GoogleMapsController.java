package com.usth_connect.vpn_server_backend_usth.controller;

import com.usth_connect.vpn_server_backend_usth.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/maps")
public class GoogleMapsController {

    @Autowired
    private GoogleMapsService googleMapsService;

    // Endpoint to fetch coordinates for an address
    @GetMapping("/geocode")
    public String getCoordinates(@RequestParam String address) {
        return googleMapsService.getCoordinatesFromAddress(address);
    }
}

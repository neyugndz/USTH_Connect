package com.usth_connect.vpn_server_backend_usth.repository;

import com.usth_connect.vpn_server_backend_usth.entity.MapLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MapLocationRepository extends JpaRepository<MapLocation, String> {
    Optional<MapLocation> findByLocation(String location);
}

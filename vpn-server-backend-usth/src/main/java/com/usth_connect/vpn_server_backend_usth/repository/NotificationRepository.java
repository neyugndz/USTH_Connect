package com.usth_connect.vpn_server_backend_usth.repository;

import com.usth_connect.vpn_server_backend_usth.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    Optional<Notification> findByMessage(String finalMessage);
}

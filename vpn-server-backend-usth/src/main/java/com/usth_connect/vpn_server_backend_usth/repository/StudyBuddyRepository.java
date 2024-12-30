package com.usth_connect.vpn_server_backend_usth.repository;

import com.usth_connect.vpn_server_backend_usth.entity.studyBuddy.StudyBuddy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyBuddyRepository extends JpaRepository<StudyBuddy, String> {
    StudyBuddy findByName(String name);
}

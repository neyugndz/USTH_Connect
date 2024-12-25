package com.usth_connect.vpn_server_backend_usth.controller;


import com.usth_connect.vpn_server_backend_usth.entity.studyBuddy.StudyBuddy;
import com.usth_connect.vpn_server_backend_usth.service.MoodleService;
import com.usth_connect.vpn_server_backend_usth.service.StudyBuddyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/studyBuddy")
public class StudyBuddyController {
    private static final Logger LOGGER = Logger.getLogger(StudyBuddyController.class.getName());

    @Autowired
    private StudyBuddyService studyBuddyService;

    // Endpoint to save StudyBuddy from the FE
    @PostMapping("/save")
    public ResponseEntity<StudyBuddy> saveStudyBuddy(@RequestBody StudyBuddy studyBuddy) {
        LOGGER.info("Received Body: " + studyBuddy);
        StudyBuddy studyBuddy1 = studyBuddyService.save(studyBuddy);
        return ResponseEntity.ok(studyBuddy1);
    }
}

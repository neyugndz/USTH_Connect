package com.usth_connect.vpn_server_backend_usth.service;

import com.usth_connect.vpn_server_backend_usth.entity.studyBuddy.StudyBuddy;
import com.usth_connect.vpn_server_backend_usth.repository.StudyBuddyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudyBuddyService {

    @Autowired
    private StudyBuddyRepository studyBuddyRepository;

    @Transactional
    public StudyBuddy save(StudyBuddy studyBuddy) {
        return studyBuddyRepository.save(studyBuddy);
    }
}

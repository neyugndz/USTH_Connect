package com.usth_connect.vpn_server_backend_usth.repository;

import com.usth_connect.vpn_server_backend_usth.entity.moodle.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {

}

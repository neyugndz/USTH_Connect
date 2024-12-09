package com.usth_connect.vpn_server_backend_usth.entity.moodle;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Start_time")
    private LocalTime startTime;

    @Column(name = "End_time")
    private LocalTime endTime;

    @Column(name = "Location_ID")
    private Integer locationId;

    @Column(name = "Room")
    private String room;

    @Column(name = "Reference_slide")
    private Integer referenceSlide;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getReferenceSlide() {
        return referenceSlide;
    }

    public void setReferenceSlide(Integer referenceSlide) {
        this.referenceSlide = referenceSlide;
    }
}

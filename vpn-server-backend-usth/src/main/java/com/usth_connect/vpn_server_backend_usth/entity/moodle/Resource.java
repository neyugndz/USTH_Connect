package com.usth_connect.vpn_server_backend_usth.entity.moodle;

import jakarta.persistence.*;

@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Module_Code")
    private Module module;

    @Column(name = "Resource_Type")
    private String type;

    @Column(name = "Resource_Name")
    private String name;

    @Column(name = "Lecture_number")
    private Integer lectureNumber;

    @Column(name = "Reference_slide")
    private Integer[] referenceSlide;

    @Column(name = "Homework_link")
    private String[] homeworkLink;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.usth_connect.vpn_server_backend_usth.entity.moodle.Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Integer getLectureNumber() {
        return lectureNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLectureNumber(Integer lectureNumber) {
        this.lectureNumber = lectureNumber;
    }

    public Integer[] getReferenceSlide() {
        return referenceSlide;
    }

    public void setReferenceSlide(Integer[] referenceSlide) {
        this.referenceSlide = referenceSlide;
    }

    public String[] getHomeworkLink() {
        return homeworkLink;
    }

    public void setHomeworkLink(String[] homeworkLink) {
        this.homeworkLink = homeworkLink;
    }
}

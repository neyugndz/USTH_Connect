package com.usth_connect.vpn_server_backend_usth.entity.studyBuddy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.usth_connect.vpn_server_backend_usth.Enum.*;
import com.usth_connect.vpn_server_backend_usth.entity.Student;
import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "study_buddy")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudyBuddy {
    @Id
    @Column(name = "studentId")
    private String studentId;

    @Column(name = "name")
    private String name;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)  // Lazy load the Student entity
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private Student student;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "Major")
    private String major;

    @Column(name = "Personality")
    private String personality;

    @Column(name = "Communication_Style")
    private String communicationStyle;

    @Column(name = "Looking_For")
    private String lookingFor;

    @ElementCollection
    @CollectionTable(name = "study_buddy_interests", joinColumns = @JoinColumn(name = "study_buddy_id"))
    @Column(name = "interest")
    private List<String> interests;

    @ElementCollection
    @CollectionTable(name = "study_buddy_favorite_subjects", joinColumns = @JoinColumn(name = "study_buddy_id"))
    @Column(name = "subject")
    private List<String> favoriteSubjects;

    @ElementCollection
    @CollectionTable(name = "study_buddy_preferred_places", joinColumns = @JoinColumn(name = "study_buddy_id"))
    @Column(name = "place")
    private List<String> preferredPlaces;

    @ElementCollection
    @CollectionTable(name = "study_buddy_preferred_times", joinColumns = @JoinColumn(name = "study_buddy_id"))
    @Column(name = "time")
    private List<String> preferredTimes;

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPreferredTimes() {
        return preferredTimes;
    }

    public void setPreferredTimes(List<StudyTime> preferredTimes) {
        this.preferredTimes = preferredTimes.stream()
                .map(StudyTime::getDisplayValue)  // Convert to display value
                .collect(Collectors.toList());
    }

    public List<String> getPreferredPlaces() {
        return preferredPlaces;
    }

    public void setPreferredPlaces(List<StudyPlace> preferredPlaces) {
        this.preferredPlaces = preferredPlaces.stream()
                .map(StudyPlace::getDisplayValue)  // Convert to display value
                .collect(Collectors.toList());
    }

    public List<String> getFavoriteSubjects() {
        return favoriteSubjects;
    }

    public void setFavoriteSubjects(List<Subject> favoriteSubjects) {
        this.favoriteSubjects = favoriteSubjects.stream()
                .map(Subject::getDisplayValue)  // Convert to display value
                .collect(Collectors.toList());
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests.stream()
                .map(Interest::getDisplayValue)  // Convert to display value
                .collect(Collectors.toList());
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(Opponent lookingFor) {
        this.lookingFor = lookingFor.getDisplayValue();
    }

    public String getCommunicationStyle() {
        return communicationStyle;
    }

    public void setCommunicationStyle(CommunicationStyle communicationStyle) {
        this.communicationStyle = communicationStyle.getDisplayValue();
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender.getDisplayValue();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @PreUpdate
    private void syncMajorWithStudent() {
        if (student != null) {
            this.major = student.getMajor();
        }
    }
    @Override
    public String toString() {
        return "StudyBuddy{" +
                "studentId='" + studentId + '\'' +
                ", gender=" + gender +
                ", personality=" + personality +
                ", communicationStyle=" + communicationStyle +
                ", lookingFor=" + lookingFor +
                ", interests=" + interests +
                ", favoriteSubjects=" + favoriteSubjects +
                ", preferredPlaces=" + preferredPlaces +
                ", preferredTimes=" + preferredTimes +
                '}';
    }
}


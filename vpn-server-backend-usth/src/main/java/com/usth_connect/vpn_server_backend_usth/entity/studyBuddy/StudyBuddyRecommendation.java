package com.usth_connect.vpn_server_backend_usth.entity.studyBuddy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class StudyBuddyRecommendation {
    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("Major")
    private String major;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("Interests")
    private String interestsString; // If interests are a comma-separated string

    // If you want to get Interests as a list, you can add a getter method
    @JsonIgnore
    public List<String> getInterests() {
        if (interestsString != null && !interestsString.isEmpty()) {
            return List.of(interestsString.split(","));
        }
        return null; // or an empty list, based on your preference
    }

    @JsonProperty("Communication_Style")
    private String communicationStyle;

    @JsonProperty("Looking_For")
    private String lookingFor;

    @JsonProperty("Favorite_Subjects")
    private List<String> favoriteSubjects;

    @JsonProperty("Study_Location")
    private List<String> studyLocation;

    @JsonProperty("Study_Time")
    private List<String> studyTime;

    @JsonProperty("Personality")
    private String personality;

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInterestsString() {
        return interestsString;
    }

    public void setInterestsString(String interestsString) {
        this.interestsString = interestsString;
    }

    public String getCommunicationStyle() {
        return communicationStyle;
    }

    public void setCommunicationStyle(String communicationStyle) {
        this.communicationStyle = communicationStyle;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public List<String> getFavoriteSubjects() {
        return favoriteSubjects;
    }

    public void setFavoriteSubjects(List<String> favoriteSubjects) {
        this.favoriteSubjects = favoriteSubjects;
    }

    public List<String> getStudyLocation() {
        return studyLocation;
    }

    public void setStudyLocation(List<String> studyLocation) {
        this.studyLocation = studyLocation;
    }

    public List<String> getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(List<String> studyTime) {
        this.studyTime = studyTime;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }
}

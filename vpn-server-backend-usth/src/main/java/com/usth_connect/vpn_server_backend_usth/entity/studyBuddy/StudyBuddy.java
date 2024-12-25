package com.usth_connect.vpn_server_backend_usth.entity.studyBuddy;

import com.usth_connect.vpn_server_backend_usth.Enum.*;
import com.usth_connect.vpn_server_backend_usth.entity.Student;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "study_buddy")
public class StudyBuddy {
    @Id
    @Column(name = "Student_ID")
    private String studentId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "Student_ID")
    private Student student;

    @Column(name = "Gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "Personality")
    @Enumerated(EnumType.STRING)
    private Personality personality;

    @Column(name = "Communication_Style")
    @Enumerated(EnumType.STRING)
    private CommunicationStyle communicationStyle;

    @Column(name = "Looking_For")
    @Enumerated(EnumType.STRING)
    private Opponent lookingFor;

    @ElementCollection
    @CollectionTable(name = "study_buddy_interests", joinColumns = @JoinColumn(name = "study_buddy_id"))
    @Column(name = "interest")
    private List<Interest> interests;

    @ElementCollection
    @CollectionTable(name = "study_buddy_favorite_subjects", joinColumns = @JoinColumn(name = "study_buddy_id"))
    @Column(name = "subject")
    private List<Subject> favoriteSubjects;

    @ElementCollection
    @CollectionTable(name = "study_buddy_preferred_places", joinColumns = @JoinColumn(name = "study_buddy_id"))
    @Column(name = "place")
    private List<StudyPlace> preferredPlaces;

    @ElementCollection
    @CollectionTable(name = "study_buddy_preferred_times", joinColumns = @JoinColumn(name = "study_buddy_id"))
    @Column(name = "time")
    private List<StudyTime> preferredTimes;

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Personality getPersonality() {
        return personality;
    }

    public void setPersonality(Personality personality) {
        this.personality = personality;
    }

    public CommunicationStyle getCommunicationStyle() {
        return communicationStyle;
    }

    public void setCommunicationStyle(CommunicationStyle communicationStyle) {
        this.communicationStyle = communicationStyle;
    }

    public Opponent getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(Opponent lookingFor) {
        this.lookingFor = lookingFor;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public List<Subject> getFavoriteSubjects() {
        return favoriteSubjects;
    }

    public void setFavoriteSubjects(List<Subject> favoriteSubjects) {
        this.favoriteSubjects = favoriteSubjects;
    }

    public List<StudyPlace> getPreferredPlaces() {
        return preferredPlaces;
    }

    public void setPreferredPlaces(List<StudyPlace> preferredPlaces) {
        this.preferredPlaces = preferredPlaces;
    }

    public List<StudyTime> getPreferredTimes() {
        return preferredTimes;
    }

    public void setPreferredTimes(List<StudyTime> preferredTimes) {
        this.preferredTimes = preferredTimes;
    }

    @Override
    public String toString() {
        return "StudyBuddy{" +
                "studentId='" + studentId + '\'' +
                ", student=" + student +
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


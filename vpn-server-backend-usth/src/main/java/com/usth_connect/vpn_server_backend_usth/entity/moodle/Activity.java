package com.usth_connect.vpn_server_backend_usth.entity.moodle;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activity")
public class Activity {
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long activityId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "completion_status")
    private String completionStatus;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Resource> resources = new ArrayList<>();

    // Getters and setters
    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
}

package com.eduhubpro.eduhubpro.Entity.Review.Model;

import com.eduhubpro.eduhubpro.Entity.Course.Model.Course;
import com.eduhubpro.eduhubpro.Entity.User.Model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID reviewId;

    @Column(name = "score", columnDefinition = "INT", nullable = false)
    private int score = 1;

    @Column(name = "description", columnDefinition = "VARCHAR(255)", nullable = false)
    private String description;

    @Column(name = "score_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime scoreDate;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    public Review() {

    }

    // Para estudiante - curso
    public Review(String description, User student, Course course) {
        this.description = description;
        this.scoreDate = LocalDateTime.now();
        this.student = student;
        this.course = course;
    }

    // Para estudiante - docente
    public Review(String description, User student, User instructor) {
        this.description = description;
        this.scoreDate = LocalDateTime.now();
        this.student = student;
        this.instructor = instructor;
    }

    public UUID getReviewId() {
        return reviewId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setReviewId(UUID reviewId) {
        this.reviewId = reviewId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(LocalDateTime scoreDate) {
        this.scoreDate = scoreDate;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }
}

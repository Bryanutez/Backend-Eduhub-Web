package com.eduhubpro.eduhubpro.Entity.Registration.Model;

import com.eduhubpro.eduhubpro.Entity.Course.Model.Course;
import com.eduhubpro.eduhubpro.Entity.User.Model.User;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.RegistrationStatus;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "registration")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID registrationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RegistrationStatus registrationStatus = RegistrationStatus.PENDING_PAYMENT;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public Registration() {

    }

    // Aplica cuando se registra un usuario a un curso
    public Registration(User student, Course course) {
        this.student = student;
        this.course = course;
    }

    public UUID getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(UUID registrationId) {
        this.registrationId = registrationId;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
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
}
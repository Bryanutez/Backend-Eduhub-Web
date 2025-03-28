package com.eduhubpro.eduhubpro.Entity.Constancy.Model;

import com.eduhubpro.eduhubpro.Entity.Course.Model.Course;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;
// todo enviar la constancia solo cuando haya finalizado el curso y la fecha de fin del curso haya llegado
@Entity
@Table(name = "constancy")
public class Constancy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID constancyId;

    @Column(name = "expedition_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime expeditionDate;

    @Column(name = "content_url", columnDefinition = "VARCHAR(255)", nullable = false)
    private String constancyUrl;

    @OneToOne
    @JoinColumn(name = "course_id", nullable = false, unique = true)
    private Course course;

    public Constancy() {

    }

    public Constancy(String constancyUrl, Course course) {
        this.expeditionDate = LocalDateTime.now();
        this.constancyUrl = constancyUrl;
        this.course = course;
    }

    public UUID getConstancyId() {
        return constancyId;
    }

    public void setConstancyId(UUID constancyId) {
        this.constancyId = constancyId;
    }

    public LocalDateTime getExpeditionDate() {
        return expeditionDate;
    }

    public void setExpeditionDate(LocalDateTime expeditionDate) {
        this.expeditionDate = expeditionDate;
    }

    public String getConstancyUrl() {
        return constancyUrl;
    }

    public void setConstancyUrl(String constancyUrl) {
        this.constancyUrl = constancyUrl;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
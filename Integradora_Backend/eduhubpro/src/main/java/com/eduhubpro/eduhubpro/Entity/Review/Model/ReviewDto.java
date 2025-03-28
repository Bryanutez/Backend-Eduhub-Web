package com.eduhubpro.eduhubpro.Entity.Review.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ReviewDto {

    @NotNull(groups = {Modify.class, ChangeStatus.class, Consult.class}, message = "El id no puede ser nulo ni vacío.")
    private String reviewId;

    @NotNull(groups = {Register.class, Modify.class}, message = "La valoración no puede ser nula.")
    @Positive(message = "La valoración debe ser un número positivo.")
    private int score;

    @NotBlank(groups = {Register.class, Modify.class}, message = "La descripción no puede ser nula.")
    private String description;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El id del estudiante no puede ser nulo.")
    private String studentId;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El id del curso no puede ser nulo.")
    private String courseId;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El id del docente no puede ser nulo.")
    private String instructor_id;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getInstructor_id() {
        return instructor_id;
    }

    public void setInstructor_id(String instructor_id) {
        this.instructor_id = instructor_id;
    }

    public interface Register {
    }

    public interface Modify {
    }

    public interface ChangeStatus {
    }

    public interface Consult {
    }
}

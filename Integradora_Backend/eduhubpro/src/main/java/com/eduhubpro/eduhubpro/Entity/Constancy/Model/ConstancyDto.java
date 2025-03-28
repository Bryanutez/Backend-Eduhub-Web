package com.eduhubpro.eduhubpro.Entity.Constancy.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ConstancyDto {

    @NotNull(groups = {Modify.class, ChangeStatus.class, Consult.class}, message = "El id no puede ser nulo.")
    private String constancyId;

    @NotNull(groups = {Register.class}, message = "La fecha de expedición no puede ser nula.")
    private LocalDateTime expeditionDate;

    @NotBlank(groups = {Register.class}, message = "La url de la constancia no puede ser nula.")
    @Size(max = 255, message = "La url de la constancia no puede exceder los 255 caracteres.")
    private String constancyUrl;

    @NotBlank(groups = {Register.class}, message = "El curso asociado no puede ser nulo.")
    private String courseId;

    public String getConstancyId() {
        return constancyId;
    }

    public void setConstancyId(String constancyId) {
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

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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
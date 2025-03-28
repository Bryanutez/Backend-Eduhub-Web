package com.eduhubpro.eduhubpro.Entity.Registration.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.RegistrationStatus;
import jakarta.validation.constraints.NotNull;

public class RegistrationDto {

    @NotNull(groups = {Modify.class, ChangeStatus.class, Consult.class}, message = "El id no puede ser nulo.")
    private String registrationId;

    @NotNull(groups = {Register.class}, message = "El estudiante no puede ser nulo.")
    private String studentId; // todo || Se sacará de la sesión el usuario

    @NotNull(groups = {Register.class}, message = "El curso no puede ser nulo.")
    private String courseId;

    @NotNull(groups = {ChangeStatus.class}, message = "El estado no puede ser nulo.")
    private RegistrationStatus registrationStatus;

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
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

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
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
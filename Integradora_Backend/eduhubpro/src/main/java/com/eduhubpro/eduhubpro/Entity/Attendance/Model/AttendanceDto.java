package com.eduhubpro.eduhubpro.Entity.Attendance.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class
AttendanceDto {

    @NotNull(groups = {Modify.class, ChangeStatus.class, Consult.class}, message = "El id no puede ser nulo ni vacío.")
    private String attendanceId;

    @NotBlank(groups = {Register.class}, message = "El módulo no puede ser nulo.")
    private String moduleId;

    @NotBlank(groups = {Register.class}, message = "El del curso no puede ser nulo.")
    private String courseId;

    @NotNull(groups = {ChangeStatus.class}, message = "La asistencia no puede ser nula.")
    private boolean attended;

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
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

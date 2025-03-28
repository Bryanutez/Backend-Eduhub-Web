package com.eduhubpro.eduhubpro.Entity.Attendance.Model;

import com.eduhubpro.eduhubpro.Entity.Course.Model.Course;
import com.eduhubpro.eduhubpro.Entity.Module.Model.Module;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID attendanceId;

    @Column(name = "attended", nullable = false)
    private boolean attended = true; // Indicador de asistencia

    @OneToOne
    @JoinColumn(name = "module_id", nullable = false, unique = true)
    private Module module; // Un módulo tiene una asistencia

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // Relación con la tabla cursos

    public Attendance() {

    }

    public Attendance(Module module, Course course) {
        this.module = module;
        this.course = course;
    }

    public UUID getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(UUID attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }
}
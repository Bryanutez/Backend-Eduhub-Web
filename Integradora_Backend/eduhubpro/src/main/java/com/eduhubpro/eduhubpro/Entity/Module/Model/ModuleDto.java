package com.eduhubpro.eduhubpro.Entity.Module.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.ModuleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ModuleDto {

    @NotNull(groups = {Modify.class, ChangeStatus.class, Consult.class}, message = "El id no puede ser nulo.")
    private String moduleId;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El nombre no puede ser nulo.")
    @Size(groups = {Register.class, Modify.class}, max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String name;

    @NotNull(groups = {Register.class, Modify.class}, message = "La fecha no puede ser nula.")
    private LocalDateTime date;

    @NotNull(groups = {ChangeStatus.class}, message = "El estado no puede ser nulo.")
    private ModuleStatus status;

    @NotNull(groups = {Register.class, ConsultModules.class}, message = "El curso no puede ser nulo.")
    private String courseId;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ModuleStatus getStatus() {
        return status;
    }

    public void setStatus(ModuleStatus status) {
        this.status = status;
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

    public interface ConsultModules {
    }
}

package com.eduhubpro.eduhubpro.Entity.Section.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SectionDto {

    @NotNull(groups = {Modify.class, ChangeStatus.class, Consult.class}, message = "El id no puede ser nulo.")
    private String sectionId;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El nombre no puede ser nulo.")
    @Size(groups = {Register.class, Modify.class}, max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String name;

    @NotBlank(groups = {Register.class, Modify.class}, message = "La descripción no puede ser nula.")
    @Size(groups = {Register.class, Modify.class}, max = 255, message = "La descripción no puede exceder los 255 caracteres.")
    private String description;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El contenido no puede ser nulo.")
    @Size(groups = {Register.class, Modify.class}, max = 255, message = "El contenido no puede exceder los 255 caracteres.")
    private String contentUrl;

    @NotNull(groups = {ChangeStatus.class}, message = "El estado no puede ser nulo.")
    private Status status;

    @NotNull(groups = {Register.class, ConsultSections.class}, message = "El módulo no puede ser nulo.")
    private String moduleId;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public interface Register {
    }

    public interface Modify {
    }

    public interface ChangeStatus {
    }

    public interface Consult {
    }

    public interface ConsultSections {
    }
}
package com.eduhubpro.eduhubpro.Entity.Category.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CategoryDto {

    @NotBlank(groups = {Modify.class, ChangeStatus.class, Consult.class}, message = "El id no puede ser nulo.")
    private String categoryId;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El nombre de la categoría no puede ser nulo.")
    @Size(groups = {Register.class, Modify.class}, max = 100, message = "El nombre de la categoría no puede exceder los 100 caracteres.")
    private String name;

    @NotNull(groups = {ChangeStatus.class}, message = "El estado no puede ser nulo.")
    private Status status;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

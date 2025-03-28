package com.eduhubpro.eduhubpro.Entity.User.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.Status;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.UserRole;
import jakarta.validation.constraints.*;

public class UserDto {

    @NotBlank(groups = {Modify.class, ModifyUser.class, ChangeStatus.class, Consult.class}, message = "El id no puede ser nulo.")
    private String userId;

    @NotBlank(groups = {Register.class, Modify.class, ModifyUser.class}, message = "El nombre no puede ser nulo ni vacío.")
    @Size(groups = {Register.class, Modify.class, ModifyUser.class}, max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String name;

    @NotBlank(groups = {Register.class, Modify.class, ModifyUser.class}, message = "El correo no puede estar vacío.")
    @Size(groups = {Register.class, Modify.class, ModifyUser.class}, max = 100, message = "El correo no puede exceder 100 caracteres.")
    @Email(groups = {Register.class, Modify.class, ModifyUser.class}, message = "El correo debe tener un formato válido.")
    private String email;

    @NotBlank(groups = {Register.class}, message = "La contraseña no puede estar vacía.")
    @Size(groups = {Register.class}, min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres.")
    @Pattern(groups = {Register.class}, regexp = "^(?=.*[A-Z])(?=.*\\d).{8,20}$", message = "La contraseña debe contener al menos un número y una letra mayúscula.")
    private String password;

    @NotBlank(groups = {Modify.class}, message = "La foto del perfil no puede estar vacía.")
    @Size(groups = {Modify.class}, max = 255, message = "La foto de perfil no puede exceder 255 caracteres.")
    private String profilePhotoPath;

    @NotBlank(groups = {Modify.class}, message = "La descripción del perfil no puede estar vacía.")
    @Size(groups = {Modify.class}, max = 255, message = "La descripción no puede exceder 255 caracteres.")
    private String description;

    @NotBlank(groups = {Register.class}, message = "El rol no puede ser nulo.")
    private String role;

    @NotNull(groups = {ChangeStatus.class}, message = "El estado no puede ser nulo.")
    private Status status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public interface Register {
    }

    public interface Modify {
    }

    public interface ModifyUser {
    }

    public interface ChangeStatus {
    }

    public interface Consult {
    }
}
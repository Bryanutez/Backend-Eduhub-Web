package com.eduhubpro.eduhubpro.Entity.Course.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.CourseStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public class CourseDto {

    @NotBlank(groups = {Modify.class, ChangeStatus.class, Consult.class}, message = "El id no puede ser nulo.")
    private String courseId;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El título no puede ser nulo.")
    @Size(groups = {Register.class, Modify.class}, max = 100, message = "El título no puede exceder 100 caracteres.")
    private String title;

    @NotBlank(groups = {Register.class, Modify.class}, message = "La descripción no puede ser nula.")
    @Size(groups = {Register.class, Modify.class}, max = 255, message = "La descripción de perfil no puede exceder 255 caracteres.")
    private String description;

    @NotBlank(groups = {Register.class, Modify.class}, message = "La ruta del video no puede ser nula.")
    @Size(groups = {Register.class, Modify.class}, max = 255, message = "La portada no puede exceder 255 caracteres.")
    private String bannerPath;

    @FutureOrPresent(groups = {Register.class, Modify.class, ConsultDate.class}, message = "La fecha de inicio no puede ser anterior a la actual.")
    @NotNull(groups = {Register.class, Modify.class, ConsultDate.class}, message = "La fecha de inicio no puede ser nula.")
    private LocalDateTime startDate;

    @FutureOrPresent(groups = {Register.class, Modify.class}, message = "La fecha de fin no puede ser anterior a la actual.")
    @NotNull(groups = {Register.class, Modify.class}, message = "La fecha de fin no puede ser nula.")
    private LocalDateTime endDate;

    @NotNull(groups = {Register.class, Modify.class}, message = "El precio no puede ser nulo.")
    @Positive(groups = {Register.class, Modify.class}, message = "El precio debe ser un valor positivo.")
    private double price;

    private int size;

    @NotBlank(groups = {ChangeStatus.class}, message = "El estado no puede ser nulo.")
    private String courseStatus;

    @NotBlank(groups = {Register.class, ConsultFromInstructor.class}, message = "El instructor no puede ser nulo.")
    private String instructorId; // También se utiliza para las búsquedas por docente

    @NotNull(groups = {Register.class, Modify.class}, message = "Las categorías no pueden ser nulas.")
    private List<String> categoriesId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBannerPath() {
        return bannerPath;
    }

    public void setBannerPath(String bannerPath) {
        this.bannerPath = bannerPath;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public List<String> getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(List<String> categoriesId) {
        this.categoriesId = categoriesId;
    }

    public interface Register {
    }

    public interface Modify {
    }

    public interface ChangeStatus {
    }

    public interface Consult {
    }

    public interface ConsultFromInstructor {
    }

    public interface ConsultDate {
    }
}

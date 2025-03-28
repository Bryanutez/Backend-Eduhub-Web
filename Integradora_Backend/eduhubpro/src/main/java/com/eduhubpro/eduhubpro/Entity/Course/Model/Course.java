package com.eduhubpro.eduhubpro.Entity.Course.Model;

import com.eduhubpro.eduhubpro.Entity.Category.Model.Category;
import com.eduhubpro.eduhubpro.Entity.Module.Model.Module;
import com.eduhubpro.eduhubpro.Entity.User.Model.User;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.CourseStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID courseId;

    @Column(name = "title", columnDefinition = "VARCHAR(100)", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "VARCHAR(255)", nullable = false)
    private String description;

    @Column(name = "banner_path", columnDefinition = "VARCHAR(255)", nullable = false)
    private String bannerPath;

    @Column(name = "start_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "price", columnDefinition = "DOUBLE", nullable = false)
    private double price;

    @Column(name = "size", columnDefinition = "INT", nullable = false)
    private int size = 30; // Cupo del curso (máximo 30 personas)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CourseStatus courseStatus = CourseStatus.TO_APPROVE;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Module> modules;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "course_category",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    public Course() {

    }

    // Al crear un curso como docente
    public Course(String title, String description, String bannerPath, LocalDateTime startDate, LocalDateTime endDate, double price, User instructor, List<Category> categories) {
        this.title = title;
        this.description = description;
        this.bannerPath = bannerPath;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.instructor = instructor;
        this.categories = categories;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
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

    public CourseStatus getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(CourseStatus courseStatus) {
        this.courseStatus = courseStatus;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}
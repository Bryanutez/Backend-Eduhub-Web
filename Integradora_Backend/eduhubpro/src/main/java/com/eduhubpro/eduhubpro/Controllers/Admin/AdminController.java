package com.eduhubpro.eduhubpro.Controllers.Admin;

import com.eduhubpro.eduhubpro.Entity.Category.Model.CategoryDto;
import com.eduhubpro.eduhubpro.Entity.Category.Service.CategoryService;
import com.eduhubpro.eduhubpro.Entity.Course.Model.CourseRepository;
import com.eduhubpro.eduhubpro.Entity.Course.Service.CourseService;
import com.eduhubpro.eduhubpro.Entity.User.Model.UserDto;
import com.eduhubpro.eduhubpro.Entity.User.Service.UserService;
import com.eduhubpro.eduhubpro.Util.Response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final CourseRepository courseRepository;
    private final CourseService courseService;

    @Autowired
    public AdminController(UserService userService, CategoryService categoryService, CourseRepository courseRepository, CourseService courseService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.courseRepository = courseRepository;
        this.courseService = courseService;
    }

    @GetMapping("/user-management/count-all-users")
    public ResponseEntity<Message> countUsers() {
        return userService.countAllUsers();
    }

    @GetMapping("/user-management/get-all-users")
    public ResponseEntity<Message> getAllUsers() {
        return userService.findAllForAdmin();
    }

    @GetMapping("/user-management/get-user-by-id")
    public ResponseEntity<Message> getUserById(@Validated(UserDto.Consult.class) @RequestBody UserDto dto) {
        return userService.findById(dto);
    }

    @PutMapping("/user-management/update-user")
    public ResponseEntity<Message> updateUser(@Validated(UserDto.ModifyUser.class) @RequestBody UserDto dto) {
        return userService.updateForAdmin(dto);
    }

    @PutMapping("/user-management/change-status-instructor")
    public ResponseEntity<Message> changeStatusInstructor(@Validated(UserDto.ChangeStatus.class) @RequestBody UserDto dto) {
        return userService.changeStatusInstructorForAdmin(dto);
    }

    @PutMapping("/user-management/change-status-student")
    public ResponseEntity<Message> changeStatusUser(@Validated(UserDto.ChangeStatus.class) @RequestBody UserDto dto) {
        return userService.changeStatusStudentForAdmin(dto);
    }

    // Módulo categorías
    @GetMapping("/category-management/get-all-categories")
    public ResponseEntity<Message> getAllCategories() {
        return categoryService.findAllActives();
    }

    @GetMapping("/category-management/get-category-by-id")
    public ResponseEntity<Message> getCategoryById(@Validated(CategoryDto.Consult.class) @RequestBody CategoryDto dto) {
        return categoryService.findById(dto);
    }

    @PostMapping("/category-management/creat    e-category")
    public ResponseEntity<Message> save(@Validated(CategoryDto.Register.class) @RequestBody CategoryDto dto) {
        return categoryService.save(dto);
    }

    @PutMapping("/category-management/update-category")
    public ResponseEntity<Message> update(@Validated(CategoryDto.Modify.class) @RequestBody CategoryDto dto) {
        return categoryService.update(dto);
    }

    @PutMapping("/category-management/change-status-category")
    public ResponseEntity<Message> changeStatus(@Validated(CategoryDto.ChangeStatus.class) @RequestBody CategoryDto dto) {
        return categoryService.changeStatus(dto);
    }

    // Módulo de cursos
    // Muestra la lista de cursos por aprobar
    @GetMapping("/course-management/get-all-courses")
    public ResponseEntity<Message> getAllCourses() {
        return courseService.findAllToApprove();
    }

}
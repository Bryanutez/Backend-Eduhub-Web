package com.eduhubpro.eduhubpro.Controllers.Instructor;

import com.eduhubpro.eduhubpro.Entity.Course.Model.CourseDto;
import com.eduhubpro.eduhubpro.Entity.Course.Service.CourseService;
import com.eduhubpro.eduhubpro.Entity.User.Model.UserDto;
import com.eduhubpro.eduhubpro.Entity.User.Service.UserService;
import com.eduhubpro.eduhubpro.Util.Response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instructor")
public class InstructorController {

    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public InstructorController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @PutMapping("/user-management/change-status-instructor")
    public ResponseEntity<Message> changeStatusInstructor(@Validated(UserDto.ChangeStatus.class) @RequestBody UserDto dto) {
        return userService.changeStatusInstructor(dto);
    }

    // Muestra todos los cursos al docente
    @GetMapping("/course-management/get-all-courses")
    public ResponseEntity<Message> getAllCourses(@Validated(CourseDto.ConsultFromInstructor.class) @RequestBody CourseDto dto) {
        return courseService.findAllByInstructor(dto);
    }

    @PostMapping("/course-management/create-course")
    public ResponseEntity<Message> save(@Validated(CourseDto.Register.class) @RequestBody CourseDto dto) {
        return courseService.save(dto);
    }

    @PutMapping("/course-management/update-course")
    public ResponseEntity<Message> update(@Validated(CourseDto.Modify.class) @RequestBody CourseDto dto) {
        return courseService.update(dto);
    }
}
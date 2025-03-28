package com.eduhubpro.eduhubpro.Controllers.Global;

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
@RequestMapping("/global")
public class GlobalController {
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public GlobalController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @PostMapping("/user-management/create-user")
    public ResponseEntity<Message> createUser(@Validated(UserDto.Register.class) @RequestBody UserDto dto) {
        return userService.createUser(dto);
    }

    // Módulo cursos
    // Muestra todos los cursos publicados al main
    @GetMapping("/course-management/get-all-courses")
    public ResponseEntity<Message> getAllCourses() {
        return courseService.findAllPublished();
    }

    @GetMapping("/course-management/get-course-by-name")
    public ResponseEntity<Message> getByInstructorName(@Validated(CourseDto.ConsultFromInstructor.class) @RequestBody CourseDto dto) {
        return courseService.findByInstructorName(dto);
    }

    @GetMapping("/course-management/get-course-by-date")
    public ResponseEntity<Message> getCourseByDate(@Validated(CourseDto.ConsultDate.class) @RequestBody CourseDto dto) {
        return courseService.findByDate(dto);
    }
}
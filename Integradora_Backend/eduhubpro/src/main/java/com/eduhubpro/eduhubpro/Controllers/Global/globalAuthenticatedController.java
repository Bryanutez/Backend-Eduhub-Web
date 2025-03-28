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
@RequestMapping("/global-authenticated")
public class globalAuthenticatedController {
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public globalAuthenticatedController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    // Módulo usuarios
    @PutMapping("/user-management/update-profile")
    public ResponseEntity<Message> updateProfile(@Validated(UserDto.Modify.class) @RequestBody UserDto dto) {
        return userService.updateProfile(dto);
    }

    @PutMapping("/user-management/change-status-student")
    public ResponseEntity<Message> changeStatusUser(@Validated(UserDto.ChangeStatus.class) @RequestBody UserDto dto) {
        return userService.changeStatusStudent(dto);
    }
}
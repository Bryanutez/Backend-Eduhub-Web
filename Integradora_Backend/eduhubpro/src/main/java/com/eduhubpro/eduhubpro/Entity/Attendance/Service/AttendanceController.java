/*package com.eduhubpro.eduhubpro.Collections.Attendance.Service;

import com.eduhubpro.eduhubpro.Collections.Attendance.Model.AttendanceDto;
import com.eduhubpro.eduhubpro.Util.Response.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/attendance")
@RestController
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/all-attendances")
    public ResponseEntity<Message> getById(@Validated(AttendanceDto.Consult.class) @RequestBody AttendanceDto dto) {
        return attendanceService.getById(dto);
    }
}*/
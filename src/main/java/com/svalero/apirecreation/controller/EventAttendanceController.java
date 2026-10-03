package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.EventAttendance;
import com.svalero.apirecreation.domain.dto.EventAttendanceDTO;
import com.svalero.apirecreation.service.EventAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class EventAttendanceController {

    @Autowired
    private EventAttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<EventAttendance>> getAllAttendances() {
        return new ResponseEntity<>(attendanceService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventAttendance> createAttendance(@RequestBody EventAttendanceDTO dto) {
        EventAttendance created = attendanceService.registerAttendance(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        attendanceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

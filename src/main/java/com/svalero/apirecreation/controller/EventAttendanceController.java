package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.dto.EventAttendanceDTO;
import com.svalero.apirecreation.domain.dto.EventAttendanceOutDto;
import com.svalero.apirecreation.service.EventAttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/attendances")
public class EventAttendanceController {

    @Autowired
    private EventAttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<EventAttendanceOutDto>> getAllAttendances() {
        return new ResponseEntity<>(attendanceService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventAttendanceOutDto> createAttendance(@RequestBody EventAttendanceDTO dto) {
        EventAttendanceOutDto created = attendanceService.registerAttendance(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        attendanceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
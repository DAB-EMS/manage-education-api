package com.example.manageeducation.controller;

import com.example.manageeducation.service.ClassCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class ClassCalendarController {

    @Autowired
    ClassCalendarService classCalendarService;

    @GetMapping("syllabus/training-program/training-class/class-calendars")
    public ResponseEntity<?> classLocation() {
        return ResponseEntity.ok(classCalendarService.calendarResponse());
    }
}

package com.example.manageeducation.trainingclassservice.service;

import com.example.manageeducation.trainingclassservice.dto.response.CalendarClassResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassCalendarService {
    List<CalendarClassResponse> calendarResponse();
}

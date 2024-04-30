package com.example.manageeducation.service;

import com.example.manageeducation.dto.response.CalendarClassResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassCalendarService {
    List<CalendarClassResponse> calendarResponse();
}

package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.response.CalendarClassResponse;
import com.example.manageeducation.entity.ClassCalendar;
import com.example.manageeducation.repository.ClassCalendarRepository;
import com.example.manageeducation.repository.ClassLocationRepository;
import com.example.manageeducation.service.ClassCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassCalendarServiceImpl implements ClassCalendarService {

    @Autowired
    ClassCalendarRepository classCalendarRepository;

    @Override
    public List<CalendarClassResponse> calendarResponse() {
        List<CalendarClassResponse> calendarClassResponses = new ArrayList<>();
        List<ClassCalendar> classCalendars = classCalendarRepository.findAll();
        for(ClassCalendar classCalendar:classCalendars){
            CalendarClassResponse calendarClassResponse = new CalendarClassResponse();
            calendarClassResponse.setName(classCalendar.getTrainingClass().getName());
            calendarClassResponse.setCode(classCalendar.getTrainingClass().getCourseCode());
            calendarClassResponse.setDateTime(classCalendar.getDateTime());
            calendarClassResponse.setBeginTime(classCalendar.getBeginTime());
            calendarClassResponse.setEndTime(classCalendar.getEndTime());
            calendarClassResponse.setCreateBy(classCalendar.getTrainingClass().getCreatedBy().getFullName());
            calendarClassResponses.add(calendarClassResponse);
        }
        return calendarClassResponses;
    }
}

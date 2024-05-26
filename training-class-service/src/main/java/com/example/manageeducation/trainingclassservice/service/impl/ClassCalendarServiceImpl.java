package com.example.manageeducation.trainingclassservice.service.impl;

import com.example.manageeducation.trainingclassservice.dto.response.CalendarClassResponse;
import com.example.manageeducation.trainingclassservice.model.ClassCalendar;
import com.example.manageeducation.trainingclassservice.repository.ClassCalendarRepository;
import com.example.manageeducation.trainingclassservice.service.ClassCalendarService;
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
            calendarClassResponse.setCreateBy(String.valueOf(classCalendar.getTrainingClass().getCreatedBy()));
            calendarClassResponses.add(calendarClassResponse);
        }
        return calendarClassResponses;
    }
}

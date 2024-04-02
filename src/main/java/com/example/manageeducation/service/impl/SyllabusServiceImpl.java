package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.request.SyllabusDayRequest;
import com.example.manageeducation.dto.request.SyllabusRequest;
import com.example.manageeducation.dto.request.SyllabusUnitChapterRequest;
import com.example.manageeducation.dto.request.SyllabusUnitRequest;
import com.example.manageeducation.entity.*;
import com.example.manageeducation.enums.SyllabusDayStatus;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.*;
import com.example.manageeducation.service.SyllabusService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SyllabusServiceImpl implements SyllabusService {

    @Autowired
    SyllabusRepository syllabusRepository;

    @Autowired
    SyllabusDayRepository syllabusDayRepository;

    @Autowired
    SyllabusLevelRepository syllabusLevelRepository;

    @Autowired
    SyllabusUnitRepository syllabusUnitRepository;

    @Autowired
    SyllabusUnitChapterRepository syllabusUnitChapterRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    AssessmentSchemeRepository assessmentSchemeRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public String createSyllabus(SyllabusRequest dto) {
        AssessmentScheme assessmentScheme = getAssessmentScheme(dto);
        AssessmentScheme saveAssessmentScheme = assessmentSchemeRepository.save(assessmentScheme);

        //check validation level
        Optional<SyllabusLevel> syllabusLevelOptional = syllabusLevelRepository.findById(dto.getSyllabusLevel());
        if(syllabusLevelOptional.isEmpty()){
            throw new BadRequestException("Syllabus level id not found.");
        }

        //check validation customer
        Optional<Customer> customerOptional = customerRepository.findById(dto.getCreatedBy().toString());
        if(customerOptional.isEmpty()){
            throw new BadRequestException("Create by id not found.");
        }

        Syllabus syllabus = getSyllabus(dto, saveAssessmentScheme, syllabusLevelOptional);
        Syllabus savedSyllabus = syllabusRepository.save(syllabus);

        //save syllabus days
        for(SyllabusDayRequest syllabusDay: dto.getSyllabusDays()){
            SyllabusDay syllabusDay1 = new SyllabusDay();
            syllabusDay1.setDayNo(syllabusDay.getDayNo());
            syllabusDay1.setSyllabus(savedSyllabus);
            syllabusDay1.setStatus(SyllabusDayStatus.AVAILABLE);
            SyllabusDay savedSyllabusDay = syllabusDayRepository.save(syllabusDay1);

            //save syllabus unit
            for(SyllabusUnitRequest syllabusUnitRequest: syllabusDay.getSyllabusUnits()){
                SyllabusUnit syllabusUnit = new SyllabusUnit();
                syllabusUnit.setName(syllabusUnitRequest.getName());
                syllabusUnit.setDuration(syllabusUnitRequest.getDuration());
                syllabusUnit.setUnitNo(syllabusUnitRequest.getUnitNo());
                syllabusUnit.setSyllabus(savedSyllabus);
                syllabusUnit.setSyllabusDay(savedSyllabusDay);
                SyllabusUnit savedSyllabusUnit = syllabusUnitRepository.save(syllabusUnit);

                //save unit chapter
                for(SyllabusUnitChapterRequest syllabusUnitChapterRequest: syllabusUnitRequest.getSyllabusUnitChapters()){
                    SyllabusUnitChapter syllabusUnitChapter = new SyllabusUnitChapter();
                    syllabusUnitChapter.setDuration(syllabusUnitChapterRequest.getDuration());
                    syllabusUnitChapter.setOnline(syllabusUnitChapterRequest.isOnline());
                    syllabusUnitChapter.setName(syllabusUnitChapterRequest.getName());
                    syllabusUnitChapter.setSyllabusUnit(savedSyllabusUnit);

                }
            }
        }
        return "create successful";
    }

    private static Syllabus getSyllabus(SyllabusRequest dto, AssessmentScheme saveAssessmentScheme, Optional<SyllabusLevel> syllabusLevelOptional) {
        Syllabus syllabus = new Syllabus();
        syllabus.setName(dto.getName());
        syllabus.setCode(dto.getCode());
        syllabus.setVersion(dto.getVersion());
        syllabus.setAttendeeNumber(dto.getAttendeeNumber());
        syllabus.setTechnicalRequirement(dto.getTechnicalRequirement());
        syllabus.setCourseObjective(dto.getCourseObjective());
        syllabus.setDays(dto.getDays());
        syllabus.setHours(dto.getHours());
        syllabus.setStatus(dto.getStatus());
        syllabus.setTemplate(dto.isTemplate());
        syllabus.setCreatedBy(dto.getCreatedBy());
        syllabus.setCreatedDate(dto.getCreatedDate());
        syllabus.setUpdatedBy(dto.getUpdatedBy());
        syllabus.setUpdatedDate(dto.getUpdatedDate());
        syllabus.setAssessmentScheme(saveAssessmentScheme);
        syllabus.setSyllabusLevel(syllabusLevelOptional.get());
        return syllabus;
    }

    private static AssessmentScheme getAssessmentScheme(SyllabusRequest dto) {
        AssessmentScheme assessmentScheme = new AssessmentScheme();
        assessmentScheme.setAssignment(dto.getAssessmentScheme().getAssignment());
        assessmentScheme.setQuiz(dto.getAssessmentScheme().getQuiz());
        assessmentScheme.setExam(dto.getAssessmentScheme().getExam());
        assessmentScheme.setGpa(dto.getAssessmentScheme().getGpa());
        assessmentScheme.setFinalPoint(dto.getAssessmentScheme().getFinalPoint());
        assessmentScheme.setFinalPractice(dto.getAssessmentScheme().getFinalPractice());
        assessmentScheme.setFinalTheory(dto.getAssessmentScheme().getFinalTheory());
        return assessmentScheme;
    }
}

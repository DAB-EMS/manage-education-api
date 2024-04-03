package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.request.*;
import com.example.manageeducation.dto.response.OutputStandardResponse;
import com.example.manageeducation.dto.response.SyllabusResponse;
import com.example.manageeducation.dto.response.ViewSyllabusResponse;
import com.example.manageeducation.entity.*;
import com.example.manageeducation.enums.MaterialStatus;
import com.example.manageeducation.enums.SyllabusDayStatus;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.*;
import com.example.manageeducation.service.SyllabusService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    OutputStandardRepository outputStandardRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    DeliveryTypeRepository deliveryTypeRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public String createSyllabus(String id, SyllabusRequest dto) {
        LocalDate currentDate = LocalDate.now();
        Date date = java.sql.Date.valueOf(currentDate);

        //check validation level
        Optional<SyllabusLevel> syllabusLevelOptional = syllabusLevelRepository.findById(dto.getSyllabusLevel());
        if(syllabusLevelOptional.isEmpty()){
            throw new BadRequestException("Syllabus level id not found.");
        }

        //check validation customer
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isEmpty()){
            throw new BadRequestException("Create by id not found.");
        }

        //save syllabus
        Syllabus syllabus = getSyllabus(dto, syllabusLevelOptional, id, date);
        Syllabus savedSyllabus = syllabusRepository.save(syllabus);

        //save scheme
        AssessmentScheme assessmentScheme = getAssessmentScheme(dto, savedSyllabus);
        AssessmentScheme saveAssessmentScheme = assessmentSchemeRepository.save(assessmentScheme);

        //save syllabus days
        for(SyllabusDayRequest syllabusDay: dto.getSyllabusDays()){
            SyllabusDay syllabusDay1 = new SyllabusDay();
            syllabusDay1.setDayNo(syllabusDay.getDayNo());
            syllabusDay1.setSyllabus(savedSyllabus);
            syllabusDay1.setStatus(SyllabusDayStatus.AVAILABLE);
            syllabusDay1.setSyllabus(savedSyllabus);
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

                    //check output standard
                    Optional<OutputStandard> outputStandardOptional = outputStandardRepository.findById(syllabusUnitChapterRequest.getOutputStandardId());
                    if(outputStandardOptional.isEmpty()){
                        throw new BadRequestException("Output standard id is not found.");
                    }

                    //check delivery type
                    Optional<DeliveryType> deliveryTypeOptional = deliveryTypeRepository.findById(syllabusUnitChapterRequest.getDeliveryTypeId());
                    if(deliveryTypeOptional.isEmpty()){
                        throw new BadRequestException("Delivery type id is not found.");
                    }

                    syllabusUnitChapter.setOutputStandard(outputStandardOptional.get());
                    syllabusUnitChapter.setDeliveryType(deliveryTypeOptional.get());
                    SyllabusUnitChapter savedSyllabusUnitChapter = syllabusUnitChapterRepository.save(syllabusUnitChapter);

                    //save material
                    for(MaterialRequest materialRequest: syllabusUnitChapterRequest.getMaterials()){
                        Material material = new Material();
                        material.setName(materialRequest.getName());
                        material.setUrl(materialRequest.getUrl());
                        material.setCreatedBy(customerOptional.get().getId());
                        material.setCreatedDate(date);
                        material.setUpdatedBy(customerOptional.get().getId());
                        material.setUpdatedDate(date);
                        material.setMaterialStatus(MaterialStatus.ACTIVE);
                        material.setUnitChapter(savedSyllabusUnitChapter);
                        materialRepository.save(material);
                    }
                }
            }
        }
        return "create successful";
    }

    @Override
    public SyllabusResponse syllabus(UUID id) {
        Optional<Syllabus> syllabusOptional = syllabusRepository.findById(id);
        if(syllabusOptional.isPresent()){
            return modelMapper.map(syllabusOptional.get(),SyllabusResponse.class);
        }else {
            throw new BadRequestException("Syllabus id is not found.");
        }
    }

    @Override
    public List<ViewSyllabusResponse> syllabuses(String search, Date date) {
        List<Syllabus> syllabusList;
        if (search == null && date == null) {
            syllabusList = syllabusRepository.findAll();
        } else {
            syllabusList = syllabusRepository.findAllByNameAndCodeAndCreatedByAndCreatedDate(search, search, search, date);
        }

        return syllabusList.stream()
                .map(this::mapToViewSyllabusResponse)
                .collect(Collectors.toList());
    }

    @Override
    public String duplicatedSyllabus(UUID id) {
        Optional<Syllabus> syllabusOptional = syllabusRepository.findById(id);
        if(syllabusOptional.isPresent()){
            Syllabus syllabusFirst = syllabusOptional.get();

            LocalDate currentDate = LocalDate.now();
            Date date = java.sql.Date.valueOf(currentDate);

            //check validation level
            Optional<SyllabusLevel> syllabusLevelOptional = syllabusLevelRepository.findById(syllabusFirst.getSyllabusLevel().getId());
            if(syllabusLevelOptional.isEmpty()){
                throw new BadRequestException("Syllabus level id not found.");
            }

            //check validation customer
            Optional<Customer> customerOptional = customerRepository.findById(syllabusFirst.getCreatedBy());
            if(customerOptional.isEmpty()){
                throw new BadRequestException("Create by id not found.");
            }

            //save syllabus
            Syllabus syllabus = getSyllabus(modelMapper.map(syllabusFirst,SyllabusRequest.class), syllabusLevelOptional, syllabusFirst.getCreatedBy(), date);
            Syllabus savedSyllabus = syllabusRepository.save(syllabus);

            //save scheme
            AssessmentScheme assessmentScheme = getAssessmentScheme(modelMapper.map(syllabusFirst,SyllabusRequest.class), savedSyllabus);
            assessmentSchemeRepository.save(assessmentScheme);

            //save syllabus days
            for(SyllabusDay syllabusDay: syllabusFirst.getSyllabusDays()){
                SyllabusDay syllabusDay1 = new SyllabusDay();
                syllabusDay1.setDayNo(syllabusDay.getDayNo());
                syllabusDay1.setSyllabus(savedSyllabus);
                syllabusDay1.setStatus(SyllabusDayStatus.AVAILABLE);
                syllabusDay1.setSyllabus(savedSyllabus);
                SyllabusDay savedSyllabusDay = syllabusDayRepository.save(syllabusDay1);

                //save syllabus unit
                for(SyllabusUnit syllabusUnitRequest: syllabusDay.getSyllabusUnits()){
                    SyllabusUnit syllabusUnit = new SyllabusUnit();
                    syllabusUnit.setName(syllabusUnitRequest.getName());
                    syllabusUnit.setDuration(syllabusUnitRequest.getDuration());
                    syllabusUnit.setUnitNo(syllabusUnitRequest.getUnitNo());
                    syllabusUnit.setSyllabus(savedSyllabus);
                    syllabusUnit.setSyllabusDay(savedSyllabusDay);
                    SyllabusUnit savedSyllabusUnit = syllabusUnitRepository.save(syllabusUnit);

                    //save unit chapter
                    for(SyllabusUnitChapter syllabusUnitChapterRequest: syllabusUnitRequest.getSyllabusUnitChapters()){
                        SyllabusUnitChapter syllabusUnitChapter = new SyllabusUnitChapter();
                        syllabusUnitChapter.setDuration(syllabusUnitChapterRequest.getDuration());
                        syllabusUnitChapter.setOnline(syllabusUnitChapterRequest.isOnline());
                        syllabusUnitChapter.setName(syllabusUnitChapterRequest.getName());
                        syllabusUnitChapter.setSyllabusUnit(savedSyllabusUnit);

                        //check output standard
                        Optional<OutputStandard> outputStandardOptional = outputStandardRepository.findById(syllabusUnitChapterRequest.getOutputStandard().getId());
                        if(outputStandardOptional.isEmpty()){
                            throw new BadRequestException("Output standard id is not found.");
                        }

                        //check delivery type
                        Optional<DeliveryType> deliveryTypeOptional = deliveryTypeRepository.findById(syllabusUnitChapterRequest.getDeliveryType().getId());
                        if(deliveryTypeOptional.isEmpty()){
                            throw new BadRequestException("Delivery type id is not found.");
                        }

                        syllabusUnitChapter.setOutputStandard(outputStandardOptional.get());
                        syllabusUnitChapter.setDeliveryType(deliveryTypeOptional.get());
                        SyllabusUnitChapter savedSyllabusUnitChapter = syllabusUnitChapterRepository.save(syllabusUnitChapter);

                        //save material
                        for(Material materialRequest: syllabusUnitChapterRequest.getMaterials()){
                            Material material = new Material();
                            material.setName(materialRequest.getName());
                            material.setUrl(materialRequest.getUrl());
                            material.setCreatedBy(customerOptional.get().getId());
                            material.setCreatedDate(date);
                            material.setUpdatedBy(customerOptional.get().getId());
                            material.setUpdatedDate(date);
                            material.setMaterialStatus(MaterialStatus.ACTIVE);
                            material.setUnitChapter(savedSyllabusUnitChapter);
                            materialRepository.save(material);
                            return "create successful";
                        }
                    }
                }
            }
            return "create fail.";
        }else{
            throw new BadRequestException("Syllabus id is not found.");
        }
    }

    private ViewSyllabusResponse mapToViewSyllabusResponse(Syllabus suSyllabus) {
        ViewSyllabusResponse viewSyllabusResponse = new ViewSyllabusResponse();
        viewSyllabusResponse.setId(suSyllabus.getId());
        viewSyllabusResponse.setName(suSyllabus.getName());
        viewSyllabusResponse.setCode(suSyllabus.getCode());
        viewSyllabusResponse.setCreateOn(suSyllabus.getCreatedDate());
        viewSyllabusResponse.setCreateBy(suSyllabus.getCreatedBy());
        viewSyllabusResponse.setDuration(suSyllabus.getSyllabusDays().size());

        List<OutputStandardResponse> outputStandardResponses = suSyllabus.getSyllabusDays().stream()
                .flatMap(syllabusDay -> syllabusDay.getSyllabusUnits().stream())
                .flatMap(syllabusUnit -> syllabusUnit.getSyllabusUnitChapters().stream())
                .map(syllabusUnitChapter -> {
                    OutputStandardResponse outputStandardResponse = new OutputStandardResponse();
                    outputStandardResponse.setId(syllabusUnitChapter.getOutputStandard().getId());
                    outputStandardResponse.setName(syllabusUnitChapter.getOutputStandard().getCode());
                    return outputStandardResponse;
                })
                .distinct()
                .collect(Collectors.toList());

        viewSyllabusResponse.setOutputStandard(outputStandardResponses);
        return viewSyllabusResponse;
    }


    private static Syllabus getSyllabus(SyllabusRequest dto, Optional<SyllabusLevel> syllabusLevelOptional, String id, Date date) {
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
        syllabus.setCreatedBy(id);
        syllabus.setCreatedDate(date);
        syllabus.setUpdatedBy(id);
        syllabus.setUpdatedDate(date);
        syllabus.setSyllabusLevel(syllabusLevelOptional.get());
        return syllabus;
    }

    private static AssessmentScheme getAssessmentScheme(SyllabusRequest dto, Syllabus savedSyllabus) {
        AssessmentScheme assessmentScheme = new AssessmentScheme();
        assessmentScheme.setAssignment(dto.getAssessmentScheme().getAssignment());
        assessmentScheme.setQuiz(dto.getAssessmentScheme().getQuiz());
        assessmentScheme.setExam(dto.getAssessmentScheme().getExam());
        assessmentScheme.setGpa(dto.getAssessmentScheme().getGpa());
        assessmentScheme.setFinalPoint(dto.getAssessmentScheme().getFinalPoint());
        assessmentScheme.setFinalPractice(dto.getAssessmentScheme().getFinalPractice());
        assessmentScheme.setFinalTheory(dto.getAssessmentScheme().getFinalTheory());
        assessmentScheme.setSyllabus(savedSyllabus);
        return assessmentScheme;
    }
}

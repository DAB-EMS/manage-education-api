package com.example.manageeducation.service.impl;

import com.example.manageeducation.Utils.SecurityUtil;
import com.example.manageeducation.dto.request.*;
import com.example.manageeducation.dto.response.OutputStandardResponse;
import com.example.manageeducation.dto.response.SyllabusResponse;
import com.example.manageeducation.dto.response.ViewSyllabusResponse;
import com.example.manageeducation.entity.*;
import com.example.manageeducation.enums.MaterialStatus;
import com.example.manageeducation.enums.SyllabusDayStatus;
import com.example.manageeducation.enums.SyllabusStatus;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.*;
import com.example.manageeducation.service.SyllabusService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
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

    @Autowired
    SecurityUtil securityUtil;

    @Override
    public String createSyllabus(Principal principal, SyllabusRequest dto) {
        LocalDate currentDate = LocalDate.now();
        Date date = java.sql.Date.valueOf(currentDate);

        //check validation level
        Optional<SyllabusLevel> syllabusLevelOptional = syllabusLevelRepository.findById(dto.getSyllabusLevel());
        if(syllabusLevelOptional.isEmpty()){
            throw new BadRequestException("Syllabus level id not found.");
        }

        //check validation customer
        Optional<Customer> customerOptional = customerRepository.findById(securityUtil.getLoginUser(principal).getId());
        if(customerOptional.isEmpty()){
            throw new BadRequestException("Create by id not found.");
        }

        //save syllabus
        Syllabus syllabus = getSyllabus(dto, syllabusLevelOptional, securityUtil.getLoginUser(principal).getId(), date);
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
    public SyllabusRequest importSyllabus(Principal principal, MultipartFile file) {
        SyllabusRequest request = new SyllabusRequest();
        try (InputStream inputStream = file.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            SyllabusRequest syllabusRequest = new SyllabusRequest();
            DataFormatter dataFormatter = new DataFormatter();

            Row syllabusInfoRow = sheet.getRow(2);
            syllabusRequest.setName(dataFormatter.formatCellValue(syllabusInfoRow.getCell(3))); // Name
            syllabusRequest.setCode(dataFormatter.formatCellValue(sheet.getRow(3).getCell(3))); // Code
            syllabusRequest.setVersion(dataFormatter.formatCellValue(sheet.getRow(4).getCell(3))); // Version

            Row technicalRequirementRow = sheet.getRow(22);
            syllabusRequest.setTechnicalRequirement(dataFormatter.formatCellValue(technicalRequirementRow.getCell(4))); // Technical Requirement

            Row courseObjectiveRow = sheet.getRow(6);
            syllabusRequest.setCourseObjective(dataFormatter.formatCellValue(courseObjectiveRow.getCell(3))); // Course Objective
            syllabusRequest.setCourseObjective(syllabusRequest.getCourseObjective() + " " + dataFormatter.formatCellValue(sheet.getRow(11).getCell(3))); // Course Objective

            //get Assessment SchemeRequest from file excel
            AssessmentSchemeRequest assessmentSchemeRequest = new AssessmentSchemeRequest();
            assessmentSchemeRequest.setQuiz(Double.valueOf(dataFormatter.formatCellValue(sheet.getRow(23).getCell(4))));
            assessmentSchemeRequest.setAssignment(Double.valueOf(dataFormatter.formatCellValue(sheet.getRow(24).getCell(4))));
            assessmentSchemeRequest.setFinalTheory(Double.valueOf(dataFormatter.formatCellValue(sheet.getRow(25).getCell(4))));
            assessmentSchemeRequest.setFinalPractice(Double.valueOf(dataFormatter.formatCellValue(sheet.getRow(26).getCell(4))));
            assessmentSchemeRequest.setGpa(Double.valueOf(dataFormatter.formatCellValue(sheet.getRow(27).getCell(4))));
            assessmentSchemeRequest.setFinalPoint(assessmentSchemeRequest.getQuiz()+assessmentSchemeRequest.getAssignment());
            syllabusRequest.setAssessmentScheme(assessmentSchemeRequest);



            un( principal,  file);
            return syllabusRequest;
        } catch (IOException e) {
            throw new BadRequestException("Please fill in all information and use the correct excel file downloaded from the system.");
        }

    }

    public void un(Principal principal, MultipartFile file){
        System.out.println("###################################################################################");
        SyllabusRequest request = new SyllabusRequest();
        try (InputStream inputStream = file.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            int rowIndex = 0;

            DataFormatter dataFormatter = new DataFormatter();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                int columnIndex = 0;
                for (Cell cell : row) {
                    // Xử lý dữ liệu ở đây
                    String cellValue = dataFormatter.formatCellValue(cell);
                    System.out.println("Row: " + rowIndex + ", Column: " + columnIndex + ", Value: " + cellValue);

                    columnIndex++;
                }

                rowIndex++;
            }

        } catch (IOException e) {
            throw new BadRequestException("Please fill in all information and use the correct excel file downloaded from the system.");
        }
    }

    @Override
    public SyllabusResponse syllabus(UUID id) {
        Optional<Syllabus> syllabusOptional = syllabusRepository.findByIdAndStatus(id,SyllabusStatus.ACTIVE);
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
            syllabusList = syllabusRepository.findAllByStatus(SyllabusStatus.ACTIVE);
        } else {
            syllabusList = syllabusRepository.findAllByNameAndCodeAndCreatedByAndCreatedDateAndStatus(search, search, search, date,SyllabusStatus.ACTIVE);
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

    @Override
    public String deleteSyllabus(UUID id) {
        Optional<Syllabus> syllabusOptional = syllabusRepository.findById(id);
        if(syllabusOptional.isPresent()){
            Syllabus syllabus = syllabusOptional.get();
            syllabus.setStatus(SyllabusStatus.DELETED);
            syllabusRepository.save(syllabus);
            return "Delete successful.";
        }else{
            throw new BadRequestException("Syllabus id is not found.");
        }
    }

    @Override
    public Syllabus updateSyllabus(UUID id, SyllabusUpdateRequest dto) {
        Optional<Syllabus> syllabusOptional = syllabusRepository.findById(id);
        if(syllabusOptional.isPresent()){
            Syllabus syllabus = syllabusOptional.get();
            modelMapper.map(dto, syllabus);
            syllabusRepository.save(syllabus);
            Optional<AssessmentScheme> assessmentSchemeOptional = assessmentSchemeRepository.findById(syllabus.getAssessmentScheme().getId());
            if(assessmentSchemeOptional.isPresent()){
                AssessmentScheme assessmentScheme = assessmentSchemeOptional.get();
                modelMapper.map(dto, assessmentScheme);
                assessmentSchemeRepository.save(assessmentScheme);

                Optional<SyllabusLevel> syllabusLevelOptional = syllabusLevelRepository.findById(syllabus.getSyllabusLevel().getId());
                if(syllabusLevelOptional.isPresent()){
                    SyllabusLevel syllabusLevel = syllabusLevelOptional.get();
                    modelMapper.map(dto, syllabusLevel);
                    syllabusLevelRepository.save(syllabusLevel);
                    return syllabus;
                }else {
                    throw new BadRequestException("Assessment Scheme id is not found.");
                }
            }else {
                throw new BadRequestException("Assessment Scheme id is not found.");
            }
        }else{
            throw new BadRequestException("Syllabus id is not found.");
        }
    }

    @Override
    public String deActive(UUID id) {
        Optional<Syllabus> syllabusOptional = syllabusRepository.findById(id);
        if(syllabusOptional.isPresent()){
            Syllabus syllabus = syllabusOptional.get();
            syllabus.setStatus(SyllabusStatus.DEACTIVE);
            syllabusRepository.save(syllabus);
            return "De-active successful.";
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

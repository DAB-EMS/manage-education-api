package com.example.manageeducation.syllabusservice.service.impl;

import com.example.manageeducation.syllabusservice.dto.RequestForListOfSyllabus;
import com.example.manageeducation.syllabusservice.jdbc.SyllabusJdbc;
import com.example.manageeducation.syllabusservice.utils.SecurityUtil;
import com.example.manageeducation.syllabusservice.client.CustomerClient;
import com.example.manageeducation.syllabusservice.dto.Customer;
import com.example.manageeducation.syllabusservice.dto.request.*;
import com.example.manageeducation.syllabusservice.dto.response.*;
import com.example.manageeducation.syllabusservice.enums.MaterialStatus;
import com.example.manageeducation.syllabusservice.enums.SyllabusDayStatus;
import com.example.manageeducation.syllabusservice.enums.SyllabusStatus;
import com.example.manageeducation.syllabusservice.exception.BadRequestException;
import com.example.manageeducation.syllabusservice.model.*;
import com.example.manageeducation.syllabusservice.repository.*;
import com.example.manageeducation.syllabusservice.service.OutputStandardService;
import com.example.manageeducation.syllabusservice.service.SyllabusService;
import com.example.manageeducation.syllabusservice.utils.SyllabusServiceUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SyllabusServiceImpl implements SyllabusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyllabusServiceImpl.class);

    @Autowired
    SyllabusRepository syllabusRepository;

    @Autowired
    DeliveryPrincipleRepository deliveryPrincipleRepository;

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
    CustomerClient customerRepository;

    @Autowired
    DeliveryTypeRepository deliveryTypeRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    SyllabusServiceUtils syllabusServiceUtils;

    @Autowired
    SyllabusJdbc syllabusJdbc;

    @Autowired
    OutputStandardService outputStandardService;

    @Override
    public String createSyllabus(Principal principal, SyllabusRequest dto) {
        LocalDate currentDate = LocalDate.now();
        Date date = java.sql.Date.valueOf(currentDate);

        if(dto.getSyllabusDays().isEmpty()){
            throw new BadRequestException("Syllabus day is not found.");
        }

        if(dto.getAssessmentScheme()==null){
            throw new BadRequestException("Assessment scheme is not found.");
        }

        for(SyllabusDayRequest checkNull: dto.getSyllabusDays()){
            if(checkNull.getSyllabusUnits().isEmpty()){
                throw new BadRequestException("Syllabus unit is not found.");
            }
        }

        //check validation level
        Optional<SyllabusLevel> syllabusLevelOptional = syllabusLevelRepository.findById(dto.getSyllabusLevel());
        if(syllabusLevelOptional.isEmpty()){
            throw new BadRequestException("Syllabus level id not found.");
        }

        //check validation customer
        Optional<Customer> customerOptional = customerRepository.getCustomerById(securityUtil.getLoginUser(principal).getId());
        if(customerOptional.isEmpty()){
            throw new BadRequestException("Create by id not found.");
        }

        //save syllabus
        Syllabus syllabus = getSyllabus(dto, syllabusLevelOptional, securityUtil.getLoginUser(principal).getId(), date);
        Syllabus savedSyllabus = syllabusRepository.save(syllabus);

        //save scheme
        AssessmentScheme assessmentScheme = getAssessmentScheme(dto, savedSyllabus);
        assessmentSchemeRepository.save(assessmentScheme);

        //save delivery principle
        DeliveryPrinciple deliveryPrinciple = getDeliveryPrinciple(dto, savedSyllabus);
        deliveryPrincipleRepository.save(deliveryPrinciple);

        //save syllabus days
        int totalDay = 0;
        int totalHours = 0;
        for(SyllabusDayRequest syllabusDay: dto.getSyllabusDays()){
            totalDay ++;
            SyllabusDay syllabusDay1 = new SyllabusDay();
            syllabusDay1.setDayNo(syllabusDay.getDayNo());
            syllabusDay1.setSyllabus(savedSyllabus);
            syllabusDay1.setStatus(SyllabusDayStatus.AVAILABLE);
            syllabusDay1.setSyllabus(savedSyllabus);
            SyllabusDay savedSyllabusDay = syllabusDayRepository.save(syllabusDay1);

            //save syllabus unit
            for(SyllabusUnitRequest syllabusUnitRequest: syllabusDay.getSyllabusUnits()){
                int durations = 0;
                SyllabusUnit syllabusUnit = new SyllabusUnit();
                syllabusUnit.setName(syllabusUnitRequest.getName());
                //get duration
                for(SyllabusUnitChapterRequest syllabusUnitChapterRequest: syllabusUnitRequest.getSyllabusUnitChapters()){
                    durations += (int) syllabusUnitChapterRequest.getDuration();
                }
                totalHours += durations;
                syllabusUnit.setDuration(durations);
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

        //update hour and day of syllabus
        Optional<Syllabus> syllabusOptional = syllabusRepository.findById(savedSyllabus.getId());
        if(syllabusOptional.isPresent()){
            Syllabus syllabus1 = syllabusOptional.get();
            syllabus1.setHours(totalHours);
            syllabus1.setDays(totalDay);
            syllabusRepository.save(syllabus1);
            return "Create successfully.";
        }else{
            throw new BadRequestException("Syllabus id is not found.");
        }
    }

    @Override
    public String importSyllabus(Principal principal, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            SyllabusImportRequest syllabusRequest = new SyllabusImportRequest();
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

            //get Delivery Principle from file excel
            DeliveryPrincipleImportRequest deliveryPrincipleImportRequest = new DeliveryPrincipleImportRequest();
            deliveryPrincipleImportRequest.setTrainees(dataFormatter.formatCellValue(sheet.getRow(28).getCell(4)));
            deliveryPrincipleImportRequest.setTrainer(dataFormatter.formatCellValue(sheet.getRow(29).getCell(4)));
            deliveryPrincipleImportRequest.setTraining(dataFormatter.formatCellValue(sheet.getRow(30).getCell(4)));
            deliveryPrincipleImportRequest.setRe_test(dataFormatter.formatCellValue(sheet.getRow(31).getCell(4)));
            deliveryPrincipleImportRequest.setMarking(dataFormatter.formatCellValue(sheet.getRow(32).getCell(4)));
            deliveryPrincipleImportRequest.setWaiverCriteria(dataFormatter.formatCellValue(sheet.getRow(33).getCell(4)));
            deliveryPrincipleImportRequest.setOthers(dataFormatter.formatCellValue(sheet.getRow(34).getCell(4)));
            syllabusRequest.setDeliveryPrinciple(deliveryPrincipleImportRequest);

            //List standard output
            List<OutputStandardRequest> standardRequests = new ArrayList<>();
            for(int i = 8; i<11;i++){
                OutputStandardRequest outputStandardRequest = new OutputStandardRequest();
                outputStandardRequest.setCode(dataFormatter.formatCellValue(sheet.getRow(i).getCell(4)));
                outputStandardRequest.setName(dataFormatter.formatCellValue(sheet.getRow(i).getCell(3)));
                outputStandardRequest.setDescription(dataFormatter.formatCellValue(sheet.getRow(i).getCell(5)));
                standardRequests.add(outputStandardRequest);
            }
            syllabusRequest.setStandardRequests(standardRequests);

            //get day
            List<SyllabusDayImportRequest> syllabusDays = new ArrayList<>();
            String days = dataFormatter.formatCellValue(sheet.getRow(12).getCell(3));
            List<Integer> dayNumbers = extractDayNumbers(days);
            for (int day : dayNumbers) {
                SyllabusDayImportRequest syllabusDayRequest = new SyllabusDayImportRequest();
                syllabusDayRequest.setDayNo(day);
                syllabusDayRequest.setStatus(SyllabusDayStatus.AVAILABLE);
                List<SyllabusUnitImportRequest> syllabusUnitRequests = new ArrayList<>();
                //sheet 2
                XSSFSheet sheet2 = workbook.getSheetAt(1);
                SyllabusUnitImportRequest syllabusUnitRequest = new SyllabusUnitImportRequest();
                if(day==1){
                    syllabusUnitRequest.setName(getStringAfterUnderscore(dataFormatter.formatCellValue(sheet2.getRow(2).getCell(1))));
                    syllabusUnitRequest.setUnitNo(getUnitNumber(dataFormatter.formatCellValue(sheet2.getRow(3).getCell(1))));
                    Double d = Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(3).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(4).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(5).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(6).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(7).getCell(5)));
                    Integer f = (int) Math.round(d);
                    syllabusUnitRequest.setDuration(f);
                }else if(day==2){
                    syllabusUnitRequest.setName(getStringAfterUnderscore(dataFormatter.formatCellValue(sheet2.getRow(8).getCell(1))));
                    syllabusUnitRequest.setUnitNo(getUnitNumber(dataFormatter.formatCellValue(sheet2.getRow(8).getCell(1))));
                    Double d = Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(8).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(9).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(10).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(11).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(12).getCell(5) )) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(13).getCell(5) )) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(14).getCell(5) ));
                    Integer f = (int) Math.round(d);
                    syllabusUnitRequest.setDuration(f);
                }else if(day==3){
                    syllabusUnitRequest.setName(getStringAfterUnderscore(dataFormatter.formatCellValue(sheet2.getRow(15).getCell(1))));
                    syllabusUnitRequest.setUnitNo(getUnitNumber(dataFormatter.formatCellValue(sheet2.getRow(15).getCell(1))));
                    Double d = Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(15).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(16).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(17).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(18).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(19).getCell(5) )) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(20).getCell(5) )) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(21).getCell(5) ));
                    Integer f = (int) Math.round(d);
                    syllabusUnitRequest.setDuration(f);
                }else{
                    syllabusUnitRequest.setName(getStringAfterUnderscore(dataFormatter.formatCellValue(sheet2.getRow(22).getCell(1))));
                    syllabusUnitRequest.setUnitNo(getUnitNumber(dataFormatter.formatCellValue(sheet2.getRow(22).getCell(1))));
                    Double d = Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(22).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(23).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(24).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(25).getCell(5))) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(26).getCell(5) )) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(27).getCell(5) )) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(28).getCell(5) )) + Double.valueOf(dataFormatter.formatCellValue(sheet2.getRow(29).getCell(5) ));
                    Integer f = (int) Math.round(d);
                    syllabusUnitRequest.setDuration(f);
                }


                //List unit chapter
                List<SyllabusUnitChapterRequest> syllabusUnitChapterRequests = new ArrayList<>();
                if(day==1){
                    for(int i = 3; i<=7;i++ ){
                        SyllabusUnitChapterRequest syllabusUnitChapterRequest1 = new SyllabusUnitChapterRequest();
                        syllabusUnitChapterRequest1.setName(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(3)));
                        syllabusUnitChapterRequest1.setOnline(true);
                        syllabusUnitChapterRequest1.setDuration(Double.parseDouble(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(5))));
                        //get ID delivery
                        Optional<DeliveryType> deliveryTypeOptional = deliveryTypeRepository.findByNameIgnoreCase(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(4)));
                        if(deliveryTypeOptional.isEmpty()){
                            throw new BadRequestException("Delivery is not found.");
                        }
                        syllabusUnitChapterRequest1.setDeliveryTypeId(deliveryTypeOptional.get().getId());
                        syllabusUnitChapterRequest1.setOutputStandardId(null);

                        //get material
                        List<MaterialRequest> materialRequests = new ArrayList<>();
                        List<String> resultList = splitByNewLine(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(6)));
                        for (String str : resultList) {
                            MaterialRequest materialRequest = new MaterialRequest();
                            materialRequest.setName(str);
                            materialRequest.setUrl(null);
                            materialRequests.add(materialRequest);
                        }

                        syllabusUnitChapterRequest1.setMaterials(materialRequests);
                        syllabusUnitChapterRequests.add(syllabusUnitChapterRequest1);
                    }
                    syllabusUnitRequest.setSyllabusUnitChapters(syllabusUnitChapterRequests);
                    syllabusUnitRequests.add(syllabusUnitRequest);
                    syllabusDayRequest.setSyllabusUnits(syllabusUnitRequests);
                    syllabusDays.add(syllabusDayRequest);
                }else if(day==2){
                    for(int i = 8; i<=14;i++ ){
                        SyllabusUnitChapterRequest syllabusUnitChapterRequest1 = new SyllabusUnitChapterRequest();
                        syllabusUnitChapterRequest1.setName(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(3)));
                        syllabusUnitChapterRequest1.setOnline(true);
                        syllabusUnitChapterRequest1.setDuration(Double.parseDouble(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(5))));
                        //get ID delivery
                        Optional<DeliveryType> deliveryTypeOptional = deliveryTypeRepository.findByNameIgnoreCase(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(4)));
                        if(deliveryTypeOptional.isEmpty()){
                            throw new BadRequestException("Delivery is not found.");
                        }
                        syllabusUnitChapterRequest1.setDeliveryTypeId(deliveryTypeOptional.get().getId());
                        syllabusUnitChapterRequest1.setOutputStandardId(null);

                        //get material
                        List<MaterialRequest> materialRequests = new ArrayList<>();
                        List<String> resultList = splitByNewLine(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(6)));
                        for (String str : resultList) {
                            MaterialRequest materialRequest = new MaterialRequest();
                            materialRequest.setName(str);
                            materialRequest.setUrl(null);
                            materialRequests.add(materialRequest);
                        }

                        syllabusUnitChapterRequest1.setMaterials(materialRequests);
                        syllabusUnitChapterRequests.add(syllabusUnitChapterRequest1);
                    }
                    syllabusUnitRequest.setSyllabusUnitChapters(syllabusUnitChapterRequests);
                    syllabusUnitRequests.add(syllabusUnitRequest);
                    syllabusDayRequest.setSyllabusUnits(syllabusUnitRequests);
                    syllabusDays.add(syllabusDayRequest);
                }else if(day==3){
                    for(int i = 15; i<=21;i++ ){
                        SyllabusUnitChapterRequest syllabusUnitChapterRequest1 = new SyllabusUnitChapterRequest();
                        syllabusUnitChapterRequest1.setName(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(3)));
                        syllabusUnitChapterRequest1.setOnline(true);
                        syllabusUnitChapterRequest1.setDuration(Double.parseDouble(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(5))));
                        //get ID delivery
                        Optional<DeliveryType> deliveryTypeOptional = deliveryTypeRepository.findByNameIgnoreCase(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(4)));
                        if(deliveryTypeOptional.isEmpty()){
                            throw new BadRequestException("Delivery is not found.");
                        }
                        syllabusUnitChapterRequest1.setDeliveryTypeId(deliveryTypeOptional.get().getId());
                        syllabusUnitChapterRequest1.setOutputStandardId(null);

                        //get material
                        List<MaterialRequest> materialRequests = new ArrayList<>();
                        List<String> resultList = splitByNewLine(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(6)));
                        for (String str : resultList) {
                            MaterialRequest materialRequest = new MaterialRequest();
                            materialRequest.setName(str);
                            materialRequest.setUrl(null);
                            materialRequests.add(materialRequest);
                        }

                        syllabusUnitChapterRequest1.setMaterials(materialRequests);
                        syllabusUnitChapterRequests.add(syllabusUnitChapterRequest1);
                    }
                    syllabusUnitRequest.setSyllabusUnitChapters(syllabusUnitChapterRequests);
                    syllabusUnitRequests.add(syllabusUnitRequest);
                    syllabusDayRequest.setSyllabusUnits(syllabusUnitRequests);
                    syllabusDays.add(syllabusDayRequest);
                }else{
                    for(int i = 22; i<=29;i++ ){
                        SyllabusUnitChapterRequest syllabusUnitChapterRequest1 = new SyllabusUnitChapterRequest();
                        syllabusUnitChapterRequest1.setName(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(3)));
                        syllabusUnitChapterRequest1.setOnline(true);
                        syllabusUnitChapterRequest1.setDuration(Double.parseDouble(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(5))));
                        //get ID delivery
                        Optional<DeliveryType> deliveryTypeOptional = deliveryTypeRepository.findByNameIgnoreCase(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(4)));
                        if(deliveryTypeOptional.isEmpty()){
                            throw new BadRequestException("Delivery is not found.");
                        }
                        syllabusUnitChapterRequest1.setDeliveryTypeId(deliveryTypeOptional.get().getId());
                        syllabusUnitChapterRequest1.setOutputStandardId(null);

                        //get material
                        List<MaterialRequest> materialRequests = new ArrayList<>();
                        List<String> resultList = splitByNewLine(dataFormatter.formatCellValue(sheet2.getRow(i).getCell(6)));
                        for (String str : resultList) {
                            MaterialRequest materialRequest = new MaterialRequest();
                            materialRequest.setName(str);
                            materialRequest.setUrl(null);
                            materialRequests.add(materialRequest);
                        }

                        syllabusUnitChapterRequest1.setMaterials(materialRequests);
                        syllabusUnitChapterRequests.add(syllabusUnitChapterRequest1);
                    }
                    syllabusUnitRequest.setSyllabusUnitChapters(syllabusUnitChapterRequests);
                    syllabusUnitRequests.add(syllabusUnitRequest);
                    syllabusDayRequest.setSyllabusUnits(syllabusUnitRequests);
                    syllabusDays.add(syllabusDayRequest);
                }

            }
            syllabusRequest.setSyllabusDays(syllabusDays);
            syllabusRequest.setDeliveryPrinciple(deliveryPrincipleImportRequest);

            //save data excel into database
            return createSyllabusByImport(principal, syllabusRequest);
//            un(principal, file);
//            return syllabusRequest;
        } catch (IOException e) {
            throw new BadRequestException("Please fill in all information and use the correct excel file downloaded from the system.");
        }

    }

    public String createSyllabusByImport(Principal principal, SyllabusImportRequest dto) {
        LocalDate currentDate = LocalDate.now();
        Date date = java.sql.Date.valueOf(currentDate);

        //save standard from file excel
        for(OutputStandardRequest outputStandardRequest: dto.getStandardRequests()){
            outputStandardService.createOutputStandard(outputStandardRequest);
        }


        //check validation customer
        Optional<Customer> customerOptional = customerRepository.getCustomerById(securityUtil.getLoginUser(principal).getId());
        if(customerOptional.isEmpty()){
            throw new BadRequestException("Create by id not found.");
        }

        int days = 0;
        int hours = 0;
        for(SyllabusDayImportRequest syllabusDayCount: dto.getSyllabusDays()){
            days++;
            for(SyllabusUnitImportRequest syllabusUnit: syllabusDayCount.getSyllabusUnits()){
                hours += syllabusUnit.getDuration();
            }
        }
        //save syllabus
        dto.setStatus(SyllabusStatus.ACTIVE);
        dto.setDays(days);
        dto.setHours(hours);
        Syllabus syllabus = getSyllabusImport(modelMapper.map(dto,SyllabusRequest.class), securityUtil.getLoginUser(principal).getId(), date);
        Syllabus savedSyllabus = syllabusRepository.save(syllabus);

        //save Delivery Principle
        DeliveryPrinciple deliveryPrinciple = new DeliveryPrinciple();
        deliveryPrinciple.setSyllabus(savedSyllabus);
        modelMapper.map(dto.getDeliveryPrinciple(),deliveryPrinciple);
        deliveryPrincipleRepository.save(deliveryPrinciple);

        //save scheme
        AssessmentScheme assessmentScheme = new AssessmentScheme();
        assessmentScheme.setSyllabus(savedSyllabus);
        modelMapper.map(dto.getAssessmentScheme(),assessmentScheme);
        assessmentSchemeRepository.save(assessmentScheme);


        //save syllabus days
        for(SyllabusDayImportRequest syllabusDay: dto.getSyllabusDays()){
            SyllabusDay syllabusDay1 = new SyllabusDay();
            syllabusDay1.setDayNo(syllabusDay.getDayNo());
            syllabusDay1.setSyllabus(savedSyllabus);
            syllabusDay1.setStatus(SyllabusDayStatus.AVAILABLE);
            syllabusDay1.setSyllabus(savedSyllabus);
            SyllabusDay savedSyllabusDay = syllabusDayRepository.save(syllabusDay1);

            //save syllabus unit
            for(SyllabusUnitImportRequest syllabusUnitRequest: syllabusDay.getSyllabusUnits()){
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

                    //check delivery type
                    Optional<DeliveryType> deliveryTypeOptional = deliveryTypeRepository.findById(syllabusUnitChapterRequest.getDeliveryTypeId());
                    if(deliveryTypeOptional.isEmpty()){
                        throw new BadRequestException("Delivery type id is not found.");
                    }

                    syllabusUnitChapter.setOutputStandard(null);
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
        Optional<Syllabus> syllabusOptional = syllabusRepository.findByIdAndStatus(id,SyllabusStatus.ACTIVE);
        if(syllabusOptional.isPresent()){
            //logic calculation point component
            double test = Objects.requireNonNullElse(syllabusRepository.sumDurationBySyllabusIdAndDeliveryId(id, UUID.fromString("08e635bd-3a1d-40cc-b890-b723e4228ddf")), 0.0);
            double assignment = Objects.requireNonNullElse(syllabusRepository.sumDurationBySyllabusIdAndDeliveryId(id,UUID.fromString("9a226b15-038b-42ca-9a4d-ebf2f13a479a")),0.0);
            double concept = Objects.requireNonNullElse(syllabusRepository.sumDurationBySyllabusIdAndDeliveryId(id,UUID.fromString("bdd335ee-d8ae-4024-b01d-ed49d14aae9c")),0.0);
            double exam = Objects.requireNonNullElse(syllabusRepository.sumDurationBySyllabusIdAndDeliveryId(id,UUID.fromString("e3786dd5-4e73-41f8-bc97-1d7cffe8a313")),0.0);
            double guide = Objects.requireNonNullElse(syllabusRepository.sumDurationBySyllabusIdAndDeliveryId(id,UUID.fromString("e447bbe0-e681-4805-8486-924da7e2e40d")),0.0);
            double seminar = Objects.requireNonNullElse(syllabusRepository.sumDurationBySyllabusIdAndDeliveryId(id,UUID.fromString("af835041-2072-49e2-951b-f43b985573d5")),0.0);
            double sum = test + assignment + seminar + concept + exam + guide;

            double testPercentage = (test / sum) * 100;
            double assignmentPercentage = (assignment / sum) * 100;
            double seminarPercentage = (seminar / sum) * 100;
            double conceptPercentage = (concept / sum) * 100;
            double examPercentage = (exam / sum) * 100;
            double guidePercentage = (guide / sum) * 100;

            //other
            double remainingPercentage = 100 - (testPercentage + assignmentPercentage + seminarPercentage + conceptPercentage + examPercentage + guidePercentage);

            TimeAllocationResponse response = new TimeAllocationResponse();
            response.setTest(testPercentage);
            response.setAssignment(assignmentPercentage);
            response.setExam(examPercentage);
            response.setConcept(conceptPercentage);
            response.setGuides(guidePercentage);

            SyllabusResponse syllabusResponse = new SyllabusResponse();
            syllabusResponse.setTimeAllocationResponse(response);
            modelMapper.map(syllabusOptional.get(),syllabusResponse);
            return syllabusResponse;
        }else {
            throw new BadRequestException("Syllabus id is not found.");
        }
    }

    @Override
    public List<ViewSyllabusResponse> syllabuses() {
        List<Syllabus> syllabusList = syllabusRepository.findAllByStatus(SyllabusStatus.ACTIVE);
        List<ViewSyllabusResponse> viewSyllabusResponses = new ArrayList<>();

        for (Syllabus suSyllabus : syllabusList) {
            ViewSyllabusResponse viewSyllabusResponse = new ViewSyllabusResponse();
            viewSyllabusResponse.setId(suSyllabus.getId());
            viewSyllabusResponse.setName(suSyllabus.getName());
            viewSyllabusResponse.setCode(suSyllabus.getCode());
            viewSyllabusResponse.setCreateOn(suSyllabus.getCreatedDate());
            Optional<Customer> customerOptional = customerRepository.getCustomerById(suSyllabus.getCreatedBy());
            if(customerOptional.isPresent()){
                Customer customer = customerOptional.get();
                viewSyllabusResponse.setCreateBy(customer.getFullName());
            }else{
                viewSyllabusResponse.setCreateBy(null);
            }

            viewSyllabusResponse.setDuration(suSyllabus.getSyllabusDays().size());

            Set<String> outputStandardCodes = new HashSet<>();
            List<OutputStandardResponse> outputStandardResponses = new ArrayList<>();

            for (SyllabusDay syllabusDay : suSyllabus.getSyllabusDays()) {
                for (SyllabusUnit syllabusUnit : syllabusDay.getSyllabusUnits()) {
                    for (SyllabusUnitChapter syllabusUnitChapter : syllabusUnit.getSyllabusUnitChapters()) {
                        OutputStandard outputStandard = syllabusUnitChapter.getOutputStandard();
                        if (outputStandard != null) {
                            String code = outputStandard.getCode();
                            if (!outputStandardCodes.contains(code)) {
                                outputStandardCodes.add(code);
                                OutputStandardResponse outputStandardResponse = new OutputStandardResponse();
                                outputStandardResponse.setCode(code);
                                outputStandardResponses.add(outputStandardResponse);
                            }
                        }
                    }
                }
            }

            viewSyllabusResponse.setOutputStandard(outputStandardResponses);
            viewSyllabusResponses.add(viewSyllabusResponse);
        }

        return viewSyllabusResponses;
    }


    @Override
    public String duplicatedSyllabus(UUID id) {
        Optional<Syllabus> syllabusOptional = syllabusRepository.findById(id);
        if(syllabusOptional.isPresent()){
            Syllabus syllabusFirst = syllabusOptional.get();

            LocalDate currentDate = LocalDate.now();
            Date date = java.sql.Date.valueOf(currentDate);

            //check validation level
            Optional<SyllabusLevel> syllabusLevelOptional = null;
            if(syllabusFirst.getSyllabusLevel() != null && syllabusFirst.getSyllabusLevel().getId() != null){
                syllabusLevelOptional = syllabusLevelRepository.findById(syllabusFirst.getSyllabusLevel().getId());
                if(syllabusLevelOptional.isEmpty()){
                    throw new BadRequestException("Syllabus level id not found.");
                }
            }


            //check validation customer
            Optional<Customer> customerOptional = customerRepository.getCustomerById(syllabusFirst.getCreatedBy());
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
                        Optional<OutputStandard> outputStandardOptional=null;
                        if (syllabusUnitChapterRequest.getOutputStandard() != null && syllabusUnitChapterRequest.getOutputStandard().getId() != null) {
                            outputStandardOptional = outputStandardRepository.findById(syllabusUnitChapterRequest.getOutputStandard().getId());
                            if (outputStandardOptional.isEmpty()) {
                                throw new BadRequestException("Output standard id is not found.");
                            }
                        }


                        //check delivery type
                        Optional<DeliveryType> deliveryTypeOptional = deliveryTypeRepository.findById(syllabusUnitChapterRequest.getDeliveryType().getId());
                        if(deliveryTypeOptional.isEmpty()){
                            throw new BadRequestException("Delivery type id is not found.");
                        }

                        if(outputStandardOptional==null){
                            syllabusUnitChapter.setOutputStandard(null);
                        }else{
                            syllabusUnitChapter.setOutputStandard(outputStandardOptional.get());
                        }
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
                            return "Create duplication successfully.";
                        }
                    }
                }
            }
            return "Create fail.";
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
            return "Delete successfully.";
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
            return "De-active successfully.";
        }else{
            throw new BadRequestException("Syllabus id is not found.");
        }
    }

    @Override
    public List<SyllabusViewProgramResponse> viewSyllabusProgram() {
        List<SyllabusViewProgramResponse> syllabusViewProgramResponses = new ArrayList<>();
        List<Syllabus> syllabusList = syllabusRepository.findAllByStatus(SyllabusStatus.ACTIVE);
        for(Syllabus syllabus: syllabusList){
            SyllabusViewProgramResponse syllabusViewProgramResponse = new SyllabusViewProgramResponse();
            Optional<Customer> customerOptional = customerRepository.getCustomerById(syllabus.getCreatedBy());
            if(customerOptional.isPresent()){
                Customer customer = customerOptional.get();
                syllabusViewProgramResponse.setCreatedByName(customer.getFullName());
                modelMapper.map(syllabus, syllabusViewProgramResponse);
                syllabusViewProgramResponses.add(syllabusViewProgramResponse);
            }else{
                throw new BadRequestException("Customer id is not found.");
            }
        }

        return syllabusViewProgramResponses;
    }

    @Override
    public List<Syllabus> checkCondition(String name, String code, String version, SyllabusStatus status) {
        List<Syllabus> syllabusList = syllabusRepository.findByNameIgnoreCaseAndCodeIgnoreCaseAndVersionIgnoreCaseAndStatus(name,code,version,status);
        return syllabusList;
    }

    @Override
    public ResponseEntity<ResponseObject> getAllSyllabus(RequestForListOfSyllabus request) {
        LOGGER.info("Start method getAllSyllabus in SyllabusServiceImpl");
        List<ViewSyllabusResponse> responseData = new ArrayList<>();
        List<Syllabus> results = new ArrayList<>();
        int totalPage = 0;
        int totalRows = 0;
        String message = "";
        if("".equalsIgnoreCase(request.getStartDate()) && "".equalsIgnoreCase(request.getEndDate()) && request.getKeyword().length==0){
            if (request.getSortBy() == null && request.getSortType() == null) {
                LOGGER.info("Start View All Syllabus");
                Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
                Page<Syllabus> syllabusPage = syllabusRepository.findAllSyllabus(pageable);
                totalPage = syllabusPage.getTotalPages();
                results = syllabusPage.getContent();
            }else {
                LOGGER.info("View All Syllabus With Sort Options");
                results = syllabusJdbc.getSyllabus(syllabusServiceUtils
                        .getSQLForSortingAllSyllabus(request.getPage() - 1, request.getSize(),
                                request.getSortBy(), request.getSortType()));
                totalRows = syllabusRepository.getTotalRows();
            }
        }else{
            if("".equalsIgnoreCase(request.getStartDate()) && "".equalsIgnoreCase(request.getEndDate())){
                if (request.getSortBy() != null && request.getSortType() != null) {
                    LOGGER.info("View All Syllabus With Keywords");
                    Set<Syllabus> syllabusSet = new HashSet<>();
                    for(String tag: request.getKeyword()){
                        List<Syllabus> syllabus = syllabusJdbc.getSyllabus(syllabusServiceUtils
                                .getSQLForSearchingByKeywordsForSuggestions(request.getPage() - 1, request.getSize(),
                                        tag));
                        if(syllabus!=null){
                            syllabusSet.addAll(syllabus);
                        }
                    }
                    results = new ArrayList<>(syllabusSet);
                    totalRows = syllabusSet.size();
                }else {
                    LOGGER.info("View All Syllabus With Keywords and Sort Options");
                    Set<Syllabus> syllabusSet = new HashSet<>();
                    for(String tag: request.getKeyword()){
                        List<Syllabus> syllabus = syllabusJdbc.getSyllabus(syllabusServiceUtils
                                .getSQLForSearchingByKeywordsForSuggestionsAndSorting(request.getPage() - 1, request.getSize(),
                                        request.getSortBy(), request.getSortType(), tag));
                        if(syllabus!=null){
                            results.addAll(syllabus);
                        }
                    }
                    results = new ArrayList<>(syllabusSet);
                    totalRows = syllabusSet.size();
                }
            }else if(request.getKeyword().length==0){
                if(request.getSortBy() == null && request.getSortType() == null) {
                    LOGGER.info("View All Syllabus With start date and end date");
                    results = syllabusJdbc.getSyllabus(syllabusServiceUtils
                            .getSQLForSearchingByCreatedDateAndNotSort(request.getStartDate(), request.getEndDate(), request.getPage() - 1, request.getSize()));
                    totalRows = syllabusRepository.getTotalRows();
                }else{
                    LOGGER.info("View All Syllabus With start date and end date and Sort Options");
                    results = syllabusJdbc.getSyllabus(syllabusServiceUtils
                            .getSQLForSearchingByCreatedDateAndSort(request.getStartDate(), request.getEndDate(), request.getPage() - 1, request.getSize(),
                                    request.getSortBy(),request.getSortType()));
                    totalRows = syllabusRepository.getTotalRows();
                }
            }else{
                if (request.getSortBy() != null && request.getSortType() != null) {
                    LOGGER.info("View All Syllabus With Keywords and Start Date and End Date");
                    Set<Syllabus> syllabusSet = new HashSet<>();
                    for(String tag: request.getKeyword()){
                        List<Syllabus> syllabus = syllabusJdbc.getSyllabus(syllabusServiceUtils
                                .getSQLForSearchingByKeywordAndCreatedDateAndNotSort(tag, request.getStartDate(), request.getEndDate(), request.getPage() - 1, request.getSize()));
                        if(syllabus!=null){
                            syllabusSet.addAll(syllabus);
                        }
                    }
                    results = new ArrayList<>(syllabusSet);
                    totalRows = syllabusSet.size();
                }else {
                    LOGGER.info("View All Syllabus With Keywords and Start Date and End Date and Sort Options");
                    Set<Syllabus> syllabusSet = new HashSet<>();
                    for(String tag: request.getKeyword()){
                        List<Syllabus> syllabus = syllabusJdbc.getSyllabus(syllabusServiceUtils
                                .getSQLForSearchingByKeywordAndCreatedDateAndSort(tag, request.getStartDate(), request.getEndDate(), request.getPage() - 1, request.getSize(),
                                        request.getSortBy(), request.getSortType()));
                        if(syllabus!=null){
                            results.addAll(syllabus);
                        }
                    }
                    results = new ArrayList<>(syllabusSet);
                    totalRows = syllabusSet.size();
                }
            }
        }

        if (results.size() > 0) {
            if(totalPage == 0) {
                totalPage = (int)(totalRows % request.getSize() == 0 ? (totalRows / request.getSize()) : (totalRows / request.getSize()) + 1);
            }
            message += "Total " + results.size() + " element(s) in page " + request.getPage();
            responseData = results.stream()
                    .map(customer -> modelMapper.map(customer, ViewSyllabusResponse.class))
                    .toList();
        } else {
            LOGGER.info("Null result");
            message += "Empty list";
        }
        LOGGER.info("End method getAllSyllabus in SpringDataServiceImpl");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.toString(), message,
                new Pagination(request.getPage(), request.getSize(), totalPage), responseData));
    }

    private static Syllabus getSyllabus(SyllabusRequest dto, Optional<SyllabusLevel> syllabusLevelOptional, UUID id, Date date) {
        Syllabus syllabus = new Syllabus();
        syllabus.setName(dto.getName());
        syllabus.setCode(dto.getCode());
        syllabus.setVersion(dto.getVersion());
        syllabus.setAttendeeNumber(dto.getAttendeeNumber());
        syllabus.setTechnicalRequirement(dto.getTechnicalRequirement());
        syllabus.setCourseObjective(dto.getCourseObjective());
        syllabus.setStatus(dto.getStatus());
        syllabus.setTemplate(dto.isTemplate());
        syllabus.setCreatedBy(id);
        syllabus.setCreatedDate(date);
        syllabus.setUpdatedBy(id);
        syllabus.setUpdatedDate(date);
        if(syllabusLevelOptional==null){
            syllabus.setSyllabusLevel(null);
        }else{
            syllabus.setSyllabusLevel(syllabusLevelOptional.get());
        }
        return syllabus;
    }

    private static Syllabus getSyllabusImport(SyllabusRequest dto, UUID id, Date date) {
        Syllabus syllabus = new Syllabus();
        syllabus.setName(dto.getName());
        syllabus.setCode(dto.getCode());
        syllabus.setVersion(dto.getVersion());
        syllabus.setAttendeeNumber(dto.getAttendeeNumber());
        syllabus.setTechnicalRequirement(dto.getTechnicalRequirement());
        syllabus.setCourseObjective(dto.getCourseObjective());
        syllabus.setStatus(dto.getStatus());
        syllabus.setTemplate(dto.isTemplate());
        syllabus.setCreatedBy(id);
        syllabus.setCreatedDate(date);
        syllabus.setUpdatedBy(id);
        syllabus.setUpdatedDate(date);
        syllabus.setSyllabusLevel(null);
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

    private static DeliveryPrinciple getDeliveryPrinciple(SyllabusRequest dto, Syllabus savedSyllabus) {
        DeliveryPrinciple deliveryPrinciple = new DeliveryPrinciple();
        deliveryPrinciple.setTrainees(dto.getDeliveryPrinciple().getTrainees());
        deliveryPrinciple.setTrainer(dto.getDeliveryPrinciple().getTrainer());
        deliveryPrinciple.setTraining(dto.getDeliveryPrinciple().getTraining());
        deliveryPrinciple.setRe_test(dto.getDeliveryPrinciple().getRe_test());
        deliveryPrinciple.setMarking(dto.getDeliveryPrinciple().getMarking());
        deliveryPrinciple.setWaiverCriteria(dto.getDeliveryPrinciple().getWaiverCriteria());
        deliveryPrinciple.setOthers(dto.getDeliveryPrinciple().getOthers());
        deliveryPrinciple.setSyllabus(savedSyllabus);
        return deliveryPrinciple;
    }

    public static List<Integer> extractDayNumbers(String input) {
        List<Integer> dayNumbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("Day(\\d+):");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String dayNumberStr = matcher.group(1);
            int dayNumber = Integer.parseInt(dayNumberStr);
            dayNumbers.add(dayNumber);
        }

        return dayNumbers;
    }

    public static String getStringAfterUnderscore(String input) {
        int index = input.indexOf('_');
        if (index != -1) {
            return input.substring(index + 1);
        } else {
            return input;
        }
    }

    public static int getUnitNumber(String input) {
        Pattern pattern = Pattern.compile("Unit(\\d+)_");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String numberString = matcher.group(1);
            return Integer.parseInt(numberString);
        } else {
            return -1;
        }
    }

    public static List<String> splitByNewLine(String input) {
        List<String> result = new ArrayList<>();
        String[] parts = input.split("\n");

        for (String part : parts) {
            result.add(part.trim());
        }

        return result;
    }
}

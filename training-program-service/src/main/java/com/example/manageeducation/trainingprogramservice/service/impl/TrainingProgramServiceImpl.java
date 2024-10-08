package com.example.manageeducation.trainingprogramservice.service.impl;


import com.example.manageeducation.trainingprogramservice.client.CustomerClient;
import com.example.manageeducation.trainingprogramservice.client.SyllabusClient;
import com.example.manageeducation.trainingprogramservice.dto.*;
import com.example.manageeducation.trainingprogramservice.enums.SyllabusStatus;
import com.example.manageeducation.trainingprogramservice.enums.TrainingProgramStatus;
import com.example.manageeducation.trainingprogramservice.exception.BadRequestException;
import com.example.manageeducation.trainingprogramservice.jdbc.TrainingProgramJdbc;
import com.example.manageeducation.trainingprogramservice.model.*;
import com.example.manageeducation.trainingprogramservice.repository.ProgramSyllabusRepository;
import com.example.manageeducation.trainingprogramservice.repository.TrainingProgramRepository;
import com.example.manageeducation.trainingprogramservice.service.TrainingProgramService;
import com.example.manageeducation.trainingprogramservice.utils.SecurityUtil;
import com.example.manageeducation.trainingprogramservice.utils.TrainingProgramServiceUtils;
import org.apache.poi.ss.usermodel.Cell;
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

@Service
public class TrainingProgramServiceImpl implements TrainingProgramService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingProgramServiceImpl.class);

    @Autowired
    CustomerClient customerRepository;

    @Autowired
    SyllabusClient syllabusRepository;

    @Autowired
    TrainingProgramRepository trainingProgramRepository;

    @Autowired
    ProgramSyllabusRepository programSyllabusRepository;

    @Autowired
    TrainingProgramJdbc trainingProgramJdbc;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    TrainingProgramServiceUtils trainingProgramServiceUtils;

    @Override
    public String createTrainingProgram(Principal principal, TrainingProgramRequest dto) {
        LocalDate currentDate = LocalDate.now();
        Date date = java.sql.Date.valueOf(currentDate);
        Optional<Customer> customerOptional = customerRepository.getCustomerById(securityUtil.getLoginUser(principal).getId());
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();

            TrainingProgram trainingProgram = new TrainingProgram();
            trainingProgram.setCreatedBy(customer.getId());
            trainingProgram.setUpdatedBy(customer.getId());
            trainingProgram.setCreatedDate(date);
            trainingProgram.setUpdatedDate(date);
            trainingProgram.setStatus(TrainingProgramStatus.ACTIVE);
            trainingProgram.setTemplate(dto.isTemplate());
            modelMapper.map(dto,trainingProgram);
            TrainingProgram saved = trainingProgramRepository.save(trainingProgram);

            for(ProgramSyllabusRequest trainingProgramRequest:dto.getProgramSyllabuses()){
                ProgramSyllabus programSyllabus = new ProgramSyllabus();
                programSyllabus.setTrainingProgram(saved);

                //check validation Syllabus
                Optional<Syllabus> syllabusOptional = syllabusRepository.getSyllabus(trainingProgramRequest.getSyllabusId());
                if(syllabusOptional.isPresent()){
                    programSyllabus.setSyllabus(syllabusOptional.get().getId());
                }else{
                    throw new BadRequestException("Syllabus id is not found.");
                }
                programSyllabus.setPosition(trainingProgramRequest.getPosition());

                //Embeddable
                ProgramSyllabusId programSyllabusId = new ProgramSyllabusId();
                programSyllabusId.setTrainingProgramId(saved.getId());
                programSyllabusId.setSyllabusId(syllabusOptional.get().getId());

                programSyllabus.setId(programSyllabusId);

                programSyllabusRepository.save(programSyllabus);
            }
            return "create successful.";


        }else{
            throw new BadRequestException("Customer id is not found.");
        }
    }

    @Override
    public TrainingProgramResponse viewTrainingProgram(UUID id) {
        int days = 0;
        int hours = 0;
        TrainingProgramResponse trainingProgramResponse = new TrainingProgramResponse();

        Optional<TrainingProgram> trainingProgramOptional = trainingProgramRepository.findById(id);
        if(trainingProgramOptional.isPresent()){
            TrainingProgram trainingProgram = trainingProgramOptional.get();
            trainingProgramResponse.setId(trainingProgram.getId());
            trainingProgramResponse.setName(trainingProgram.getName());
            trainingProgramResponse.setStatus(trainingProgram.getStatus());
            trainingProgramResponse.setCreatedDate(trainingProgram.getCreatedDate());

            //get customer
            Optional<Customer> customerOptional = customerRepository.getCustomerById(trainingProgram.getCreatedBy());
            if(customerOptional.isPresent()){
                trainingProgramResponse.setCreatedBy(customerOptional.get().getFullName());
            }else{
                throw new BadRequestException("Customer id is not found.");
            }

            List<SyllabusResponse> responses = new ArrayList<>();
            for(ProgramSyllabus trainingProgram1: trainingProgramOptional.get().getProgramSyllabusAssociation()){
                responses.add(modelMapper.map(trainingProgram1.getSyllabus(), SyllabusResponse.class));
                Optional<Syllabus> syllabusOptional = syllabusRepository.getSyllabus(trainingProgram1.getSyllabus());
                if(syllabusOptional.isPresent()){
                    Syllabus syllabus = syllabusOptional.get();
                    for(SyllabusDay syllabusDay:syllabus.getSyllabusDays()){
                        days++;
                        for (SyllabusUnit syllabusUnit: syllabusDay.getSyllabusUnits()){
                            for(SyllabusUnitChapter syllabusUnitChapter: syllabusUnit.getSyllabusUnitChapters()){
                                hours += (int) syllabusUnitChapter.getDuration();
                            }
                        }
                    }
                }

            }

            trainingProgramResponse.setDay(days);
            trainingProgramResponse.setHours(hours);
            trainingProgramResponse.setSyllabuses(responses);

            return trainingProgramResponse;
        }else {
            throw new BadRequestException("Training program id is not found.");
        }
    }

    @Override
    public String deActiveTrainingProgram(UUID id) {
        Optional<TrainingProgram> trainingProgramOptional = trainingProgramRepository.findById(id);
        if(trainingProgramOptional.isPresent()){
            TrainingProgram trainingProgram = trainingProgramOptional.get();
            trainingProgram.setStatus(TrainingProgramStatus.INACTIVE);
            trainingProgramRepository.save(trainingProgram);
            return "In-active successful.";
        }else{
            throw new BadRequestException("Training program is not found.");
        }
    }

    @Override
    public String deleteTrainingProgram(UUID id) {
        Optional<TrainingProgram> trainingProgramOptional = trainingProgramRepository.findById(id);
        if(trainingProgramOptional.isPresent()){
            TrainingProgram trainingProgram = trainingProgramOptional.get();
            trainingProgram.setStatus(TrainingProgramStatus.DELETED);
            trainingProgramRepository.save(trainingProgram);
            return "In-active successful.";
        }else{
            throw new BadRequestException("Training program is not found.");
        }
    }

    @Override
    public TrainingProgram duplicatedTrainingProgram(Principal principal, UUID id) {
        LocalDate currentDate = LocalDate.now();
        Date date = java.sql.Date.valueOf(currentDate);

        Optional<TrainingProgram> trainingProgramOptional = trainingProgramRepository.findById(id);
        if(trainingProgramOptional.isPresent()){
            TrainingProgram trainingProgram = trainingProgramOptional.get();

            //duplicated
            TrainingProgram duplicatedTrainingProgram = new TrainingProgram();
            duplicatedTrainingProgram.setName(trainingProgram.getName());
            duplicatedTrainingProgram.setTemplate(trainingProgram.isTemplate());
            duplicatedTrainingProgram.setCreatedDate(date);


            Optional<Customer> customerOptional = customerRepository.getCustomerById(securityUtil.getLoginUser(principal).getId());
            if(customerOptional.isPresent()){
                duplicatedTrainingProgram.setCreatedBy(customerOptional.get().getId());
            }else {
                throw new BadRequestException("Customer id is not found.");
            }
            duplicatedTrainingProgram.setUpdatedDate(trainingProgram.getUpdatedDate());
            duplicatedTrainingProgram.setUpdatedBy(trainingProgram.getUpdatedBy());
            duplicatedTrainingProgram.setVersion(trainingProgram.getVersion());
            duplicatedTrainingProgram.setStatus(trainingProgram.getStatus());
            TrainingProgram saved = trainingProgramRepository.save(duplicatedTrainingProgram);

            //save program syllabus
            for(ProgramSyllabus programSyllabus:trainingProgram.getProgramSyllabusAssociation()){
                ProgramSyllabus programSyllabus1 = new ProgramSyllabus();

                ProgramSyllabusId programSyllabusId = new ProgramSyllabusId();
                programSyllabusId.setTrainingProgramId(saved.getId());
                programSyllabusId.setSyllabusId(programSyllabus.getSyllabus());
                programSyllabus1.setId(programSyllabusId);
                programSyllabus1.setTrainingProgram(saved);
                programSyllabus1.setSyllabus(programSyllabus.getSyllabus());
                programSyllabus1.setPosition(programSyllabus.getPosition());
                programSyllabusRepository.save(programSyllabus1);

            }
            return saved;
        }else{
            throw new BadRequestException("Training program is not found.");
        }
    }

    @Override
    public List<TrainingProgramsResponse> trainingPrograms() {
        List<TrainingProgramsResponse> trainingProgramsResponses = new ArrayList<>();
        List<TrainingProgram> trainingPrograms = trainingProgramRepository.findAllByStatus(TrainingProgramStatus.ACTIVE);
        for (TrainingProgram trainingProgram : trainingPrograms) {
            int days = 0;
            TrainingProgramsResponse trainingProgramsResponse = new TrainingProgramsResponse();

            //get customer
            Optional<Customer> customerOptional = customerRepository.getCustomerById(trainingProgram.getCreatedBy());
            if (customerOptional.isPresent()) {
                trainingProgramsResponse.setCreatedBy(customerOptional.get().getFullName());
            } else {
                throw new BadRequestException("Customer id is not found");
            }
            trainingProgramsResponse.setId(trainingProgram.getId());
            trainingProgramsResponse.setName(trainingProgram.getName());
            trainingProgramsResponse.setCreatedDate(trainingProgram.getCreatedDate());
            trainingProgramsResponse.setStatus(trainingProgram.getStatus());

            //get duration
            for (ProgramSyllabus syllabus : trainingProgram.getProgramSyllabusAssociation()) {
                Optional<Syllabus> syllabusOptional = syllabusRepository.getSyllabus(syllabus.getSyllabus());
                if(syllabusOptional.isPresent()){
                    Syllabus programSyllabus = syllabusOptional.get();
                    days = programSyllabus.getSyllabusDays().size();
                }
            }
            trainingProgramsResponse.setDay(days);
            trainingProgramsResponses.add(trainingProgramsResponse);
        }
        return trainingProgramsResponses;
    }

    @Override
    public String importTrainingProgram(MultipartFile file, Principal principal) {
        TrainingProgramImportRequest trainingProgram = new TrainingProgramImportRequest();
        List<SyllabusTNImportRequest> syllabuses = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            boolean rowIsEmpty = false;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell firstCell = row.getCell(0);

                if (firstCell != null) {
                    trainingProgram.setName(firstCell.toString());

                    boolean firstRowSkipped = false;

                    while (rowIterator.hasNext()) {
                        row = rowIterator.next();

                        //check ignore first row
                        if (!firstRowSkipped) {
                            firstRowSkipped = true;
                            continue;
                        }

                        //check null cell to stop while
                        for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                            String c = row.getCell(cellIndex).toString();
                            if (row.getCell(cellIndex).toString().equals("")) {
                                rowIsEmpty = true;
                                break;
                            }
                        }

                        // check is null
                        if (rowIsEmpty) {
                            break;
                        }

                        SyllabusTNImportRequest syllabus = new SyllabusTNImportRequest();
                        syllabus.setName(row.getCell(0).getStringCellValue());
                        syllabus.setCode(row.getCell(1).getStringCellValue());
                        syllabus.setVersion(String.valueOf(row.getCell(2).getNumericCellValue()));
                        syllabuses.add(syllabus);
                    }

                    trainingProgram.setSyllabuses(syllabuses);
                }
                if(rowIsEmpty){
                    break;
                }
            }
            return checkValidationDataTrainingProgram(principal,trainingProgram);
        } catch (IOException e) {
            throw new BadRequestException("Please fill in all information and use the correct excel file downloaded from the system.");
        }
    }

    @Override
    public List<TrainingProgramAddClassRequest> trainingProgramAddClass() {
        List<TrainingProgramAddClassRequest> trainingProgramAdds = new ArrayList<>();
        List<TrainingProgram> trainingPrograms = trainingProgramRepository.findAllWithoutTrainingClass();
        for(TrainingProgram trainingProgram: trainingPrograms){
            int hour = 0;
            int day = 0;
            TrainingProgramAddClassRequest trainingProgramAddClassRequest = new TrainingProgramAddClassRequest();
            trainingProgramAddClassRequest.setId(trainingProgram.getId());
            trainingProgramAddClassRequest.setName(trainingProgram.getName());
            trainingProgramAddClassRequest.setVersion(trainingProgram.getVersion());
            Optional<Customer> customerOptional = customerRepository.getCustomerById(trainingProgram.getCreatedBy());
            if(customerOptional.isPresent()){
                Customer customer = customerOptional.get();
                trainingProgramAddClassRequest.setCreatedBy(customer.getFullName());
            }else{
                trainingProgramAddClassRequest.setCreatedBy(null);
            }

            trainingProgramAddClassRequest.setCreatedDate(trainingProgram.getCreatedDate());
            for(ProgramSyllabus programSyllabus: trainingProgram.getProgramSyllabusAssociation()){
                Optional<Syllabus> syllabusOptional = syllabusRepository.getSyllabus(programSyllabus.getSyllabus());
                if(syllabusOptional.isPresent()){
                    Syllabus syllabus = syllabusOptional.get();
                    for(SyllabusDay syllabusDay: syllabus.getSyllabusDays()){
                        day++;
                        for(SyllabusUnit syllabusUnit: syllabusDay.getSyllabusUnits()){
                            for(SyllabusUnitChapter syllabusUnitChapter: syllabusUnit.getSyllabusUnitChapters()){
                                hour += (int) syllabusUnitChapter.getDuration();
                            }
                        }
                    }
                }
            }
            trainingProgramAddClassRequest.setHours(hour);
            trainingProgramAddClassRequest.setDays(day);
            trainingProgramAdds.add(trainingProgramAddClassRequest);
        }
        return trainingProgramAdds;
    }

    @Override
    public TrainingProgram trainingProgramName(String name, String version) {
        return trainingProgramRepository.findByName(name,version);
    }

    @Override
    public TrainingProgram trainingProgramId(UUID id) {
        Optional<TrainingProgram> trainingProgramOptional = trainingProgramRepository.findById(id);
        if(trainingProgramOptional.isPresent()){
            return trainingProgramOptional.get();
        }else {
            throw new BadRequestException("training program id not found.");
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getAllTrainingPrograms(RequestForListOfTrainingProgram request) {
        LOGGER.info("Start method getAllTrainingPrograms in TrainingProgramServiceImpl");
        List<TrainingProgramsResponse> responseData = new ArrayList<>();
        List<TrainingProgram> results = new ArrayList<>();
        int totalPage = 0;
        int totalRows = 0;
        String message = "";
        if("".equalsIgnoreCase(request.getStartDate()) && "".equalsIgnoreCase(request.getEndDate()) && request.getKeyword().length==0){
            if (request.getSortBy() == null && request.getSortType() == null) {
                if(request.getStatus()==null){
                    LOGGER.info("Start View All TrainingProgram");
                    Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
                    Page<TrainingProgram> trainingProgramPage = trainingProgramRepository.findAllTrainingProgram(pageable);
                    totalPage = trainingProgramPage.getTotalPages();
                    results = trainingProgramPage.getContent();
                }else{
                    LOGGER.info("Start View All TrainingProgram with status");
                    Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
                    Page<TrainingProgram> trainingProgramPage = trainingProgramRepository.findAllTrainingProgramWithStatus(pageable,request.getStatus());
                    totalPage = trainingProgramPage.getTotalPages();
                    results = trainingProgramPage.getContent();
                }
            }else {
                LOGGER.info("View All TrainingProgram With Sort Options");
                results = trainingProgramJdbc.getTrainingPrograms(trainingProgramServiceUtils
                        .getSQLForSortingAllTrainingPrograms(request.getPage() - 1, request.getSize(),
                                request.getSortBy(), request.getSortType()));
                totalRows = trainingProgramRepository.getTotalRows();
            }
        }else{
            if("".equalsIgnoreCase(request.getStartDate()) && "".equalsIgnoreCase(request.getEndDate())){
                if (request.getSortBy() != null && request.getSortType() != null) {
                    LOGGER.info("View All TrainingProgram With Keywords");
                    Set<TrainingProgram> trainingProgramSet = new HashSet<>();
                    for(String tag: request.getKeyword()){
                        List<TrainingProgram> syllabus = trainingProgramJdbc.getTrainingPrograms(trainingProgramServiceUtils
                                .getSQLForSearchingByKeywordsForSuggestions(tag, request.getStatus(), request.getPage() - 1, request.getSize()));
                        if(syllabus!=null){
                            trainingProgramSet.addAll(syllabus);
                        }
                    }
                    results = new ArrayList<>(trainingProgramSet);
                    totalRows = trainingProgramSet.size();
                }else {
                    LOGGER.info("View All TrainingProgram With Keywords and Sort Options");
                    Set<TrainingProgram> trainingProgramSet = new HashSet<>();
                    for(String tag: request.getKeyword()){
                        List<TrainingProgram> syllabus = trainingProgramJdbc.getTrainingPrograms(trainingProgramServiceUtils
                                .getSQLForSearchingByKeywordsForSuggestionsAndSorting(tag, request.getStatus(), request.getPage() - 1, request.getSize(),
                                        request.getSortBy(), request.getSortType()));
                        if(syllabus!=null){
                            results.addAll(syllabus);
                        }
                    }
                    results = new ArrayList<>(trainingProgramSet);
                    totalRows = trainingProgramSet.size();
                }
            }else if(request.getKeyword().length==0){
                if(request.getSortBy() == null && request.getSortType() == null) {
                    LOGGER.info("View All TrainingProgram With start date and end date");
                    results = trainingProgramJdbc.getTrainingPrograms(trainingProgramServiceUtils
                            .getSQLForSearchingByCreatedDateAndNotSort(request.getStartDate(), request.getEndDate(), request.getStatus(), request.getPage() - 1, request.getSize()));
                    totalRows = trainingProgramRepository.getTotalRows();
                }else{
                    LOGGER.info("View All TrainingProgram With start date and end date and Sort Options");
                    results = trainingProgramJdbc.getTrainingPrograms(trainingProgramServiceUtils
                            .getSQLForSearchingByCreatedDateAndSort(request.getStartDate(), request.getEndDate(), request.getStatus(), request.getPage() - 1, request.getSize(),
                                    request.getSortBy(),request.getSortType()));
                    totalRows = trainingProgramRepository.getTotalRows();
                }
            }else{
                if (request.getSortBy() != null && request.getSortType() != null) {
                    LOGGER.info("View All TrainingProgram With Keywords and Start Date and End Date");
                    Set<TrainingProgram> trainingProgramSet = new HashSet<>();
                    for(String tag: request.getKeyword()){
                        List<TrainingProgram> syllabus = trainingProgramJdbc.getTrainingPrograms(trainingProgramServiceUtils
                                .getSQLForSearchingByKeywordAndCreatedDateAndNotSort(tag, request.getStartDate(), request.getEndDate(), request.getStatus(), request.getPage() - 1, request.getSize()));
                        if(syllabus!=null){
                            trainingProgramSet.addAll(syllabus);
                        }
                    }
                    results = new ArrayList<>(trainingProgramSet);
                    totalRows = trainingProgramSet.size();
                }else {
                    LOGGER.info("View All TrainingProgram With Keywords and Start Date and End Date and Sort Options");
                    Set<TrainingProgram> trainingProgramSet = new HashSet<>();
                    for(String tag: request.getKeyword()){
                        List<TrainingProgram> syllabus = trainingProgramJdbc.getTrainingPrograms(trainingProgramServiceUtils
                                .getSQLForSearchingByKeywordAndCreatedDateAndSort(tag, request.getStartDate(), request.getEndDate(), request.getStatus(), request.getPage() - 1, request.getSize(),
                                        request.getSortBy(), request.getSortType()));
                        if(syllabus!=null){
                            results.addAll(syllabus);
                        }
                    }
                    results = new ArrayList<>(trainingProgramSet);
                    totalRows = trainingProgramSet.size();
                }
            }
        }

        if (results.size() > 0) {
            if(totalPage == 0) {
                totalPage = (int)(totalRows % request.getSize() == 0 ? (totalRows / request.getSize()) : (totalRows / request.getSize()) + 1);
            }
            message += "Total " + results.size() + " element(s) in page " + request.getPage();
            responseData = results.stream()
                    .map(customer -> modelMapper.map(customer, TrainingProgramsResponse.class))
                    .toList();
        } else {
            LOGGER.info("Null result");
            message += "Empty list";
        }
        LOGGER.info("End method getAllTrainingProgram in TrainingProgramServiceImpl");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.toString(), message,
                new Pagination(request.getPage(), request.getSize(), totalPage), responseData));
    }

    private String checkValidationDataTrainingProgram(Principal principal, TrainingProgramImportRequest dto){
        try{
            TrainingProgramRequest request = new TrainingProgramRequest();
            request.setName(dto.getName());
            request.setVersion("1.0");
            request.setTemplate(true);
            request.setStatus(TrainingProgramStatus.ACTIVE);
            List<ProgramSyllabusRequest> programSyllabuses = new ArrayList<>();
            for(SyllabusTNImportRequest importRequest: dto.getSyllabuses()){
                List<Syllabus> syllabuses = syllabusRepository.checkCondition(importRequest.getName(),importRequest.getCode(), importRequest.getVersion(), SyllabusStatus.ACTIVE);
                if(syllabuses!=null){
                    ProgramSyllabusRequest programSyllabusRequest = new ProgramSyllabusRequest();
                    for(Syllabus syllabus:syllabuses){
                        programSyllabusRequest.setSyllabusId(syllabus.getId());
                        programSyllabusRequest.setPosition(1);
                        programSyllabuses.add(programSyllabusRequest);
                        break;
                    }

                }else{
                    throw new BadRequestException("Syllabus name: " + importRequest.getName() + " is not exist in system.");
                }

            }
            request.setProgramSyllabuses(programSyllabuses);
            if(request.getProgramSyllabuses()!=null){
                createTrainingProgram(principal,request);
                return "Create successful.";
            }else{
                return "Create fail.";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Create fail.";
        }

    }
}

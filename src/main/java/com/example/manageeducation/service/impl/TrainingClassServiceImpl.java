package com.example.manageeducation.service.impl;

import com.example.manageeducation.Utils.DataUtil;
import com.example.manageeducation.Utils.DateTimeUtils;
import com.example.manageeducation.Utils.SecurityUtil;
import com.example.manageeducation.dto.request.ClassCalendarRequest;
import com.example.manageeducation.dto.request.CustomerRequest;
import com.example.manageeducation.dto.request.DataExcelForTrainingClass;
import com.example.manageeducation.dto.request.TrainingClassRequest;
import com.example.manageeducation.dto.response.TrainingClassViewResponse;
import com.example.manageeducation.dto.response.TrainingClassesResponse;
import com.example.manageeducation.dto.response.TrainingProgramResponse;
import com.example.manageeducation.entity.*;
import com.example.manageeducation.enums.TrainingClassStatus;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.*;
import com.example.manageeducation.service.TrainingClassService;
import com.example.manageeducation.service.TrainingProgramService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TrainingClassServiceImpl implements TrainingClassService {

    @Autowired
    TrainingClassRepository trainingClassRepository;

    @Autowired
    ClassLocationRepository classLocationRepository;

    @Autowired
    AttendLevelRepository attendLevelRepository;

    @Autowired
    FormatTypeRepository formatTypeRepository;

    @Autowired
    ClassStatusRepository classStatusRepository;

    @Autowired
    TechnicalGroupRepository technicalGroupRepository;

    @Autowired
    ClassCalendarRepository classCalendarRepository;

    @Autowired
    ProgramContentRepository programContentRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TrainingProgramRepository trainingProgramRepository;

    @Autowired
    FsuRepository fsuRepository;

    @Autowired
    ContactPointRepository contactPointRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    TrainingProgramService trainingProgramService;

    @Override
    public String createTrainingClass(Principal principal, UUID trainingProgramId, TrainingClassRequest dto) {
        LocalDateTime currentDate = LocalDateTime.now();
        //check validation training program
        Optional<TrainingProgram> trainingProgramOptional = trainingProgramRepository.findById(trainingProgramId);
        if(trainingProgramOptional.isEmpty()){
            throw new BadRequestException("Training program id is not found.");
        }

        //check validation
        Optional<Customer> customerOptional = customerRepository.findById(securityUtil.getLoginUser(principal).getId());
        if(customerOptional.isEmpty()){
            throw new BadRequestException("Customer id is not found.");
        }

        TrainingProgram trainingProgram = trainingProgramOptional.get();
        Customer customer = customerOptional.get();

        TrainingClass trainingClass = new TrainingClass();
        trainingClass.setTrainingProgram(trainingProgram);
        trainingClass.setCreatedBy(customer);
        trainingClass.setUpdatedBy(customer);
        trainingClass.setCreatedDate(currentDate);
        trainingClass.setUpdatedDate(currentDate);
        trainingClass.setTrainingProgram(trainingProgram);
        trainingClass.setName(dto.getName());
        trainingClass.setCourseCode(dto.getCourseCode());
        trainingClass.setStartDate(dto.getStartDate());
        trainingClass.setEndDate(dto.getEndDate());
        trainingClass.setStartTime(dto.getStartTime());
        trainingClass.setEndTime(dto.getEndTime());
        trainingClass.setDuration(dto.getDuration());
        trainingClass.setReviewedDate(dto.getReviewedDate());
        trainingClass.setApprovedDate(dto.getApprovedDate());
        trainingClass.setUniversityCode(dto.getUniversityCode());
        trainingClass.setPlannedAttendee(dto.getPlannedAttendee());
        trainingClass.setAcceptedAttendee(dto.getAcceptedAttendee());
        trainingClass.setActualAttendee(dto.getActualAttendee());
        trainingClass.setStatus(TrainingClassStatus.ACTIVE);

        //check approve validation
        Optional<Customer> customerApOptional = customerRepository.findById(dto.getApprovedBy());
        if(customerApOptional.isEmpty()){
            throw new BadRequestException("Customer id is not found.");
        }
        trainingClass.setApprovedBy(customerApOptional.get());

        //check accept validation
        Optional<Customer> customerReOptional = customerRepository.findById(dto.getReviewedBy());
        if(customerReOptional.isEmpty()){
            throw new BadRequestException("Customer id is not found.");
        }
        trainingClass.setReviewedBy(customerReOptional.get());

        //set Admin
        Set<Customer> admins = new HashSet<>();
        for(CustomerRequest customer1: dto.getAccount_admins()){
            Optional<Customer> AdminOptional = customerRepository.findById(customer1.getId());
            if(AdminOptional.isPresent()){
                admins.add(AdminOptional.get());
            }else {
                throw new BadRequestException("Admin id is not found.");
            }
        }
        trainingClass.setAccount_admins(admins);

        //set trainee
        List<Customer> trainee = new ArrayList<>();
        for(CustomerRequest customer1: dto.getAccount_trainee()){
            Optional<Customer> TraineeOptional = customerRepository.findById(customer1.getId());
            if(TraineeOptional.isPresent()){
                trainee.add(TraineeOptional.get());
            }else {
                throw new BadRequestException("Admin id is not found.");
            }
        }
        trainingClass.setAccount_trainee(trainee);

        //set trainer
        Set<Customer> trainer = new HashSet<>();
        for(CustomerRequest customer1: dto.getAccount_trainers()){
            Optional<Customer> TrainerOptional = customerRepository.findById(customer1.getId());
            if(TrainerOptional.isPresent()){
                trainer.add(TrainerOptional.get());
            }else {
                throw new BadRequestException("Admin id is not found.");
            }
        }
        trainingClass.setAccount_trainers(trainer);

        //set class location
        Optional<ClassLocation> classLocationOptional = classLocationRepository.findById(dto.getClassLocation().getId());
        if(classLocationOptional.isPresent()){
            ClassLocation classLocation = classLocationOptional.get();
            trainingClass.setClassLocation(classLocation);
        }else {
            throw new BadRequestException("Class location id is not found.");
        }

        //set attendee level
        Optional<AttendLevel> attendLevelOptional = attendLevelRepository.findById(dto.getAttendeeLevel().getId());
        if(attendLevelOptional.isPresent()){
            AttendLevel attendLevel = attendLevelOptional.get();
            trainingClass.setAttendeeLevel(attendLevel);
        }else {
            throw new BadRequestException("Attend level is not found.");
        }

        //set format type
        Optional<FormatType> formatTypeOptional = formatTypeRepository.findById(dto.getFormatType().getId());
        if(formatTypeOptional.isPresent()){
            FormatType formatType = formatTypeOptional.get();
            trainingClass.setFormatType(formatType);
        }else {
            throw new BadRequestException("Format type is not found.");
        }

        //set class status
        Optional<ClassStatus> classStatusOptional = classStatusRepository.findById(dto.getClassStatus().getId());
        if(classStatusOptional.isPresent()){
            ClassStatus classStatus = classStatusOptional.get();
            trainingClass.setClassStatus(classStatus);
        }else {
            throw new BadRequestException("Class status is not found.");
        }

        //set technical group
        Optional<TechnicalGroup> technicalGroupOptional = technicalGroupRepository.findById(dto.getTechnicalGroup().getId());
        if(technicalGroupOptional.isPresent()){
            TechnicalGroup technicalGroup = technicalGroupOptional.get();
            trainingClass.setTechnicalGroup(technicalGroup);
        }else {
            throw new BadRequestException("Technical group is not found.");
        }

        //set program content
        Optional<ProgramContent> programContentOptional = programContentRepository.findById(dto.getProgramContent().getId());
        if(programContentOptional.isPresent()){
            ProgramContent programContent = programContentOptional.get();
            trainingClass.setProgramContent(programContent);
        }else {
            throw new BadRequestException("Program content is not found.");
        }

        //setFsu
        Optional<Fsu> fsuOptional = fsuRepository.findById(dto.getFsu().getId());
        if(fsuOptional.isPresent()){
            Fsu fsu = fsuOptional.get();
            trainingClass.setFsu(fsu);
        }else {
            throw new BadRequestException("Fsu id is not found.");
        }
        modelMapper.map(dto,trainingClass);
        TrainingClass savedTrainingClass = trainingClassRepository.save(trainingClass);

        //set class calendar
        for(ClassCalendarRequest calendar: dto.getClassCalendars()){
            ClassCalendar classCalendar = new ClassCalendar();
            classCalendar.setDay_no(calendar.getDay_no());
            classCalendar.setDateTime(calendar.getDateTime());
            classCalendar.setBeginTime(calendar.getBeginTime());
            classCalendar.setEndTime(calendar.getEndTime());
            classCalendar.setTrainingClass(savedTrainingClass);
            classCalendarRepository.save(classCalendar);
        }

        return "create successful.";
    }

    @Override
    public List<TrainingClassesResponse> TrainingClassesResponses() {
        List<TrainingClassesResponse> trainingClassesResponses = new ArrayList<>();
        List<TrainingClass> trainingClasses = trainingClassRepository.findAllByStatus(TrainingClassStatus.ACTIVE);
        for(TrainingClass trainingClass:trainingClasses){
            TrainingClassesResponse trainingClassesResponse = new TrainingClassesResponse();
            trainingClassesResponse.setId(trainingClass.getId());
            trainingClassesResponse.setName(trainingClass.getName());
            trainingClassesResponse.setCode(trainingClass.getCourseCode());
            trainingClassesResponse.setCreatedDate(trainingClass.getCreatedDate());
            trainingClassesResponse.setDuration(trainingClass.getDuration());
            trainingClassesResponse.setFsu(trainingClass.getFsu().getName());
            trainingClassesResponse.setLocation(trainingClass.getClassLocation().getName());
            trainingClassesResponse.setAttend(trainingClass.getAttendeeLevel().getName());
            trainingClassesResponse.setCreatedBy(trainingClass.getCreatedBy().getFullName());
            trainingClassesResponses.add(trainingClassesResponse);

        }
        return trainingClassesResponses;
    }

    @Override
    public String deleteTrainingClass(UUID id) {
        Optional<TrainingClass> trainingClassOptional = trainingClassRepository.findById(id);
        if(trainingClassOptional.isPresent()){
            TrainingClass trainingClass = trainingClassOptional.get();
            trainingClass.setStatus(TrainingClassStatus.DELETED);
            trainingClassRepository.save(trainingClass);
            return "Delete successful.";
        }else {
            throw new BadRequestException("Training class id is not found.");
        }
    }

    @Override
    public String duplicated(Principal principal, UUID id) {
        Optional<TrainingClass> trainingClassOptional = trainingClassRepository.findById(id);
        if(trainingClassOptional.isPresent()){
            TrainingClass trainingClass = trainingClassOptional.get();
            TrainingProgram trainingProgram = trainingProgramService.duplicatedTrainingProgram(principal,trainingClass.getTrainingProgram().getId());

            TrainingClass newTrainingClass = new TrainingClass();
            modelMapper.map(trainingClass,newTrainingClass);
            newTrainingClass.setId(null);
            newTrainingClass.setTrainingProgram(trainingProgram);
            UUID uuid = UUID.randomUUID();
            newTrainingClass.setCourseCode(trainingClass.getCourseCode() + uuid);
            trainingClassRepository.save(newTrainingClass);
            return "Training class duplicated successful.";
        }else{
            throw new BadRequestException("Training program id is not found.");
        }
    }

    @Override
    public TrainingClassViewResponse viewTrainingClass(UUID id) {
        Optional<TrainingClass> trainingClassOptional = trainingClassRepository.findById(id);
        if(trainingClassOptional.isPresent()){
            TrainingClass trainingClass = trainingClassOptional.get();

            //init new
            TrainingClassViewResponse trainingClassViewResponse = new TrainingClassViewResponse();
            TrainingProgramResponse trainingProgramResponse = trainingProgramService.viewTrainingProgram(trainingClass.getTrainingProgram().getId());
            trainingClassViewResponse.setTrainingProgram(trainingProgramResponse);
            modelMapper.map(trainingClass,trainingClassViewResponse);
            return trainingClassViewResponse;
        }else {
            throw new BadRequestException("Training class id is not found.");
        }
    }

    @Override
    public List<DataExcelForTrainingClass> readDataFromExcel(Principal principal, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (!fileName.contains(".xlsx") || !fileName.contains(".xls"))
            throw new RuntimeException("file you have requested for reading must be in .xlsx or .xls");
        try {
            Workbook workbook = null;
            if (file instanceof MultipartFile) {
                byte[] b = file.getBytes();
                InputStream inputStream = new ByteArrayInputStream(b);
                if (fileName.contains(".xlsx"))
                    workbook = new XSSFWorkbook(inputStream);
                else if (fileName.contains(".xls"))
                    workbook = new HSSFWorkbook(inputStream);

            }

            Sheet sheet = workbook.getSheetAt(1);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            List<DataExcelForTrainingClass> list = new ArrayList<>();
            DataFormatter formatter = new DataFormatter();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            DateTimeFormatter formatLocalDate = DateTimeFormatter.ofPattern("d/M/yyyy");
            DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("H:m");


            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                // DataExcelForTrainingClass trainingClassDTO = mapToDTO(newTraningClass);
                Row row = sheet.getRow(i);
                // dataExcelForTrainingClass=new DataExcelForTrainingClass();
                DataExcelForTrainingClass dataExcelForTrainingClass = new DataExcelForTrainingClass();

                if (row != null) {
                    UUID id = UUID.randomUUID();
                    dataExcelForTrainingClass.setId(id);
                    Cell c0 = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c1 = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c2 = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c3 = row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c4 = row.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c5 = row.getCell(5, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c6 = row.getCell(6, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c7 = row.getCell(7, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c8 = row.getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c9 = row.getCell(9, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c10 = row.getCell(10, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c11 = row.getCell(11, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c12 = row.getCell(12, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c13 = row.getCell(13, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c14 = row.getCell(14, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c15 = row.getCell(15, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c16 = row.getCell(16, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c17 = row.getCell(17, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c18 = row.getCell(18, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c19 = row.getCell(19, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c20 = row.getCell(20, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c21 = row.getCell(21, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c22 = row.getCell(22, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c23 = row.getCell(23, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c24 = row.getCell(24, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c25 = row.getCell(25, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c26 = row.getCell(26, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c27 = row.getCell(27, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c28 = row.getCell(28, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c29 = row.getCell(29, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c30 = row.getCell(30, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                    if (c0 != null) {
                        String sttValue = formatter.formatCellValue(c0, evaluator).trim();
                        int stt = Integer.parseInt(sttValue);
                        dataExcelForTrainingClass.setStt(stt);

                    }
                    if (c1 != null) {
                        String site = formatter.formatCellValue(c1, evaluator).trim();
                        dataExcelForTrainingClass.setLocationId(site);

                    }
                    if (c2 != null) {
                        String noClassValue = formatter.formatCellValue(c2, evaluator).trim();
                        int noClass = Integer.parseInt(noClassValue);
                        dataExcelForTrainingClass.setNoClass(noClass);
                    }
                    if (c3 != null) {
                        String courseCode = formatter.formatCellValue(c3, evaluator).trim();
                        dataExcelForTrainingClass.setCourseCode(courseCode);

                    }
                    if (c4 != null) {
                        String status = formatter.formatCellValue(c4, evaluator).trim();
                        dataExcelForTrainingClass.setStatus(status);
                    }
                    if (c5 != null) {
                        String attendeeType = formatter.formatCellValue(c5, evaluator).trim();
                        dataExcelForTrainingClass.setAttendeeType(attendeeType);
                    }
                    if (c6 != null) {
                        String formatType = formatter.formatCellValue(c6, evaluator).trim();
                        dataExcelForTrainingClass.setFormatType(formatType);
                    }
                    if (c7 != null) {
                        String fsu = formatter.formatCellValue(c7, evaluator).trim();
                        dataExcelForTrainingClass.setFsu(fsu);
                    }
                    if (c8 != null) {
                        String universityCode = formatter.formatCellValue(c8, evaluator).trim();
                        dataExcelForTrainingClass.setUniversityCode(universityCode);
                    }
                    if (c9 != null) {
                        String technicalGroup = formatter.formatCellValue(c9, evaluator).trim();
                        dataExcelForTrainingClass.setTechnicalGroup(technicalGroup);
                    }
                    if (c10 != null) {
                        String trainingProgram = formatter.formatCellValue(c10, evaluator).trim();
                        dataExcelForTrainingClass.setTrainingProgram(trainingProgram);
                    }
                    if (c11 != null) {
                        String trainingProgramVersion = formatter.formatCellValue(c11, evaluator).trim();
                        dataExcelForTrainingClass.setTrainingProgramVersion(trainingProgramVersion);
                    }
                    if (c12 != null) {
                        String programContentId = formatter.formatCellValue(c12, evaluator).trim();
                        dataExcelForTrainingClass.setProgramContentId(programContentId);
                    }
                    if (c13 != null) {
                        String recer = formatter.formatCellValue(c13, evaluator).trim();
                        dataExcelForTrainingClass.setRecer(recer);
                    }
                    if (c14 != null) {
                        String traineeNoValue = formatter.formatCellValue(c14, evaluator).trim();
                        int traineeNo = Integer.parseInt(traineeNoValue);
                        dataExcelForTrainingClass.setTraineeNO(traineeNo);
                    }
                    if (c15 != null) {
                        String planStartDateString = formatter.formatCellValue(c15, evaluator).trim();
                        LocalDate planStartDate=LocalDate.parse(planStartDateString,formatLocalDate);
                        dataExcelForTrainingClass.setStartDate(planStartDate);

                    }
                    if (c15 != null & c16 != null & c17 != null) {
                        if (c16.getCellTypeEnum() == CellType.FORMULA) {
                            String planStartDateString = formatter.formatCellValue(c15, evaluator).trim();
                            LocalDate planStartDate=LocalDate.parse(planStartDateString,formatLocalDate);
                            String durationValue = formatter.formatCellValue(c17, evaluator).trim();
                            int duration = Integer.parseInt(durationValue);
                            LocalDate planEndDate=planStartDate.plusMonths(duration).plusDays(-1);
                            dataExcelForTrainingClass.setEndDate(planEndDate);
                        } else {
                            String planEndDateValue = formatter.formatCellValue(c15, evaluator).trim();
                            LocalDate planEndDate= DateTimeUtils.convertStringToLocalDate(planEndDateValue);
                            dataExcelForTrainingClass.setEndDate(planEndDate);

                        }

                    }
                    if (c17 != null) {
                        String durationValue = formatter.formatCellValue(c17, evaluator).trim();
                        int duration = Integer.parseInt(durationValue);
                        dataExcelForTrainingClass.setDuration(duration);
                    }
                    if (c18 != null) {
                        String trainer = formatter.formatCellValue(c18, evaluator).trim();
                        Set<String> listTrainer = DataUtil.splitString(trainer);
                        System.out.println(listTrainer.size());
                        dataExcelForTrainingClass.setTrainer(listTrainer);
                    }
                    if (c19 != null) {
                        String mentor = formatter.formatCellValue(c19, evaluator).trim();
                        dataExcelForTrainingClass.setMentor(mentor);
                    }
                    if (c20 != null) {
                        String classAdmin = formatter.formatCellValue(c20, evaluator).trim();
                        Set<String> listClassAdmin = DataUtil.splitString(classAdmin);
                        dataExcelForTrainingClass.setClassAdmin(listClassAdmin);
                    }
                    if (c21 != null) {
                        String location = formatter.formatCellValue(c21, evaluator).trim();
                        dataExcelForTrainingClass.setLocationUnit(location);
                    }
                    if (c22 != null) {
                        String updatedDateValue = formatter.formatCellValue(c22, evaluator).trim();
//                        Date updatedDate = df.parse(updatedDateValue);
                        //dataExcelForTrainingClass.setUpdatedDate(updatedDate);
                    }
                    if (c23 != null) {
                        String updatedBy = formatter.formatCellValue(c23, evaluator).trim();
                        dataExcelForTrainingClass.setUpdatedBy(updatedBy);
                    }
                    if (c24 != null) {
                        String formatType_Abb = formatter.formatCellValue(c24, evaluator).trim();
                        dataExcelForTrainingClass.setFormatType_Abb(formatType_Abb);
                    }
                    if (c25 != null) {
                        String classNo_AbbValue = formatter.formatCellValue(c25, evaluator).trim();
                        int classNo_Abb = Integer.parseInt(classNo_AbbValue);
                        dataExcelForTrainingClass.setClassNo_Abb(classNo_Abb);
                    }
                    if (c26 != null) {
                        String universityCode_Abb = formatter.formatCellValue(c26, evaluator).trim();
                        dataExcelForTrainingClass.setUniversityCode_Abb(universityCode_Abb);
                    }
                    if (c27 != null) {
                        String startYearValue = formatter.formatCellValue(c27, evaluator).trim();
                        int startYear = Integer.parseInt(startYearValue);
                        dataExcelForTrainingClass.setStartYear(startYear);
                    }
                    if (c28 != null) {
                        String startTimeString = formatter.formatCellValue(c28, evaluator).trim();
                        try {
                            LocalTime startTime=LocalTime.parse(startTimeString,formatTime);

                            dataExcelForTrainingClass.setStartTime(startTime);
                        } catch (Exception ex) {

                        }
                    }
                    if (c29 != null) {
                        String endTimeString = formatter.formatCellValue(c29, evaluator).trim();
                        try {
                            LocalTime endTime=LocalTime.parse(endTimeString,formatTime);
                            dataExcelForTrainingClass.setEndTime(endTime);
                        } catch (Exception ex) {

                        }
                    }
                    if(c30 != null) {
                        String plannedAttendee = formatter.formatCellValue(c30, evaluator).trim();
                        int plannedAtt = Integer.parseInt(plannedAttendee);
                        dataExcelForTrainingClass.setPlannedAttendee(plannedAtt);
                    }
                    System.out.println("Tới dòng đây rồi");
                    saveTrainingClass(principal,dataExcelForTrainingClass);
                    System.out.println("save success");
                    list.add(dataExcelForTrainingClass);
                }
            }
            return list;
        } catch (

                Exception ex) {

        }
        return null;
    }

    public void saveTrainingClass(Principal principal, DataExcelForTrainingClass dataExcelForTrainingClass) {

        TrainingProgram trainingProgram = findTrainingProgramByNameAndVersion(dataExcelForTrainingClass.getTrainingProgram(),dataExcelForTrainingClass.getTrainingProgramVersion());
        TrainingClass trainingProgramInTrainingClass = findClassByTrainingProgramId(trainingProgram);

        if (trainingClassRepository.findByCourseCode(dataExcelForTrainingClass.getCourseCode()) == null) {
            if (trainingProgram != null) {
                if (trainingProgramInTrainingClass == null) {
                    System.out.println("Bat dau insert");
                    TrainingClass trainingClass = new TrainingClass();
                    Optional<Customer> customerOptional = customerRepository.findById(securityUtil.getLoginUser(principal).getId());
                    if(customerOptional.isPresent()){
                        trainingClass.setCreatedBy(customerRepository.findCustomerByEmail(customerOptional.get().getEmail()));
                    }else{
                        throw new BadRequestException("Customer id is not found.");
                    }
                    trainingClass.setId(dataExcelForTrainingClass.getId());
                    trainingClass.setCourseCode(dataExcelForTrainingClass.getCourseCode());
                    trainingClass.setStartTime(dataExcelForTrainingClass.getStartTime());
                    trainingClass.setEndTime(dataExcelForTrainingClass.getEndTime());
                    trainingClass.setStartDate(dataExcelForTrainingClass.getStartDate());
                    trainingClass.setEndDate(dataExcelForTrainingClass.getEndDate());
                    trainingClass.setDuration(dataExcelForTrainingClass.getDuration());
                    trainingClass.setUpdatedBy(customerRepository.findCustomerByEmail(dataExcelForTrainingClass.getUpdatedBy()));
                    trainingClass.setUpdatedDate(dataExcelForTrainingClass.getUpdatedDate());
                    trainingClass.setUniversityCode(dataExcelForTrainingClass.getUniversityCode());
                    trainingClass.setClassLocation(classLocationRepository.findByName(dataExcelForTrainingClass.getLocationId()));
                    trainingClass.setAttendeeLevel(attendLevelRepository.findByName(dataExcelForTrainingClass.getAttendeeType()));
                    trainingClass.setFormatType(formatTypeRepository.findByName(dataExcelForTrainingClass.getFormatType()));
                    trainingClass.setClassStatus(classStatusRepository.findByName(dataExcelForTrainingClass.getStatus().trim()));
                    trainingClass.setTechnicalGroup(technicalGroupRepository.findByName(dataExcelForTrainingClass.getTechnicalGroup()));
                    trainingClass.setProgramContent(programContentRepository.findByName(dataExcelForTrainingClass.getProgramContentId()));
                    trainingClass.setFsu(fsuRepository.findByName(dataExcelForTrainingClass.getFsu()));
                    trainingClass.setTrainingProgram(trainingProgram);
                    trainingClass.setPlannedAttendee(dataExcelForTrainingClass.getPlannedAttendee());
                    Set<String> listTrainer = dataExcelForTrainingClass.getTrainer();
                    System.out.println(listTrainer.size());
                    Set<Customer> trainers = new HashSet<>();
                    for (String value : listTrainer) {
                        Optional<Customer> customerOptionalT = customerRepository.findByEmail(value);
                        if(customerOptionalT.isPresent()){
                            Customer trainer = customerOptionalT.get();
                            trainers.add(trainer);
                            trainingClass.setAccount_trainers(trainers);
                        }else {
                            throw new BadRequestException("Customer id is not found.");
                        }

                    }
                    Set<String> listAdmin = dataExcelForTrainingClass.getClassAdmin();
                    Set<Customer> admins = new HashSet<>();
                    for (String value : listAdmin) {
                        Optional<Customer> customerOptionalA = customerRepository.findByEmail(value);
                        if(customerOptionalA.isPresent()){
                            Customer admin = customerOptionalA.get();
                            admins.add(admin);
                            trainingClass.setAccount_admins(admins);
                        }else {
                            throw new BadRequestException("Customer id is not found.");
                        }

                    }
                    trainingClassRepository.save(trainingClass);
                    System.out.println("save rồi");
                } else {
                    dataExcelForTrainingClass.setMessageError("TrainingProgram is duplicated with another class");

                }
            } else {
                dataExcelForTrainingClass.setMessageError("TrainingProgram does not exist");
            }
        } else {
            dataExcelForTrainingClass.setMessageError("Already has this class with same courseCode");

        }


    }

    private TrainingProgram findTrainingProgramByNameAndVersion(String name, String version) {
        return trainingProgramRepository.findByName(name,version);
    }

    private TrainingClass findClassByTrainingProgramId(TrainingProgram trainingProgramId) {
        return trainingClassRepository.findClassByTrainingProgramId(trainingProgramId);
    }
}

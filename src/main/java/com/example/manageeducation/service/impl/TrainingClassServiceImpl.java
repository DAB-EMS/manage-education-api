package com.example.manageeducation.service.impl;

import com.example.manageeducation.Utils.SecurityUtil;
import com.example.manageeducation.dto.request.ClassCalendarRequest;
import com.example.manageeducation.dto.request.CustomerRequest;
import com.example.manageeducation.dto.request.TrainingClassRequest;
import com.example.manageeducation.dto.response.TrainingClassesResponse;
import com.example.manageeducation.entity.*;
import com.example.manageeducation.enums.TrainingClassStatus;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.*;
import com.example.manageeducation.service.TrainingClassService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
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
        List<TrainingClass> trainingClasses = trainingClassRepository.findAll();
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
}

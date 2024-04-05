package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.request.CustomerRequest;
import com.example.manageeducation.dto.request.TrainingClassRequest;
import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.entity.Fsu;
import com.example.manageeducation.entity.TrainingClass;
import com.example.manageeducation.entity.TrainingProgram;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.*;
import com.example.manageeducation.service.TrainingClassService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TrainingClassServiceImpl implements TrainingClassService {

    @Autowired
    TrainingClassRepository trainingClassRepository;

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

    @Override
    public String createTrainingClass(String customerId, UUID trainingProgramId, TrainingClassRequest dto) {
        LocalDateTime currentDate = LocalDateTime.now();
        //check validation training program
        Optional<TrainingProgram> trainingProgramOptional = trainingProgramRepository.findById(trainingProgramId);
        if(trainingProgramOptional.isEmpty()){
            throw new BadRequestException("Training program id is not found.");
        }

        //check validation
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
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

        //set Admin
        List<Customer> admins = new ArrayList<>();
        for(CustomerRequest customer1: dto.getAccount_admins()){
            Optional<Customer> AdminOptional = customerRepository.findById(customer1.getId());
            if(AdminOptional.isPresent()){
                admins.add(customerOptional.get());
            }else {
                throw new BadRequestException("Admin id is not found.");
            }
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
        trainingClassRepository.save(trainingClass);
        return "Save successful.";
    }
}

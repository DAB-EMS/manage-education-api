package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.request.ProgramSyllabusRequest;
import com.example.manageeducation.dto.request.SyllabusDayRequest;
import com.example.manageeducation.dto.request.SyllabusUnitRequest;
import com.example.manageeducation.dto.request.TrainingProgramRequest;
import com.example.manageeducation.dto.response.SyllabusResponse;
import com.example.manageeducation.dto.response.TrainingProgramResponse;
import com.example.manageeducation.entity.*;
import com.example.manageeducation.enums.TrainingProgramStatus;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.CustomerRepository;
import com.example.manageeducation.repository.ProgramSyllabusRepository;
import com.example.manageeducation.repository.SyllabusRepository;
import com.example.manageeducation.repository.TrainingProgramRepository;
import com.example.manageeducation.service.TrainingProgramService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TrainingProgramServiceImpl implements TrainingProgramService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SyllabusRepository syllabusRepository;

    @Autowired
    TrainingProgramRepository trainingProgramRepository;

    @Autowired
    ProgramSyllabusRepository programSyllabusRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public String createTrainingProgram(String customerId, TrainingProgramRequest dto) {
        LocalDate currentDate = LocalDate.now();
        Date date = java.sql.Date.valueOf(currentDate);
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
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
                Optional<Syllabus> syllabusOptional = syllabusRepository.findById(trainingProgramRequest.getSyllabusId());
                if(syllabusOptional.isPresent()){
                    programSyllabus.setSyllabus(syllabusOptional.get());
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
            Optional<Customer> customerOptional = customerRepository.findById(trainingProgram.getCreatedBy());
            if(customerOptional.isPresent()){
                trainingProgramResponse.setCreatedBy(customerOptional.get().getFullName());
            }else{
                throw new BadRequestException("Customer id is not found.");
            }

            List<SyllabusResponse> responses = new ArrayList<>();
            for(ProgramSyllabus trainingProgram1: trainingProgramOptional.get().getProgramSyllabusAssociation()){
                responses.add(modelMapper.map(trainingProgram1.getSyllabus(), SyllabusResponse.class));
                for(SyllabusDay syllabusDay:trainingProgram1.getSyllabus().getSyllabusDays()){
                    days++;
                    for (SyllabusUnit syllabusUnit: syllabusDay.getSyllabusUnits()){
                        for(SyllabusUnitChapter syllabusUnitChapter: syllabusUnit.getSyllabusUnitChapters()){
                            hours += (int) syllabusUnitChapter.getDuration();
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
}

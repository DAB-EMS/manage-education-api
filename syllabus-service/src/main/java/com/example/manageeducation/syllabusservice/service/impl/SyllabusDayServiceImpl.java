package com.example.manageeducation.syllabusservice.service.impl;

import com.example.manageeducation.syllabusservice.dto.request.SyllabusDayAddRequest;
import com.example.manageeducation.syllabusservice.dto.response.SyllabusDayResponse;
import com.example.manageeducation.syllabusservice.enums.SyllabusDayStatus;
import com.example.manageeducation.syllabusservice.exception.BadRequestException;
import com.example.manageeducation.syllabusservice.model.Syllabus;
import com.example.manageeducation.syllabusservice.model.SyllabusDay;
import com.example.manageeducation.syllabusservice.repository.SyllabusDayRepository;
import com.example.manageeducation.syllabusservice.repository.SyllabusRepository;
import com.example.manageeducation.syllabusservice.service.SyllabusDayService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SyllabusDayServiceImpl implements SyllabusDayService {

    @Autowired
    SyllabusRepository syllabusRepository;

    @Autowired
    SyllabusDayRepository syllabusDayRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public SyllabusDay createSyllabusDay(UUID id, SyllabusDayAddRequest dto) {
        Optional<Syllabus> syllabusOptional = syllabusRepository.findById(id);
        if(syllabusOptional.isPresent()){
            Syllabus syllabus = syllabusOptional.get();
            SyllabusDay syllabusDay = new SyllabusDay();
            syllabusDay.setDayNo(dto.getDayNo());
            syllabusDay.setStatus(SyllabusDayStatus.AVAILABLE);
            syllabusDay.setSyllabus(syllabus);
            return syllabusDayRepository.save(syllabusDay);
        }else {
            throw new BadRequestException("Syllabus id is not found.");
        }
    }

    @Override
    public String deleteSyllabusDay(UUID id) {
        Optional<SyllabusDay> syllabusDayOptional = syllabusDayRepository.findById(id);
        if(syllabusDayOptional.isPresent()){
            SyllabusDay syllabusDay = syllabusDayOptional.get();
            syllabusDay.setStatus(SyllabusDayStatus.DELETED);
            syllabusDayRepository.save(syllabusDay);
            return "Delete successful";
        }else {
            throw new BadRequestException("Syllabus day id is not found.");
        }
    }

    @Override
    public List<SyllabusDayResponse> syllabusDays(UUID id) {
        List<SyllabusDay> syllabusDays = syllabusDayRepository.findAllBySyllabus_IdAndStatus(id, SyllabusDayStatus.AVAILABLE);
        return syllabusDays.stream()
                .map(syllabusDay -> modelMapper.map(syllabusDay, SyllabusDayResponse.class))
                .collect(Collectors.toList());
    }

}

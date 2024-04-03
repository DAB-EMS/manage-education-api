package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.request.SyllabusUnitAddRequest;
import com.example.manageeducation.dto.request.SyllabusUnitRequest;
import com.example.manageeducation.dto.response.SyllabusUnitResponse;
import com.example.manageeducation.entity.Syllabus;
import com.example.manageeducation.entity.SyllabusDay;
import com.example.manageeducation.entity.SyllabusUnit;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.SyllabusDayRepository;
import com.example.manageeducation.repository.SyllabusRepository;
import com.example.manageeducation.repository.SyllabusUnitRepository;
import com.example.manageeducation.service.SyllabusUnitService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SyllabusUnitServiceImpl implements SyllabusUnitService {

    @Autowired
    SyllabusUnitRepository syllabusUnitRepository;

    @Autowired
    SyllabusRepository syllabusRepository;

    @Autowired
    SyllabusDayRepository syllabusDayRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public SyllabusUnitResponse createSyllabusUnit(UUID syllabusId, UUID syllabusDayId, SyllabusUnitAddRequest dto) {
        Optional<Syllabus> syllabusOptional = syllabusRepository.findById(syllabusId);
        if(syllabusOptional.isEmpty()){
            throw new BadRequestException("Syllabus id is not found.");
        }

        //check syllabus day
        Optional<SyllabusDay> syllabusDayOptional = syllabusDayRepository.findById(syllabusDayId);
        if(syllabusDayOptional.isEmpty()){
            throw new BadRequestException("Syllabus id is not found.");
        }
        SyllabusUnit syllabusUnit = new SyllabusUnit();
        syllabusUnit.setName(dto.getName());
        syllabusUnit.setUnitNo(dto.getUnitNo());
        syllabusUnit.setDuration(dto.getDuration());
        syllabusUnit.setSyllabus(syllabusOptional.get());
        syllabusUnit.setSyllabusDay(syllabusDayOptional.get());
        SyllabusUnit saved = syllabusUnitRepository.save(syllabusUnit);
        return modelMapper.map(saved,SyllabusUnitResponse.class);
    }

    @Override
    public SyllabusUnitResponse updateSyllabusUnit(UUID unitId, SyllabusUnitAddRequest dto) {
        Optional<SyllabusUnit> syllabusUnitOptional = syllabusUnitRepository.findById(unitId);
        if(syllabusUnitOptional.isPresent()){
            SyllabusUnit syllabusUnit = syllabusUnitOptional.get();
            modelMapper.map(dto,syllabusUnit);
            SyllabusUnit saved = syllabusUnitRepository.save(syllabusUnit);
            return modelMapper.map(saved,SyllabusUnitResponse.class);
        }else {
            throw new BadRequestException("Syllabus unit id is not found.");
        }
    }
}

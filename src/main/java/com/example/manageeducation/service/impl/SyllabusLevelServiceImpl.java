package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.request.SyllabusLevelRequest;
import com.example.manageeducation.dto.response.SyllabusLevelResponse;
import com.example.manageeducation.entity.SyllabusLevel;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.SyllabusLevelRepository;
import com.example.manageeducation.service.SyllabusLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SyllabusLevelServiceImpl implements SyllabusLevelService {

    @Autowired
    SyllabusLevelRepository syllabusLevelRepository;

    @Override
    public String createSyllabusLevel(SyllabusLevelRequest dto) {
        SyllabusLevel syllabusLevel = new SyllabusLevel();
        syllabusLevel.setName(dto.getName());
        syllabusLevelRepository.save(syllabusLevel);
        return "create successful";
    }

    @Override
    public SyllabusLevelResponse updateSyllabusLevel(UUID id, SyllabusLevelRequest dto) {
        Optional<SyllabusLevel> syllabusLevelOptional = syllabusLevelRepository.findById(id);
        if(syllabusLevelOptional.isPresent()){
            SyllabusLevel syllabusLevel = syllabusLevelOptional.get();
            syllabusLevel.setName(dto.getName());
            syllabusLevelRepository.save(syllabusLevel);
            SyllabusLevelResponse level = new SyllabusLevelResponse();
            level.setId(syllabusLevel.getId());
            level.setName(dto.getName());
            return level;
        }else{
            throw new BadRequestException("Syllabus level id not found.");
        }
    }

    @Override
    public List<SyllabusLevelResponse> syllabusLevelResponse() {
        List<SyllabusLevel> syllabusLevels = syllabusLevelRepository.findAll();
        List<SyllabusLevelResponse> responses = new ArrayList<>();
        for (SyllabusLevel response:syllabusLevels){
            SyllabusLevelResponse response1 = new SyllabusLevelResponse();
            response1.setId(response.getId());
            response1.setName(response.getName());
            responses.add(response1);
        }
        return responses;
    }
}

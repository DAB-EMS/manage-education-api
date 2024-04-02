package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.request.MaterialRequest;
import com.example.manageeducation.dto.response.MaterialResponse;
import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.entity.Material;
import com.example.manageeducation.entity.SyllabusUnitChapter;
import com.example.manageeducation.enums.MaterialStatus;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.CustomerRepository;
import com.example.manageeducation.repository.MaterialRepository;
import com.example.manageeducation.repository.SyllabusUnitChapterRepository;
import com.example.manageeducation.repository.SyllabusUnitRepository;
import com.example.manageeducation.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SyllabusUnitChapterRepository syllabusUnitChapterRepository;

    @Override
    public Material createMaterial(String customerId, UUID chapterId, MaterialRequest dto) {
        LocalDate currentDate = LocalDate.now();
        Date date = java.sql.Date.valueOf(currentDate);

        //check validation customer
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if(customerOptional.isEmpty()){
            throw new BadRequestException("Customer id not found.");
        }

        //check validation unit chapter
        Optional<SyllabusUnitChapter> syllabusUnitChapter = syllabusUnitChapterRepository.findById(chapterId);
        if(syllabusUnitChapter.isEmpty()){
            throw new BadRequestException("Syllabus unit chapter id not found.");
        }

        Material material = new Material();
        material.setName(dto.getName());
        material.setUrl(dto.getUrl());
        material.setCreatedBy(customerOptional.get().getId());
        material.setCreatedDate(date);
        material.setUpdatedBy(customerOptional.get().getId());
        material.setUpdatedDate(date);
        material.setMaterialStatus(MaterialStatus.ACTIVE);
        material.setUnitChapter(syllabusUnitChapter.get());
        return materialRepository.save(material);
    }

    @Override
    public Material updateMaterial(String customerId, UUID materialId, MaterialRequest dto) {
        LocalDate currentDate = LocalDate.now();
        Date date = java.sql.Date.valueOf(currentDate);
        //check validation customer
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if(customerOptional.isEmpty()){
            throw new BadRequestException("Customer id not found.");
        }

        //check validation material
        Optional<Material> materialOptional = materialRepository.findById(materialId);
        if(materialOptional.isPresent()){
            Material material = materialOptional.get();
            material.setName(dto.getName());
            material.setUrl(dto.getUrl());
            material.setUpdatedBy(customerOptional.get().getId());
            material.setUpdatedDate(date);
            return materialRepository.save(material);

        }else {
            throw new BadRequestException("Material id not found.");
        }
    }

    @Override
    public String deleteMaterial(UUID materialId) {
        Optional<Material> materialOptional = materialRepository.findById(materialId);
        if (materialOptional.isPresent()){
            Material material = materialOptional.get();
            material.setMaterialStatus(MaterialStatus.DELETED);
            materialRepository.save(material);
            return "Material with ID " + materialId + " has been deleted successfully.";
        }else{
            throw new BadRequestException("Material id is not found.");
        }
    }

    @Override
    public List<MaterialResponse> materials(UUID chapterId) {
        try {
            List<Material> materials = materialRepository.findAllByUnitChapter_IdAndMaterialStatus(chapterId,MaterialStatus.ACTIVE);
            List<MaterialResponse> materialResponses = new ArrayList<>();
            for(Material material:materials){
                MaterialResponse materialResponse = new MaterialResponse();
                materialResponse.setId(material.getId());
                materialResponse.setName(material.getName());
                materialResponse.setUrl(material.getUrl());
                //get name customer
                Optional<Customer> customerOptional = customerRepository.findById(material.getCreatedBy());
                if(customerOptional.isPresent()){
                    materialResponse.setCreatedBy(customerOptional.get().getFullName());
                }else {
                    throw new BadRequestException("Customer id not found.");
                }

                Optional<Customer> customerOptional1 = customerRepository.findById(material.getCreatedBy());
                if(customerOptional1.isPresent()){
                    materialResponse.setUpdatedBy(customerOptional1.get().getFullName());
                }else {
                    throw new BadRequestException("Customer id not found.");
                }
                materialResponse.setCreatedDate(material.getCreatedDate());
                materialResponse.setUpdatedDate(material.getUpdatedDate());
                materialResponses.add(materialResponse);
            }
            return materialResponses;
        } catch (Exception ex) {
            // Log exception
            ex.printStackTrace();
            throw new BadRequestException("An internal server error occurred.");
        }
    }
}

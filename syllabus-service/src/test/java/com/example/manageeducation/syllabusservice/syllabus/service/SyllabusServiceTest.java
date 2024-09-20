package com.example.manageeducation.syllabusservice.syllabus.service;

import com.example.manageeducation.syllabusservice.dto.request.AssessmentSchemeRequest;
import com.example.manageeducation.syllabusservice.dto.request.SyllabusDayRequest;
import com.example.manageeducation.syllabusservice.dto.request.SyllabusRequest;
import com.example.manageeducation.syllabusservice.dto.request.SyllabusUpdateRequest;
import com.example.manageeducation.syllabusservice.enums.MaterialStatus;
import com.example.manageeducation.syllabusservice.enums.SyllabusDayStatus;
import com.example.manageeducation.syllabusservice.enums.SyllabusStatus;
import com.example.manageeducation.syllabusservice.jdbc.SyllabusJdbc;
import com.example.manageeducation.syllabusservice.model.*;
import com.example.manageeducation.syllabusservice.repository.DeliveryTypeRepository;
import com.example.manageeducation.syllabusservice.repository.OutputStandardRepository;
import com.example.manageeducation.syllabusservice.repository.SyllabusLevelRepository;
import com.example.manageeducation.syllabusservice.repository.SyllabusRepository;
import com.example.manageeducation.syllabusservice.service.SyllabusService;
import com.example.manageeducation.syllabusservice.utils.SyllabusServiceUtils;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Principal;
import java.util.*;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.mockito.Mockito.when;

@SpringBootTest
@Log4j2
public class SyllabusServiceTest {

    @Mock
    private Principal principal;

    @MockBean
    private SyllabusRepository syllabusRepository;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private SyllabusLevelRepository syllabusLevelRepository;

    @MockBean
    private OutputStandardRepository outputStandardRepository;

    @MockBean
    private DeliveryTypeRepository deliveryTypeRepository;

    @MockBean
    private SyllabusJdbc syllabusJdbc;

    @MockBean
    private SyllabusServiceUtils syllabusServiceUtil;

    @Autowired
    private SyllabusService syllabusService;

    private final UUID syllabusLevelId1 = UUID.randomUUID();
    private final UUID syllabusLevelId2 = UUID.randomUUID();
    private final UUID syllabusLevelId3 = UUID.randomUUID();
    private final UUID outputStandardId1 = UUID.randomUUID();
    private final UUID outputStandardId2 = UUID.randomUUID();
    private final UUID outputStandardId3 = UUID.randomUUID();
    private final UUID deliveryTypeId1 = UUID.randomUUID();
    private final UUID deliveryTypeId2 = UUID.randomUUID();
    private final UUID deliveryTypeId3 = UUID.randomUUID();
    private final UUID deliveryTypeId4 = UUID.randomUUID();
    private final UUID deliveryTypeId5 = UUID.randomUUID();
    private final UUID deliveryTypeId6 = UUID.randomUUID();

    private final UUID userB = UUID.randomUUID();
    private final UUID userA = UUID.randomUUID();
    private final UUID userC = UUID.randomUUID();
    private final UUID syllabus1 = UUID.randomUUID();
    private final UUID syllabus2 = UUID.randomUUID();
    private final UUID syllabus3 = UUID.randomUUID();
    private final UUID syllabus4 = UUID.randomUUID();
    private final UUID syllabus5 = UUID.randomUUID();
    private final UUID syllabusDeleted = UUID.randomUUID();
    private final UUID syllabusNotFound = UUID.randomUUID();
    private final UUID syllabusDraft = UUID.randomUUID();
    private final UUID syllabusDraft2 = UUID.randomUUID();
    private final List<Syllabus> syllabuses = new ArrayList<>(List.of(
            Syllabus.builder().id(syllabus1).name(".Net Basic Program").code("NBP").createdBy(userB).days(2)
                    .createdDate(new Date(2020, 11, 11)).hours(10).status(SyllabusStatus.ACTIVE).updatedBy(userB).version("1.0").build(),
            Syllabus.builder().id(syllabus2).name("Azure DevOps").code("ADO").createdBy(userB).days(2).hours(10)
                    .createdDate(new Date(2020, 12, 20)).status(SyllabusStatus.ACTIVE).updatedBy(userB).version("1.0").build(),
            Syllabus.builder().id(syllabus3).name("JUnit Testing").code("FSJ").createdBy(userB).days(2).hours(10)
                    .createdDate(new Date(2019, 10, 10)).status(SyllabusStatus.ACTIVE).updatedBy(userB).version("1.0").build(),
            Syllabus.builder().id(syllabus4).name("JUnit Testing").code("JUT").createdBy(userB).days(2).hours(10)
                    .createdDate(new Date(2011, 20, 20)).status(SyllabusStatus.DEACTIVE).updatedBy(userB).version("1.1").build(),
            Syllabus.builder().id(syllabus5).name("JUnit Testing").code("JUT").createdBy(userB).days(2).hours(10)
                    .createdDate(new Date(2017, 9, 8)).status(SyllabusStatus.ACTIVE).updatedBy(userB).version("3.0").build()
    ));

    @BeforeEach
    void setUp() throws Exception {
        List<SyllabusLevel> syllabusLevels = new ArrayList<>(List.of(
                SyllabusLevel.builder().id(syllabusLevelId1).name("Basic").build(),
                SyllabusLevel.builder().id(syllabusLevelId2).name("Advanced").build(),
                SyllabusLevel.builder().id(syllabusLevelId3).name("Intermediate").build()
        ));

        List<OutputStandard> outputStandards = new ArrayList<>(List.of(
                OutputStandard.builder().id(outputStandardId1).code("H4SD").name("Coding convention").description("None").build(),
                OutputStandard.builder().id(outputStandardId2).code("K4SD").name("Comment convention").description("None").build(),
                OutputStandard.builder().id(outputStandardId3).code("K5SD").name("Testing convention").description("None").build()
        ));

        List<DeliveryType> deliveryTypes = new ArrayList<>(List.of(
                DeliveryType.builder().id(deliveryTypeId1).name("Assignment/Lab").description("None").build(),
                DeliveryType.builder().id(deliveryTypeId2).name("Concept/Lecture").description("None").build(),
                DeliveryType.builder().id(deliveryTypeId3).name("Guide/Review").description("None").build(),
                DeliveryType.builder().id(deliveryTypeId4).name("Test/Quiz").description("None").build(),
                DeliveryType.builder().id(deliveryTypeId5).name("Exam").description("None").build(),
                DeliveryType.builder().id(deliveryTypeId6).name("Seminar/Workshop").description("None").build()
        ));

        // Setup Optional for syllabus level
        Optional<SyllabusLevel> oSyllabusLevel1 = Optional.ofNullable(syllabusLevels.get(0));
        Optional<SyllabusLevel> oSyllabusLevel2 = Optional.ofNullable(syllabusLevels.get(1));
        Optional<SyllabusLevel> oSyllabusLevel3 = Optional.ofNullable(syllabusLevels.get(2));

        // Mockito to syllabus level
        when(syllabusLevelRepository.findById(syllabusLevelId1)).thenReturn(oSyllabusLevel1);
        when(syllabusLevelRepository.findById(syllabusLevelId2)).thenReturn(oSyllabusLevel2);
        when(syllabusLevelRepository.findById(syllabusLevelId3)).thenReturn(oSyllabusLevel3);


        // Setup Optional for output standard
        Optional<OutputStandard> oOutputStandard1 = Optional.ofNullable(outputStandards.get(0));
        Optional<OutputStandard> oOutputStandard2 = Optional.ofNullable(outputStandards.get(1));
        Optional<OutputStandard> oOutputStandard3 = Optional.ofNullable(outputStandards.get(2));

        // Mockito to output standard
        when(outputStandardRepository.findById(outputStandardId1)).thenReturn(oOutputStandard1);
        when(outputStandardRepository.findById(outputStandardId2)).thenReturn(oOutputStandard2);
        when(outputStandardRepository.findById(outputStandardId3)).thenReturn(oOutputStandard3);


        // Setup Optional for delivery type
        Optional<DeliveryType> oDeliveryType1 = Optional.ofNullable(deliveryTypes.get(0));
        Optional<DeliveryType> oDeliveryType2 = Optional.ofNullable(deliveryTypes.get(1));
        Optional<DeliveryType> oDeliveryType3 = Optional.ofNullable(deliveryTypes.get(2));
        Optional<DeliveryType> oDeliveryType4 = Optional.ofNullable(deliveryTypes.get(3));
        Optional<DeliveryType> oDeliveryType5 = Optional.ofNullable(deliveryTypes.get(4));
        Optional<DeliveryType> oDeliveryType6 = Optional.ofNullable(deliveryTypes.get(5));

        // Mockito to delivery type
        when(deliveryTypeRepository.findById(deliveryTypeId1)).thenReturn(oDeliveryType1);
        when(deliveryTypeRepository.findById(deliveryTypeId2)).thenReturn(oDeliveryType2);
        when(deliveryTypeRepository.findById(deliveryTypeId3)).thenReturn(oDeliveryType3);
        when(deliveryTypeRepository.findById(deliveryTypeId4)).thenReturn(oDeliveryType4);
        when(deliveryTypeRepository.findById(deliveryTypeId5)).thenReturn(oDeliveryType5);
        when(deliveryTypeRepository.findById(deliveryTypeId6)).thenReturn(oDeliveryType6);

        when(syllabusRepository.findSyllabusById(syllabus1)).thenReturn(syllabuses.get(0));
        when(syllabusRepository.findSyllabusById(syllabus2)).thenReturn(syllabuses.get(1));
        when(syllabusRepository.findSyllabusById(syllabus3)).thenReturn(syllabuses.get(2));
        when(syllabusRepository.findSyllabusById(syllabus4)).thenReturn(syllabuses.get(3));
        when(syllabusRepository.findSyllabusById(syllabus5)).thenReturn(syllabuses.get(4));
    }

    @Test
    @DisplayName("Create new syllabus")
    public void createSyllabus() {
        when(principal.getName()).thenReturn("testUser");
        Material material = Material.builder().name("Material").materialStatus(MaterialStatus.ACTIVE).createdDate(new Date())
                .updatedDate(new Date()).createdBy(UUID.randomUUID()).updatedBy(UUID.randomUUID()).build();
        List<Material> materialList = new ArrayList<>();
        materialList.add(material);

        SyllabusUnitChapter syllabusUnitChapter1 = SyllabusUnitChapter.builder().isOnline(true).materials(materialList)
                .outputStandard(outputStandardRepository.findById(outputStandardId1).get())
                .deliveryType(deliveryTypeRepository.findById(deliveryTypeId1).get()).build();
        List<SyllabusUnitChapter> syllabusUnitChapterList = new ArrayList<>();
        syllabusUnitChapterList.add(syllabusUnitChapter1);

        SyllabusUnit syllabusUnit1 = SyllabusUnit.builder().unitNo(1).duration(10).syllabusUnitChapters(syllabusUnitChapterList).build();
        List<SyllabusUnit> syllabusUnitList = new ArrayList<>();
        syllabusUnitList.add(syllabusUnit1);

        SyllabusDay syllabusDay1 = SyllabusDay.builder().dayNo(1).status(SyllabusDayStatus.AVAILABLE).syllabusUnits(syllabusUnitList).build();
        List<SyllabusDayRequest> syllabusDayList = new ArrayList<>();
        syllabusDayList.add(modelMapper.map(syllabusDay1, SyllabusDayRequest.class));

        AssessmentSchemeRequest assessmentScheme1 = AssessmentSchemeRequest.builder().assignment(15.0).quiz(15.0).gpa(70.0)
                .finalPoint(70.0).finalTheory(40.0).finalPractice(60.0).build();

        SyllabusRequest requestSyllabus = SyllabusRequest.builder().name(".NET Programming Language").code("NPL")
                .syllabusDays(syllabusDayList).version("1.0").assessmentScheme(assessmentScheme1).attendeeNumber(20)
                .syllabusLevel(syllabusLevelId1).build();

        try{
            String createSuccessfully = syllabusService.createSyllabus(principal,requestSyllabus);
            Assertions.assertThat(createSuccessfully).isEqualTo("Create successfully.");
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Create new syllabus not found assessment scheme")
    public void createSyllabusNotFoundAssessmentScheme() {
        when(principal.getName()).thenReturn("testUser");
        Material material = Material.builder().name("Material").materialStatus(MaterialStatus.ACTIVE).createdDate(new Date())
                .updatedDate(new Date()).createdBy(UUID.randomUUID()).updatedBy(UUID.randomUUID()).build();
        List<Material> materialList = new ArrayList<>();
        materialList.add(material);

        SyllabusUnitChapter syllabusUnitChapter1 = SyllabusUnitChapter.builder().isOnline(true).materials(materialList)
                .outputStandard(outputStandardRepository.findById(outputStandardId1).get())
                .deliveryType(deliveryTypeRepository.findById(deliveryTypeId1).get()).build();
        List<SyllabusUnitChapter> syllabusUnitChapterList = new ArrayList<>();
        syllabusUnitChapterList.add(syllabusUnitChapter1);

        SyllabusUnit syllabusUnit1 = SyllabusUnit.builder().unitNo(1).duration(10).syllabusUnitChapters(syllabusUnitChapterList).build();
        List<SyllabusUnit> syllabusUnitList = new ArrayList<>();
        syllabusUnitList.add(syllabusUnit1);

        SyllabusDay syllabusDay1 = SyllabusDay.builder().dayNo(1).status(SyllabusDayStatus.AVAILABLE).syllabusUnits(syllabusUnitList).build();
        List<SyllabusDayRequest> syllabusDayList = new ArrayList<>();
        syllabusDayList.add(modelMapper.map(syllabusDay1, SyllabusDayRequest.class));

        SyllabusRequest requestSyllabus = SyllabusRequest.builder().name(".NET Programming Language").code("NPL")
                .syllabusDays(syllabusDayList).version("1.0").attendeeNumber(20)
                .syllabusLevel(syllabusLevelId1).build();

        try{
            String createSuccessfully = syllabusService.createSyllabus(principal,requestSyllabus);
            Assertions.assertThat(createSuccessfully).isEqualTo("Assessment scheme is not found.");
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Create syllabus not found syllabus day")
    public void createSyllabusNotFoundSyllabusDay() {
        when(principal.getName()).thenReturn("testUser");
        Material material = Material.builder().name("Material").materialStatus(MaterialStatus.ACTIVE).createdDate(new Date())
                .updatedDate(new Date()).createdBy(UUID.randomUUID()).updatedBy(UUID.randomUUID()).build();
        List<Material> materialList = new ArrayList<>();
        materialList.add(material);

        SyllabusUnitChapter syllabusUnitChapter1 = SyllabusUnitChapter.builder().isOnline(true).materials(materialList)
                .outputStandard(outputStandardRepository.findById(outputStandardId1).get())
                .deliveryType(deliveryTypeRepository.findById(deliveryTypeId1).get()).build();
        List<SyllabusUnitChapter> syllabusUnitChapterList = new ArrayList<>();
        syllabusUnitChapterList.add(syllabusUnitChapter1);

        AssessmentSchemeRequest assessmentScheme1 = AssessmentSchemeRequest.builder().assignment(15.0).quiz(15.0).gpa(70.0)
                .finalPoint(70.0).finalTheory(40.0).finalPractice(60.0).build();

        SyllabusRequest requestSyllabus = SyllabusRequest.builder().name(".NET Programming Language").code("NPL")
                .version("1.0").assessmentScheme(assessmentScheme1).attendeeNumber(20)
                .syllabusLevel(syllabusLevelId1).build();
        try{
            String createSyllabus = syllabusService.createSyllabus(principal,requestSyllabus);
            Assertions.assertThat(createSyllabus).isEqualTo("Syllabus day is not found.");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Create syllabus not found syllabus unit")
    public void createSyllabusNotFoundSyllabusUnit() {
        when(principal.getName()).thenReturn("testUser");
        Material material = Material.builder().name("Material").materialStatus(MaterialStatus.ACTIVE).createdDate(new Date())
                .updatedDate(new Date()).createdBy(UUID.randomUUID()).updatedBy(UUID.randomUUID()).build();
        List<Material> materialList = new ArrayList<>();
        materialList.add(material);

        SyllabusUnitChapter syllabusUnitChapter1 = SyllabusUnitChapter.builder().isOnline(true).materials(materialList)
                .outputStandard(outputStandardRepository.findById(outputStandardId1).get())
                .deliveryType(deliveryTypeRepository.findById(deliveryTypeId1).get()).build();
        List<SyllabusUnitChapter> syllabusUnitChapterList = new ArrayList<>();
        syllabusUnitChapterList.add(syllabusUnitChapter1);

        SyllabusDay syllabusDay1 = SyllabusDay.builder().dayNo(1).status(SyllabusDayStatus.AVAILABLE).build();
        List<SyllabusDayRequest> syllabusDayList = new ArrayList<>();
        syllabusDayList.add(modelMapper.map(syllabusDay1, SyllabusDayRequest.class));

        AssessmentSchemeRequest assessmentScheme1 = AssessmentSchemeRequest.builder().assignment(15.0).quiz(15.0).gpa(70.0)
                .finalPoint(70.0).finalTheory(40.0).finalPractice(60.0).build();

        SyllabusRequest requestSyllabus = SyllabusRequest.builder().name(".NET Programming Language").code("NPL")
                .syllabusDays(syllabusDayList).version("1.0").assessmentScheme(assessmentScheme1).attendeeNumber(20)
                .syllabusLevel(syllabusLevelId1).build();
        try{
            String createSyllabus = syllabusService.createSyllabus(principal,requestSyllabus);
            Assertions.assertThat(createSyllabus).isEqualTo("Syllabus unit is not found.");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Delete syllabus successfully")
    public void deleteSyllabus() {
        try {
            String deleteSuccessfully = syllabusService.deleteSyllabus(syllabusLevelId1);
            Assertions.assertThat(deleteSuccessfully).isEqualTo("Delete successfully.");
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Duplication syllabus successfully")
    public void duplicationSyllabus() {
        try {
            String deleteSuccessfully = syllabusService.duplicatedSyllabus(syllabusLevelId2);
            Assertions.assertThat(deleteSuccessfully).isEqualTo("Create duplication successfully.");
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("De-active syllabus successfully")
    public void deActiveSyllabus() {
        try {
            String deleteSuccessfully = syllabusService.deActive(syllabusLevelId2);
            Assertions.assertThat(deleteSuccessfully).isEqualTo("De-active successfully.");
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Update syllabus successfully")
    public void updateSyllabus() {
        when(principal.getName()).thenReturn("testUser");
        Material material = Material.builder().name("Material").materialStatus(MaterialStatus.ACTIVE).createdDate(new Date())
                .updatedDate(new Date()).createdBy(UUID.randomUUID()).updatedBy(UUID.randomUUID()).build();
        List<Material> materialList = new ArrayList<>();
        materialList.add(material);

        SyllabusUnitChapter syllabusUnitChapter1 = SyllabusUnitChapter.builder().isOnline(true).materials(materialList)
                .outputStandard(outputStandardRepository.findById(outputStandardId1).get())
                .deliveryType(deliveryTypeRepository.findById(deliveryTypeId1).get()).build();
        List<SyllabusUnitChapter> syllabusUnitChapterList = new ArrayList<>();
        syllabusUnitChapterList.add(syllabusUnitChapter1);

        AssessmentSchemeRequest assessmentScheme1 = AssessmentSchemeRequest.builder().assignment(15.0).quiz(15.0).gpa(70.0)
                .finalPoint(70.0).finalTheory(40.0).finalPractice(60.0).build();

        SyllabusUpdateRequest syllabusUpdateRequest = SyllabusUpdateRequest.builder().name(".NET Programming Language").code("NPL")
                .version("1.0").assessmentScheme(assessmentScheme1).attendeeNumber(20)
                .syllabusLevel(syllabusLevelId1).build();
        try{
            Syllabus updateSyllabus = syllabusService.updateSyllabus(syllabusLevelId3,syllabusUpdateRequest);
            Assertions.assertThat(updateSyllabus.getName()).isEqualTo(syllabusUpdateRequest.getName());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}

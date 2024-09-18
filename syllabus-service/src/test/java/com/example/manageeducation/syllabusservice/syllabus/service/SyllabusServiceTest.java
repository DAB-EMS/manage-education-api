package com.example.manageeducation.syllabusservice.syllabus.service;

import com.example.manageeducation.syllabusservice.enums.SyllabusStatus;
import com.example.manageeducation.syllabusservice.jdbc.SyllabusJdbc;
import com.example.manageeducation.syllabusservice.model.DeliveryType;
import com.example.manageeducation.syllabusservice.model.OutputStandard;
import com.example.manageeducation.syllabusservice.model.Syllabus;
import com.example.manageeducation.syllabusservice.model.SyllabusLevel;
import com.example.manageeducation.syllabusservice.repository.DeliveryTypeRepository;
import com.example.manageeducation.syllabusservice.repository.OutputStandardRepository;
import com.example.manageeducation.syllabusservice.repository.SyllabusLevelRepository;
import com.example.manageeducation.syllabusservice.repository.SyllabusRepository;
import com.example.manageeducation.syllabusservice.service.SyllabusService;
import com.example.manageeducation.syllabusservice.utils.SyllabusServiceUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

@SpringBootTest
@Log4j2
public class SyllabusServiceTest {

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
        Mockito.when(syllabusLevelRepository.findById(syllabusLevelId1)).thenReturn(oSyllabusLevel1);
        Mockito.when(syllabusLevelRepository.findById(syllabusLevelId2)).thenReturn(oSyllabusLevel2);
        Mockito.when(syllabusLevelRepository.findById(syllabusLevelId3)).thenReturn(oSyllabusLevel3);


        // Setup Optional for output standard
        Optional<OutputStandard> oOutputStandard1 = Optional.ofNullable(outputStandards.get(0));
        Optional<OutputStandard> oOutputStandard2 = Optional.ofNullable(outputStandards.get(1));
        Optional<OutputStandard> oOutputStandard3 = Optional.ofNullable(outputStandards.get(2));

        // Mockito to output standard
        Mockito.when(outputStandardRepository.findById(outputStandardId1)).thenReturn(oOutputStandard1);
        Mockito.when(outputStandardRepository.findById(outputStandardId2)).thenReturn(oOutputStandard2);
        Mockito.when(outputStandardRepository.findById(outputStandardId3)).thenReturn(oOutputStandard3);


        // Setup Optional for delivery type
        Optional<DeliveryType> oDeliveryType1 = Optional.ofNullable(deliveryTypes.get(0));
        Optional<DeliveryType> oDeliveryType2 = Optional.ofNullable(deliveryTypes.get(1));
        Optional<DeliveryType> oDeliveryType3 = Optional.ofNullable(deliveryTypes.get(2));
        Optional<DeliveryType> oDeliveryType4 = Optional.ofNullable(deliveryTypes.get(3));
        Optional<DeliveryType> oDeliveryType5 = Optional.ofNullable(deliveryTypes.get(4));
        Optional<DeliveryType> oDeliveryType6 = Optional.ofNullable(deliveryTypes.get(5));

        // Mockito to delivery type
        Mockito.when(deliveryTypeRepository.findById(deliveryTypeId1)).thenReturn(oDeliveryType1);
        Mockito.when(deliveryTypeRepository.findById(deliveryTypeId2)).thenReturn(oDeliveryType2);
        Mockito.when(deliveryTypeRepository.findById(deliveryTypeId3)).thenReturn(oDeliveryType3);
        Mockito.when(deliveryTypeRepository.findById(deliveryTypeId4)).thenReturn(oDeliveryType4);
        Mockito.when(deliveryTypeRepository.findById(deliveryTypeId5)).thenReturn(oDeliveryType5);
        Mockito.when(deliveryTypeRepository.findById(deliveryTypeId6)).thenReturn(oDeliveryType6);

//        List<User> users = new ArrayList<>(List.of(
//                User.builder().id(userA).fullname("Lam Anh").build(),
//
//                User.builder().id(userB).fullname("The Huy").build(),
//
//                User.builder().id(userC).fullname("Tan Phuc").build()
//        ));

        Mockito.when(syllabusRepository.findSyllabusById(syllabus1)).thenReturn(syllabuses.get(0));
        Mockito.when(syllabusRepository.findSyllabusById(syllabus2)).thenReturn(syllabuses.get(1));
        Mockito.when(syllabusRepository.findSyllabusById(syllabus3)).thenReturn(syllabuses.get(2));
        Mockito.when(syllabusRepository.findSyllabusById(syllabus4)).thenReturn(syllabuses.get(3));
        Mockito.when(syllabusRepository.findSyllabusById(syllabus5)).thenReturn(syllabuses.get(4));

//        Optional<User> userOptionalA = Optional.ofNullable(users.get(0));
//        Optional<User> userOptionalB = Optional.ofNullable(users.get(1));
//        Optional<User> userOptionalC = Optional.ofNullable(users.get(2));
//        Mockito.when(userRepository.findById(userA)).thenReturn(userOptionalA);
//        Mockito.when(userRepository.findById(userB)).thenReturn(userOptionalB);
//        Mockito.when(userRepository.findById(userC)).thenReturn(userOptionalC);
    }

}

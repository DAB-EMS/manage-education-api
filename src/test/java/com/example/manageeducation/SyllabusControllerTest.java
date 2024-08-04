package com.example.manageeducation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.manageeducation.controller.SyllabusController;
import com.example.manageeducation.dto.request.*;
import com.example.manageeducation.dto.response.ViewSyllabusResponse;
import com.example.manageeducation.entity.*;
import com.example.manageeducation.enums.SyllabusDayStatus;
import com.example.manageeducation.enums.SyllabusStatus;
import com.example.manageeducation.service.SyllabusService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;
import java.security.Principal;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class SyllabusControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private SyllabusService syllabusService;

    private ModelMapper modelMapper;

    @InjectMocks
    private SyllabusController syllabusController;

    @BeforeEach
    @Before
    @BeforeAll
    public void setUp() {
        modelMapper = new ModelMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(syllabusController).build();

        List<Syllabus> mockSyllabuses = List.of(createTestSyllabus());
        List<ViewSyllabusResponse> mockViewSyllabuses = mockSyllabuses.stream()
                .map(syllabus -> modelMapper.map(syllabus, ViewSyllabusResponse.class))
                .collect(Collectors.toList());

        when(syllabusService.syllabuses()).thenReturn(mockViewSyllabuses);
    }

    @Test
    public void getAllRecordsSuccess() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/auth/syllabus")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Test Syllabus"))
                .andExpect(jsonPath("$[0].code").value("TS101"));
    }

    @Test
    public void createSyllabus() throws Exception{
        Principal principal = Mockito.mock(Principal.class);
        SyllabusRequest record = postTestSyllabus();
        Mockito.when(syllabusService.createSyllabus(principal,record)).thenReturn("create successful");
        String content = objectWriter.writeValueAsString(record);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/auth/customer/syllabus")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name",is("Test Syllabus")));

    }

    public static Syllabus createTestSyllabus() {
        Syllabus syllabus = new Syllabus();
        syllabus.setId(UUID.randomUUID());
        syllabus.setName("Test Syllabus");
        syllabus.setCode("TS101");
        syllabus.setVersion("1.0");
        syllabus.setAttendeeNumber(30);
        syllabus.setTechnicalRequirement("Test Technical Requirement");
        syllabus.setCourseObjective("Test Course Objective");
        syllabus.setDays(10);
        syllabus.setHours(40);
        syllabus.setStatus(SyllabusStatus.ACTIVE);
        syllabus.setTemplate(false);
        syllabus.setCreatedBy(UUID.randomUUID());
        syllabus.setCreatedDate(new Date());
        syllabus.setUpdatedBy(UUID.randomUUID());
        syllabus.setUpdatedDate(new Date());

        // Create AssessmentScheme
        AssessmentScheme assessmentScheme = new AssessmentScheme();
        assessmentScheme.setId(UUID.randomUUID());
        assessmentScheme.setAssignment(20.0);
        assessmentScheme.setQuiz(30.0);
        assessmentScheme.setExam(50.0);
        assessmentScheme.setGpa(4.0);
        assessmentScheme.setFinalPoint(100.0);
        assessmentScheme.setFinalTheory(50.0);
        assessmentScheme.setFinalPractice(50.0);
        assessmentScheme.setSyllabus(syllabus);
        syllabus.setAssessmentScheme(assessmentScheme);

        // Create SyllabusLevel
        SyllabusLevel syllabusLevel = new SyllabusLevel();
        syllabusLevel.setId(UUID.randomUUID());
        syllabusLevel.setName("Intermediate");
        syllabus.setSyllabusLevel(syllabusLevel);

        // Create SyllabusDay
        SyllabusDay syllabusDay = new SyllabusDay();
        syllabusDay.setId(UUID.randomUUID());
        syllabusDay.setDayNo(1);
        syllabusDay.setStatus(SyllabusDayStatus.AVAILABLE);
        syllabusDay.setSyllabus(syllabus);

        // Create SyllabusUnit
        SyllabusUnit syllabusUnit = new SyllabusUnit();
        syllabusUnit.setId(UUID.randomUUID());
        syllabusUnit.setName("Introduction to Testing");
        syllabusUnit.setUnitNo(1);
        syllabusUnit.setDuration(2);
        syllabusUnit.setSyllabus(syllabus);
        syllabusUnit.setSyllabusDay(syllabusDay);

        syllabusDay.setSyllabusUnits(List.of(syllabusUnit));
        syllabus.setSyllabusDays(List.of(syllabusDay));

        // Add SyllabusUnits to Syllabus
        syllabus.setSyllabusUnits(List.of(syllabusUnit));

        return syllabus;
    }

    public static SyllabusRequest postTestSyllabus() {
        // Sample data for AssessmentScheme
        AssessmentSchemeRequest assessmentScheme = AssessmentSchemeRequest.builder()
                .assignment(10.0)
                .quiz(20.0)
                .exam(30.0)
                .gpa(4.0)
                .finalPoint(60.0)
                .finalTheory(20.0)
                .finalPractice(40.0)
                .build();

        // Sample data for SyllabusLevel
        SyllabusLevelRequest syllabusLevel = SyllabusLevelRequest.builder()
                .name("Beginner")
                .build();

        // Sample data for SyllabusDay
        SyllabusDayRequest syllabusDay1 = SyllabusDayRequest.builder()
                .dayNo(1)
                .status(SyllabusDayStatus.AVAILABLE)
                .build();

        SyllabusDayRequest syllabusDay2 = SyllabusDayRequest.builder()
                .dayNo(2)
                .status(SyllabusDayStatus.AVAILABLE)
                .build();

        List<SyllabusDayRequest> syllabusDays = new ArrayList<>();
        syllabusDays.add(syllabusDay1);
        syllabusDays.add(syllabusDay2);

        // Sample data for DeliveryPrinciple
        DeliveryPrincipleImportRequest deliveryPrinciple = DeliveryPrincipleImportRequest.builder()
                .trainees("Classroom based")
                .build();

        // Sample data for SyllabusUnit
        SyllabusUnitRequest syllabusUnit1 = SyllabusUnitRequest.builder()
                .name("Unit 1")
                .unitNo(1)
                .build();

        SyllabusUnitRequest syllabusUnit2 = SyllabusUnitRequest.builder()
                .name("Unit 2")
                .unitNo(2)
                .build();

        List<SyllabusUnitRequest> syllabusUnits = new ArrayList<>();
        syllabusUnits.add(syllabusUnit1);
        syllabusUnits.add(syllabusUnit2);

        // Creating Syllabus object
        SyllabusRequest syllabus = SyllabusRequest.builder()
                .name("Test Syllabus")
                .code("TS101")
                .version("1.0")
                .attendeeNumber(30)
                .technicalRequirement("Basic computer knowledge")
                .courseObjective("To teach the basics of programming")
                .status(SyllabusStatus.ACTIVE)
                .isTemplate(false)
                .assessmentScheme(assessmentScheme)
                .syllabusDays(syllabusDays)
                .deliveryPrinciple(deliveryPrinciple)
                .build();

        return syllabus;
    }
}

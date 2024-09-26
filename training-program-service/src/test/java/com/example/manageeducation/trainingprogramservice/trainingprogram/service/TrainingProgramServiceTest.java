package com.example.manageeducation.trainingprogramservice.trainingprogram.service;

import com.example.manageeducation.trainingprogramservice.dto.RequestForListOfTrainingProgram;
import com.example.manageeducation.trainingprogramservice.dto.Syllabus;
import com.example.manageeducation.trainingprogramservice.dto.TrainingProgramsResponse;
import com.example.manageeducation.trainingprogramservice.enums.SyllabusStatus;
import com.example.manageeducation.trainingprogramservice.enums.TrainingProgramStatus;
import com.example.manageeducation.trainingprogramservice.jdbc.TrainingProgramJdbc;
import com.example.manageeducation.trainingprogramservice.model.ResponseObject;
import com.example.manageeducation.trainingprogramservice.model.TrainingProgram;
import com.example.manageeducation.trainingprogramservice.repository.TrainingProgramRepository;
import com.example.manageeducation.trainingprogramservice.service.TrainingProgramService;
import com.example.manageeducation.trainingprogramservice.utils.TrainingProgramServiceUtils;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@Log4j2
public class TrainingProgramServiceTest {

    @MockBean
    private TrainingProgramRepository trainingProgramRepository;

    @MockBean
    private TrainingProgramJdbc trainingProgramJdbc;

    @Autowired
    private TrainingProgramServiceUtils trainingProgramServiceUtils;

    @Autowired
    private TrainingProgramService trainingProgramService;

    @Autowired
    private ModelMapper modelMapper;

    // set up user ID
    private final UUID userA = UUID.randomUUID();
    private final UUID userB = UUID.randomUUID();
    private final UUID userC = UUID.randomUUID(); // non
    // set up program ID
    private final UUID program1 = UUID.randomUUID();
    private final UUID program2 = UUID.randomUUID();
    private final UUID program3 = UUID.randomUUID();
    private final UUID program4 = UUID.randomUUID();
    private final UUID programEdit = UUID.randomUUID();
    private final UUID programNotFound = UUID.randomUUID();
    // set up syllabus ID
    private final UUID syllabus1 = UUID.randomUUID();
    private final UUID syllabus2 = UUID.randomUUID();
    private final UUID syllabus3 = UUID.randomUUID();
    private final UUID syllabus4 = UUID.randomUUID();
    private final UUID syllabus5 = UUID.randomUUID();
    private final UUID syllabusNotFound = UUID.randomUUID();

    List<TrainingProgram> programs = new ArrayList<>(List.of(

            TrainingProgram.builder().id(program1).name("Fresher Java Developer")
                    .status(TrainingProgramStatus.INACTIVE).version(null).createdBy(userA).build(),

            TrainingProgram.builder().id(program2).name("Fresher Android Developer")
                    .status(TrainingProgramStatus.ACTIVE).version(null).createdBy(userA).build(),

            TrainingProgram.builder().id(program3).name("Fullstack Java Web Developer")
                    .status(TrainingProgramStatus.DRAFT).version(null).createdBy(userA).build(),

            TrainingProgram.builder().id(program4)
                    .name("Professional Mobile Programing Developer (Android platform)")
                    .status(TrainingProgramStatus.ACTIVE).version("1.0").createdBy(userA).build()

    ));

    @BeforeEach
    void setUp() throws Exception {
        // set up List program , size = 4
        List<TrainingProgram> programs = new ArrayList<>(List.of(

                TrainingProgram.builder().id(program1).name("Fresher Java Developer")
                        .status(TrainingProgramStatus.INACTIVE).version(null).createdBy(userA).build(),

                TrainingProgram.builder().id(program2).name("Fresher Android Developer")
                        .status(TrainingProgramStatus.ACTIVE).version(null).createdBy(userA).build(),

                TrainingProgram.builder().id(program3).name("Fullstack Java Web Developer")
                        .status(TrainingProgramStatus.DRAFT).version(null).createdBy(userA).build(),

                TrainingProgram.builder().id(program4)
                        .name("Professional Mobile Programing Developer (Android platform)")
                        .status(TrainingProgramStatus.ACTIVE).version("1.0").createdBy(userA).build()

        ));

        // set up List Syllabus, size = 5
        List<Syllabus> syllabuses = new ArrayList<>(List.of(

                Syllabus.builder().id(syllabus1).name(".Net Basic Program").code("NBP").createdBy(userB).days(2)
                        .hours(10).status(SyllabusStatus.ACTIVE).updatedBy(userB).version("1.0").build(),

                Syllabus.builder().id(syllabus2).name("Azure DevOps").code("ADO").createdBy(userB).days(2).hours(10)
                        .status(SyllabusStatus.ACTIVE).updatedBy(userB).version("1.0").build(),

                Syllabus.builder().id(syllabus3).name("JUnit Testing").code("FSJ").createdBy(userB).days(2).hours(10)
                        .status(SyllabusStatus.ACTIVE).updatedBy(userB).version("1.0").build(),

                Syllabus.builder().id(syllabus4).name("JUnit Testing").code("JUT").createdBy(userB).days(2).hours(10)
                        .status(SyllabusStatus.DEACTIVE).updatedBy(userB).version("1.1").build(),

                Syllabus.builder().id(syllabus5).name("JUnit Testing").code("JUT").createdBy(userB).days(2).hours(10)
                        .status(SyllabusStatus.ACTIVE).updatedBy(userB).version("3.0").build()

        ));

        // set up Optional for Program
        Optional<TrainingProgram> oProgram1 = Optional.ofNullable(programs.get(0));
        Optional<TrainingProgram> oProgram2 = Optional.ofNullable(programs.get(1));
        Optional<TrainingProgram> oProgram3 = Optional.ofNullable(programs.get(2));
        Optional<TrainingProgram> oProgram4 = Optional.ofNullable(programs.get(3));

        // Mockito to Program
        Mockito.when(trainingProgramRepository.findById(program1)).thenReturn(oProgram1);
        Mockito.when(trainingProgramRepository.findById(program2)).thenReturn(oProgram2);
        Mockito.when(trainingProgramRepository.findById(program3)).thenReturn(oProgram3);
        Mockito.when(trainingProgramRepository.findById(program4)).thenReturn(oProgram4);

        // set up Optional for Syllabus
        Optional<Syllabus> oSyllabus1 = Optional.ofNullable(syllabuses.get(0));
        Optional<Syllabus> oSyllabus2 = Optional.ofNullable(syllabuses.get(1));
        Optional<Syllabus> oSyllabus3 = Optional.ofNullable(syllabuses.get(2));
        Optional<Syllabus> oSyllabus4 = Optional.ofNullable(syllabuses.get(3));
        Optional<Syllabus> oSyllabus5 = Optional.ofNullable(syllabuses.get(4));


    }

    @Test
    @DisplayName("Create training program with expected successfully")
    public void createTrainingProgramWithExpectedSuccessfully() {
    }

    @Test
    @DisplayName("Get training program by keyword empty and status is null and date empty not sort")
    public void getTrainingProgramByKeywordEmptyAndStatusNullAndDateEmptyNotSort() {
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(new String[0], "", "", null, 1, 10, null, null);
        Page<TrainingProgram> results = new PageImpl<>(programs);
        Mockito.when(trainingProgramRepository.findAllTrainingProgram(any(Pageable.class))).thenReturn(results);

        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("Get training program by keyword empty and date empty not sort")
    public void getSyllabusByKeywordEmptyAndDateEmptyNotSort() {
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(new String[0], "", "", TrainingProgramStatus.ACTIVE, 1, 10, null, null);
        List<TrainingProgram> activePrograms = programs.stream()
                .filter(program -> program.getStatus() == TrainingProgramStatus.ACTIVE)
                .collect(Collectors.toList());
        Page<TrainingProgram> results = new PageImpl<>(activePrograms);
        Mockito.when(trainingProgramRepository.findAllTrainingProgramWithStatus(any(Pageable.class), eq(request.getStatus())))
                .thenReturn(results);


        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Get training program by keyword empty and status is null and date empty sort")
    public void getTrainingProgramByKeywordEmptyAndStatusIsNullAndDateEmptySort() {
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(new String[0], "", "", null, 1, 10, "NAME", "DESC");
        String sql = trainingProgramServiceUtils.getSQLForSortingAllTrainingPrograms(request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get training program by keyword empty and date empty sort")
    public void getTrainingProgramByKeywordEmptyAndDateEmptySort() {
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(new String[0], "", "", TrainingProgramStatus.INACTIVE, 1, 10, "NAME", "DESC");
        String sql = trainingProgramServiceUtils.getSQLForSortingAllTrainingPrograms(request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get training program by keyword name and status is null not sort name")
    public void getTrainingProgramByKeywordNameAndStatusIsNullNotSort() {
        String[] keywords = {"reshe"};
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(keywords,"","",null, 1, 10, "NAME", "DESC");

        System.out.println("Keyword: " + keywords[0]);
        System.out.println("Status: " + request.getStatus());


        String sql = trainingProgramServiceUtils.getSQLForSearchingByKeywordsForSuggestions(
                keywords[0], request.getStatus(), request.getPage()-1, request.getSize()
        );
        List<TrainingProgram> filteredPrograms = programs.stream()
                .filter(program -> program.getName().toLowerCase().contains("reshe"))
                .collect(Collectors.toList());

        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(filteredPrograms);


        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList.size())
                .isEqualTo(2);
    }

    @Test
    @DisplayName("Get training program by keyword name and not sort name")
    public void getTrainingProgramByKeywordNameNotSort() {
        String[] keywords = {"Unit"};
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(keywords,"","",TrainingProgramStatus.DRAFT, 1, 10, "NAME", "DESC");
        String sql = trainingProgramServiceUtils.getSQLForSearchingByKeywordsForSuggestions(keywords[0],null,request.getPage(), request.getSize());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get training program by keyword name and status is null sort name")
    public void getTrainingProgramByKeywordNameAndStatusIsNullSort() {
        String[] keywords = {"Unit"};
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(keywords,"","",null, 1, 10, "NAME", "DESC");
        String sql = trainingProgramServiceUtils.getSQLForSearchingByKeywordsForSuggestionsAndSorting(keywords[0],request.getStatus(),request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get training program by keyword name sort name")
    public void getTrainingProgramByKeywordNameSort() {
        String[] keywords = {"Unit"};
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(keywords,"","",TrainingProgramStatus.ACTIVE, 1, 10, "NAME", "DESC");
        String sql = trainingProgramServiceUtils.getSQLForSearchingByKeywordsForSuggestionsAndSorting(keywords[0],request.getStatus(),request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get training program by date and status is null not sort")
    public void getSyllabusByDateAndStatusIsNullAndNotSort() {
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(new String[0],"13/09/2021", "24/11/2024",null, 1, 10, null, null);
        String sql = trainingProgramServiceUtils.getSQLForSearchingByCreatedDateAndNotSort(request.getStartDate(), request.getEndDate(),request.getStatus(),request.getPage(), request.getSize());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get training program by date and not sort")
    public void getSyllabusByDateAndNotSort() {
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(new String[0],"13/09/2021", "24/11/2024",TrainingProgramStatus.DRAFT, 1, 10, null, null);
        String sql = trainingProgramServiceUtils.getSQLForSearchingByCreatedDateAndNotSort(request.getStartDate(), request.getEndDate(),request.getStatus(),request.getPage(), request.getSize());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get training program by date and status is null and sort")
    public void getTrainingProgramByDateAndStatusIsNullAndSort() {
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(new String[0],"","",null, 1, 10, "NAME", "DESC");
        String sql = trainingProgramServiceUtils.getSQLForSearchingByCreatedDateAndSort(request.getStartDate(), request.getEndDate(),request.getStatus(),request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get training program by date and sort")
    public void getTrainingProgramByDateAndSort() {
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(new String[0],"","",TrainingProgramStatus.ACTIVE, 1, 10, "NAME", "DESC");
        String sql = trainingProgramServiceUtils.getSQLForSearchingByCreatedDateAndSort(request.getStartDate(), request.getEndDate(),request.getStatus(),request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get training program by keyword and status is null and date not sort")
    public void getTrainingProgramByKeywordAndStatusIsNullAndDateNotSort() {
        String[] keywords = {"Unit"};
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(keywords,"13/09/2021", "24/11/2024",null, 1, 10, "NAME", "DESC");
        String sql = trainingProgramServiceUtils.getSQLForSearchingByKeywordAndCreatedDateAndNotSort(keywords[0], request.getStartDate(), request.getEndDate(),request.getStatus(),request.getPage(), request.getSize());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get training program by keyword and date not sort")
    public void getTrainingProgramByKeywordAndDateNotSort() {
        String[] keywords = {"Unit"};
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(keywords,"13/09/2021", "24/11/2024",TrainingProgramStatus.INACTIVE, 1, 10, "NAME", "DESC");
        String sql = trainingProgramServiceUtils.getSQLForSearchingByKeywordAndCreatedDateAndNotSort(keywords[0], request.getStartDate(), request.getEndDate(),request.getStatus(),request.getPage(), request.getSize());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get training program by keyword and status is null and date and sort")
    public void getTrainingProgramByKeywordAndStatusIsNullAndDateSort() {
        String[] keywords = {"Unit"};
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(keywords,"13/09/2021", "24/11/2024",null, 1, 10, "NAME", "DESC");
        String sql = trainingProgramServiceUtils.getSQLForSearchingByKeywordAndCreatedDateAndSort(keywords[0], request.getStartDate(), request.getEndDate(),request.getStatus(),request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get training program by keyword and date and sort")
    public void getTrainingProgramByKeywordAndDateSort() {
        String[] keywords = {"Unit"};
        RequestForListOfTrainingProgram request = new RequestForListOfTrainingProgram(keywords,"13/09/2021", "24/11/2024",TrainingProgramStatus.ACTIVE, 1, 10, "NAME", "DESC");
        String sql = trainingProgramServiceUtils.getSQLForSearchingByKeywordAndCreatedDateAndSort(keywords[0], request.getStartDate(), request.getEndDate(),request.getStatus(),request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType());
        Mockito.when(trainingProgramJdbc.getTrainingPrograms(sql)).thenReturn(programs);

        List<TrainingProgramsResponse> expectedList = programs.stream()
                .map(syllabus -> modelMapper.map(syllabus, TrainingProgramsResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = trainingProgramService.getAllTrainingPrograms(request);
        List<TrainingProgramsResponse> actualList = (List<TrainingProgramsResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }
}

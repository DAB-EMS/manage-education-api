package com.example.manageeducation.syllabusservice.mapper;

import com.example.manageeducation.syllabusservice.dto.response.OutputStandardResponse;
import com.example.manageeducation.syllabusservice.dto.response.ViewSyllabusResponse;
import com.example.manageeducation.syllabusservice.model.Syllabus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class SyllabusRowMapper implements RowMapper<ViewSyllabusResponse> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public ViewSyllabusResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            List<OutputStandardResponse> outputStandardList = getOutputStandards(rs);
            return ViewSyllabusResponse.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .name(rs.getString("name"))
                    .code(rs.getString("code"))
                    .createOn(dateFormat.parse(rs.getString("createOn")))
                    .createBy(rs.getString("createBy"))
                    .duration(rs.getInt("duration"))
                    .outputStandard(outputStandardList)
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private List<OutputStandardResponse> getOutputStandards(ResultSet rs) throws SQLException {
        List<OutputStandardResponse> outputStandards = new ArrayList<>();
        while (rs.next()) {
            OutputStandardResponse os = new OutputStandardResponse();
            os.setId(UUID.fromString(rs.getString("output_standard_id")));
            os.setName(rs.getString("output_standard_name"));
            os.setCode(rs.getString("output_standard_code"));
            os.setDescription(rs.getString("output_standard_description"));
            outputStandards.add(os);
        }
        return outputStandards;
    }
}

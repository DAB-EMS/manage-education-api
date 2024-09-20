package com.example.manageeducation.syllabusservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SyllabusServiceUtils {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public String getSQLForSortingAllSyllabus(int page, int size, String sortBy, String sortType) {
        return "SELECT s.id, s.name, s.code, s.created_date, s.created_by, s.duration, "
                + "os.id as output_standard_id, os.name as output_standard_name, os.code as output_standard_code, os.description as output_standard_description "
                + "FROM syllabus s "
                + "JOIN syllabus_day sd ON s.id = sd.syllabus_id "
                + "JOIN syllabus_unit su ON sd.id = su.syllabus_day_id "
                + "JOIN syllabus_unit_chapter suc ON su.id = suc.unit_id "
                + "JOIN output_standard os ON suc.output_standard_id = os.id "
                + "WHERE s.status = 0"
                + "ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                + getLimitAndOffsetValues(page, size);
    }

    public String getSQLForSearchingByKeywordsForSuggestions(int page, int size, String keyword) {
        return "SELECT s.id, s.name, s.code, s.created_date, s.created_by, s.duration, "
                + "os.id as output_standard_id, os.name as output_standard_name, os.code as output_standard_code, os.description as output_standard_description "
                + "FROM syllabus s "
                + "JOIN syllabus_day sd ON s.id = sd.syllabus_id "
                + "JOIN syllabus_unit su ON sd.id = su.syllabus_day_id "
                + "JOIN syllabus_unit_chapter suc ON su.id = suc.unit_id "
                + "JOIN output_standard os ON suc.output_standard_id = os.id "
                + "WHERE s.status = 0 AND "
                + "(s.full_name LIKE '%" + keyword + "%' OR s.gender LIKE '%" + keyword + "%') "
                + getLimitAndOffsetValues(page, size);
    }

    public String getSQLForSearchingByKeywordsForSuggestionsAndSorting(int page, int size, String sortBy, String sortType, String keyword) {
        return "SELECT s.id, s.name, s.code, s.created_date, s.created_by, s.duration, "
                + "os.id as output_standard_id, os.name as output_standard_name, os.code as output_standard_code, os.description as output_standard_description "
                + "FROM syllabus s "
                + "JOIN syllabus_day sd ON s.id = sd.syllabus_id "
                + "JOIN syllabus_unit su ON sd.id = su.syllabus_day_id "
                + "JOIN syllabus_unit_chapter suc ON su.id = suc.unit_id "
                + "JOIN output_standard os ON suc.output_standard_id = os.id "
                + "WHERE s.status = 0 AND "
                + "(s.full_name LIKE '%" + keyword + "%' OR s.gender LIKE '%" + keyword + "%') "
                + " ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                + getLimitAndOffsetValues(page, size);
    }

    public String getLimitAndOffsetValues(int page, int size) {
        return " LIMIT " + size + " OFFSET " + (page * size);
    }
}

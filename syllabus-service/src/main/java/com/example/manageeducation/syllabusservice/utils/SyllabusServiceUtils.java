package com.example.manageeducation.syllabusservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SyllabusServiceUtils {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public String getSQLForSortingAllSyllabus(int page, int size, String sortBy, String sortType) {
        return "SELECT * FROM customer s LEFT JOIN role r ON s.role_id = r.id"
                + " ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                + getLimitAndOffsetValues(page, size);
    }

    public String getSQLForSearchingByKeywordsForSuggestions(int page, int size, String keyword) {
        return "SELECT * FROM customer s WHERE "
                + "(s.full_name LIKE '%" + keyword + "%' OR s.gender LIKE '%" + keyword + "%' OR s.email LIKE '%"
                + keyword + "%')" + getLimitAndOffsetValues(page, size);
    }

    public String getSQLForSearchingByKeywordsForSuggestionsAndSorting(int page, int size, String sortBy, String sortType, String keyword) {
        return "SELECT * FROM customer s WHERE "
                + "(s.full_name LIKE '%" + keyword + "%' OR s.gender LIKE '%" + keyword + "%' OR s.email LIKE '%"
                + " ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                + keyword + "%')" + getLimitAndOffsetValues(page, size);
    }

    public String getLimitAndOffsetValues(int page, int size) {
        return " LIMIT " + size + " OFFSET " + (page * size);
    }
}

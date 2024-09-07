package com.example.manageeducation.userservice.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceUtils {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public String getSQLForSortingAllCustomers(int page, int size, String sortBy, String sortType) {
        return "SELECT * FROM customer s LEFT JOIN role r ON s.role_id = r.id"
                + " ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                + getLimitAndOffsetValues(page, size);
    }

    public String getSQLForSearchingByKeywords(int page, int size, String keyword) {
        return "SELECT * FROM customer_spring_data s WHERE "
                + "(s.status = 2 OR s.status = 3 OR s.status = 4) AND "
                + "(s.name = '" + keyword + "' OR s.age = " + keyword + " OR s.address = '"
                + keyword + "')" + getLimitAndOffsetValues(page, size);
    }

    public String getSQLForSearchingByKeywordsForSuggestions(int page, int size, String keyword) {
        return "SELECT * FROM customer_spring_data s WHERE "
                + "(s.name LIKE '%" + keyword + "%' OR s.age LIKE '%" + keyword + "%' OR s.address LIKE '%"
                + keyword + "%')" + getLimitAndOffsetValues(page, size);
    }

    public String getSQLForSearchingByKeywordsForSuggestionsAndSorting(int page, int size, String sortBy, String sortType, String keyword) {
        return "SELECT * FROM customer_spring_data s WHERE "
                + "(s.name LIKE '%" + keyword + "%' OR s.age LIKE '%" + keyword + "%' OR s.address LIKE '%"
                + " ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                + keyword + "%')" + getLimitAndOffsetValues(page, size);
    }

    public String getLimitAndOffsetValues(int page, int size) {
        return " LIMIT " + size + " OFFSET " + (page * size);
    }
}

package com.example.manageeducation.userservice.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceUtils {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public String getSQLForSortingAllCustomers(int page, int size, String sortBy, String sortType) {
        return "SELECT s.email, s.fullName, s.avatar, s.birthday, s.gender, s.level, r.name , s.status FROM customer s LEFT JOIN role r ON s.role_id = r.id"
                + " ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                + getLimitAndOffsetValues(page, size);
    }

    public String getSQLForSearchingByKeywords(int page, int size, String keyword) {
        return "SELECT * FROM customer_spring_data s WHERE "
                + "s.status = 1 AND "
                + "(s.email = '" + keyword + "' OR s.fullName = " + keyword + " OR s.gender = '"
                + keyword + "')" + getLimitAndOffsetValues(page, size);
    }

    public String getSQLForSearchingByKeywordsForSuggestions(int page, int size, String keyword) {
        return "SELECT * FROM customer s WHERE "
                + "s.status = 1 AND "
                + "(s.email LIKE '%" + keyword + "%' OR s.fullName LIKE '%" + keyword + "%' OR s.gender LIKE '%" + keyword + "%' OR s.role LIKE '%" + keyword + "%' OR s.status LIKE '%" + keyword + "%'  OR s.level LIKE '%" + keyword + "%' OR s.gender LIKE '%"
                + keyword + "%')" + getLimitAndOffsetValues(page, size);
    }

    public String getSQLForSearchingByKeywordsForSuggestionsAndSorting(int page, int size, String sortBy, String sortType, String keyword) {
        return "SELECT s.email, s.fullName, s.avatar, s.birthday, s.gender, s.level, r.name , s.status FROM customer s LEFT JOIN role r ON s.role_id = r.id "
                + "WHERE s.status = 1 AND "
                + "(s.email LIKE '%" + keyword + "%' OR s.fullName LIKE '%" + keyword + "%' OR s.gender LIKE '%" + keyword + "%' OR s.role LIKE '%" + keyword + "%' OR s.status LIKE '%" + keyword + "%'  OR s.level LIKE '%" + keyword + "%' OR s.gender LIKE '%"
                + keyword + "%')"
                + " ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                + getLimitAndOffsetValues(page, size);
    }

    public String getLimitAndOffsetValues(int page, int size) {
        return " LIMIT " + size + " OFFSET " + (page * size);
    }
}

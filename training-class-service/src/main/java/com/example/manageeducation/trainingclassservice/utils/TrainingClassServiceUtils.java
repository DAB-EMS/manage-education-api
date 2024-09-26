package com.example.manageeducation.trainingclassservice.utils;

import com.example.manageeducation.trainingclassservice.enums.TrainingClassStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TrainingClassServiceUtils {

    public String getSQLForAllTrainingClasses(String[] keywords, String[] locations, String[] attends, String fsu,
                                              String strStartDate, String strEndDate, TrainingClassStatus[] statuses, int page, int size,
                                              String sortBy, String sortType) {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT s.id, s.name, s.courseCode, s.createdBy, s.createdDate, s.duration, ")
                .append("sd.id as attendeeLevelId, sd.name as attendeeLevelName, ")
                .append("su.id as classLocationId, su.name as classLocationName, ")
                .append("suc.id as fsuId, suc.name as fsuName ")
                .append("FROM training_class s ")
                .append("JOIN attendee_level sd ON s.attendee_level_id = sd.id ")
                .append("JOIN class_location su ON s.class_location_id = su.id ")
                .append("JOIN fsu suc ON s.fsu_id = suc.id ")
                .append("WHERE s.status != 1 ");

        if (keywords.length > 0) {
            sqlBuilder.append("AND (");
            for (int i = 0; i < keywords.length; i++) {
                if (i > 0) {
                    sqlBuilder.append(" OR ");
                }
                sqlBuilder.append("(s.name LIKE '%").append(keywords[i]).append("%' OR s.course_code LIKE '%")
                        .append(keywords[i]).append("%')");
            }
            sqlBuilder.append(") ");
        }

        if (locations.length > 0) {
            sqlBuilder.append("AND (");
            for (int i = 0; i < locations.length; i++) {
                if (i > 0) {
                    sqlBuilder.append(" OR ");
                }
                sqlBuilder.append("su.name = '").append(locations[i]).append("'");
            }
            sqlBuilder.append(") ");
        }

        if (attends.length > 0) {
            sqlBuilder.append("AND (");
            for (int i = 0; i < attends.length; i++) {
                if (i > 0) {
                    sqlBuilder.append(" OR ");
                }
                sqlBuilder.append("sd.name = '").append(attends[i]).append("'");
            }
            sqlBuilder.append(") ");
        }

        if (!fsu.isEmpty()) {
            sqlBuilder.append("AND suc.name LIKE '%").append(fsu).append("%' ");
        }

        if (statuses.length > 0) {
            sqlBuilder.append("AND (");
            for (int i = 0; i < statuses.length; i++) {
                if (i > 0) {
                    sqlBuilder.append(" OR ");
                }
                sqlBuilder.append("s.status = ").append(statuses[i].ordinal());
            }
            sqlBuilder.append(") ");
        }

        if (!"".equalsIgnoreCase(strStartDate) && !"".equals(strEndDate)) {
            sqlBuilder.append("AND (s.created_date BETWEEN '").append(strStartDate).append("' AND '").append(strEndDate).append("') ");
        }

        if (!"".equalsIgnoreCase(sortBy) && !"".equalsIgnoreCase(sortType)) {
            sqlBuilder.append("ORDER BY s.").append(sortBy.toLowerCase()).append(" ").append(sortType).append(" ");
        }

        sqlBuilder.append(getLimitAndOffsetValues(page, size));

        return sqlBuilder.toString();
    }

    public String getLimitAndOffsetValues(int page, int size) {
        return " LIMIT " + size + " OFFSET " + (page * size);
    }
}

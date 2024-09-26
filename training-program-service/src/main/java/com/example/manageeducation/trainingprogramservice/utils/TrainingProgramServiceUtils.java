package com.example.manageeducation.trainingprogramservice.utils;

import com.example.manageeducation.trainingprogramservice.enums.TrainingProgramStatus;
import org.springframework.stereotype.Service;

@Service
public class TrainingProgramServiceUtils {


    public String getSQLForSortingAllTrainingPrograms(int page, int size, String sortBy, String sortType) {
        return "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                + "FROM training_program s "
                + "WHERE NOT s.status = 2 "
                + "ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                + getLimitAndOffsetValues(page, size);
    }

    public String getSQLForSearchingByKeywordsForSuggestions(String keyword, TrainingProgramStatus status, int page, int size) {
        if(status==null){
            return "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                    + "FROM training_program s "
                    + "WHERE s.status != 2 AND "
                    + "s.name LIKE '%" + keyword + "%' "
                    + getLimitAndOffsetValues(page, size);
        }else{
            return "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                    + "FROM training_program s "
                    + "WHERE s.status != 2 AND "
                    + "s.status = " + status + " AND s.name LIKE '%" + keyword + "%' "
                    + getLimitAndOffsetValues(page, size);
        }

    }

    public String getSQLForSearchingByKeywordsForSuggestionsAndSorting(String keyword, TrainingProgramStatus status, int page, int size, String sortBy, String sortType) {
        if(status==null){
            return "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                    + "FROM training_program s "
                    + "WHERE s.status != 2 AND "
                    + "s.name LIKE '%" + keyword + "%' "
                    + "ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                    + getLimitAndOffsetValues(page, size);
        }else{
            return "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                    + "FROM training_program s "
                    + "WHERE s.status =! 2 AND "
                    + "s.status = " + status + " AND s.name LIKE '%" + keyword + "%' "
                    + "ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                    + getLimitAndOffsetValues(page, size);
        }
    }

    public String getSQLForSearchingByCreatedDateAndSort(String startDate, String endDate,TrainingProgramStatus status, int page, int size,
                                                         String sortBy, String sortType) {
        String[] startDateArr = startDate.split("/");
        String[] endDateArr = endDate.split("/");
        String strStartDate = startDateArr[2] + "-" + startDateArr[1] + "-" + startDateArr[0] + " 00:00:00";
        String strEndDate = endDateArr[2] + "-" + endDateArr[1] + "-" + endDateArr[0] + " 00:00:00";

        String sqlWithStatus = "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                + "FROM training_program s "
                + "WHERE NOT s.status = 2 AND "
                + "s.status = " + status + " AND (s.created_date BETWEEN '" + strStartDate + "' AND '" + strEndDate + "')"
                + " ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                + getLimitAndOffsetValues(page, size);

        String sqlWithNotStatus = "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                + "FROM training_program s "
                + "WHERE NOT s.status = 2 "
                + "AND (s.created_date BETWEEN '" + strStartDate + "' AND '" + strEndDate + "')"
                + " ORDER BY s." + sortBy + " " + sortType
                + getLimitAndOffsetValues(page, size);

        if(status==null){
            return sqlWithNotStatus;
        }else{
            return sqlWithStatus;
        }
    }

    public String getSQLForSearchingByCreatedDateAndNotSort(String startDate, String endDate, TrainingProgramStatus status, int page, int size) {
        String[] startDateArr = startDate.split("/");
        String[] endDateArr = endDate.split("/");
        String strStartDate = startDateArr[2] + "-" + startDateArr[1] + "-" + startDateArr[0] + " 00:00:00";
        String strEndDate = endDateArr[2] + "-" + endDateArr[1] + "-" + endDateArr[0] + " 00:00:00";

        String sqlWithStatus = "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                + "FROM training_program s "
                + "WHERE NOT s.status = 2 AND "
                + "s.status = " + status + " AND (s.created_date BETWEEN '" + strStartDate + "' AND '" + strEndDate + "') "
                + getLimitAndOffsetValues(page, size);

        String sqlWithNotStatus = "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                + "FROM training_program s "
                + "WHERE NOT s.status = 2 "
                + "AND (s.created_date BETWEEN '" + strStartDate + "' AND '" + strEndDate + "') "
                + getLimitAndOffsetValues(page, size);

        if(status==null){
            return sqlWithNotStatus;
        }else{
            return sqlWithStatus;
        }
    }

    public String getSQLForSearchingByKeywordAndCreatedDateAndSort(String keyword, String startDate, String endDate, TrainingProgramStatus status, int page, int size,
                                                                   String sortBy, String sortType) {
        String[] startDateArr = startDate.split("/");
        String[] endDateArr = endDate.split("/");
        String strStartDate = startDateArr[2] + "-" + startDateArr[1] + "-" + startDateArr[0] + " 00:00:00";
        String strEndDate = endDateArr[2] + "-" + endDateArr[1] + "-" + endDateArr[0] + " 00:00:00";

        String sqlWithStatus = "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                + "FROM training_program s "
                + "WHERE NOT s.status = 2 AND "
                + "(s.name LIKE '%" + keyword + "%' "
                + "AND s.status = " + status + " AND (s.created_date BETWEEN '" + strStartDate + "' AND '" + strEndDate + "')"
                + " ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                + getLimitAndOffsetValues(page, size);

        String sqlWithNotStatus = "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                + "FROM training_program s "
                + "WHERE NOT s.status = 2 AND "
                + "(s.name LIKE '%" + keyword + "%' "
                + "AND (s.created_date BETWEEN '" + strStartDate + "' AND '" + strEndDate + "')"
                + " ORDER BY s." + sortBy.toLowerCase() + " " + sortType
                + getLimitAndOffsetValues(page, size);

        if(status==null){
            return sqlWithNotStatus;
        }else{
            return sqlWithStatus;
        }

    }

    public String getSQLForSearchingByKeywordAndCreatedDateAndNotSort(String keyword, String startDate, String endDate, TrainingProgramStatus status, int page, int size) {
        String[] startDateArr = startDate.split("/");
        String[] endDateArr = endDate.split("/");
        String strStartDate = startDateArr[2] + "-" + startDateArr[1] + "-" + startDateArr[0] + " 00:00:00";
        String strEndDate = endDateArr[2] + "-" + endDateArr[1] + "-" + endDateArr[0] + " 00:00:00";

        String sqlWithStatus = "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                + "FROM training_program s "
                + "WHERE NOT s.status = 2 AND "
                + "(s.name LIKE '%" + keyword + "%' "
                + "AND s.status = " + status + " AND (s.created_date BETWEEN '" + strStartDate + "' AND '" + strEndDate + "')"
                + getLimitAndOffsetValues(page, size);

        String sqlWithNotStatus = "SELECT s.id, s.name, s.created_date, s.created_by, s.status "
                + "FROM training_program s "
                + "WHERE NOT s.status = 2 AND "
                + "(s.name LIKE '%" + keyword + "%' "
                + "AND (s.created_date BETWEEN '" + strStartDate + "' AND '" + strEndDate + "')"
                + getLimitAndOffsetValues(page, size);

        if(status==null){
            return sqlWithNotStatus;
        }else{
            return sqlWithStatus;
        }

    }

    public String getLimitAndOffsetValues(int page, int size) {
        return " LIMIT " + size + " OFFSET " + page;
    }

}

package com.example.manageeducation.trainingclassservice.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DataUtil {
    public static Date endDate(Date date, int months)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    public static Set<String> splitString(String s) {
        String[] result = s.split(",");
        Set<String> list=new HashSet<String>();

        for (String value:result
        ) {
            list.add(value.trim());
        }

        return list;
    }
}

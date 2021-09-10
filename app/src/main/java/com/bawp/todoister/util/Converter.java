package com.bawp.todoister.util;

import androidx.room.TypeConverter;

import com.bawp.todoister.model.Priority;

import java.util.Date;

public class Converter {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimeStamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static int priorityToString(Priority priority) {
        if(priority == Priority.HIGH)
            return 3;
        else if(priority == Priority.MEDIUM)
            return 2;
        else
            return 1;
    }

    @TypeConverter
    public static Priority stringToPriority(int value) {
        if(value == 3) return Priority.HIGH;
        else if(value == 2) return Priority.MEDIUM;
        else return Priority.LOW;
    }
}
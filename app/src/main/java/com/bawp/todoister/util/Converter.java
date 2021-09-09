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
    public static String priorityToString(Priority priority) {
        return priority == null ? null : priority.name();
    }

    @TypeConverter
    public static Priority stringToPriority(String value) {
        return value == null ? null : Priority.valueOf(value);
    }
}
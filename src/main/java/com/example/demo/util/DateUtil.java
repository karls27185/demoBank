package com.example.demo.util;

import java.time.format.DateTimeFormatter;

/**
 * Класс DateUtil предоставляет инструменты для работы с датами и временем.
 * */

public final class DateUtil {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
}

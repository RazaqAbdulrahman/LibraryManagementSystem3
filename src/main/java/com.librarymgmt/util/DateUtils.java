package com.librarymgmt.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;

    public static String toString(LocalDate d) {
        return d == null ? null : d.format(ISO);
    }

    public static LocalDate fromString(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return LocalDate.parse(s, ISO);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}

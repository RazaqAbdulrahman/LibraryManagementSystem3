package com.librarymgmt.validation;

import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE = Pattern.compile("^[+\\d][\\d\\s\\-()]{5,20}$");

    public static boolean isNonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE.matcher(phone).matches();
    }
}


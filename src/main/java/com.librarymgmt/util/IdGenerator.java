package com.librarymgmt.util;

import java.util.UUID;

public class IdGenerator {
    public static String generate(String prefix) {
        String id = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        return prefix + "-" + id;
    }
}

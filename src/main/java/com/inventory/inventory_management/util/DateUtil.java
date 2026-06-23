package com.inventory.inventory_management.util;

import java.time.LocalDateTime;

public final class DateUtil {

    private DateUtil() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}

package com.mape.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LocalDateTimeUtils {

  private static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT_PATTERN);

  private LocalDateTimeUtils() {
  }

  public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
    return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
  }

  public static String formatLocalDateTime(LocalDateTime localDateTime) {
    return localDateTime.format(formatter);
  }

  public static LocalDateTime parseLocalDateTime(String input, String pattern) {
    return LocalDateTime.parse(input, DateTimeFormatter.ofPattern(pattern));
  }

  public static LocalDateTime parseLocalDateTime(String input) {
    return LocalDateTime.parse(input, formatter);
  }
}


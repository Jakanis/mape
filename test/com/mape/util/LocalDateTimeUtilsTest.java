package com.mape.util;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;

public class LocalDateTimeUtilsTest {

  private static final String DATETIME_FORMAT_PATTERN_WITH_SECONDS = "yyyy-MM-dd HH:mm:ss";
  private static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
  private static final String DATE_ONE_WITH_SECONDS = "2019-06-06 16:44:55";
  private static final String DATE_ONE = "2019-06-06 16:44";
  private static final LocalDateTime DATETIME_ONE_WITH_SECONDS = LocalDateTime
      .parse(DATE_ONE_WITH_SECONDS, DateTimeFormatter.ofPattern(DATETIME_FORMAT_PATTERN_WITH_SECONDS));
  private static final LocalDateTime DATETIME_ONE = LocalDateTime
      .parse(DATE_ONE, DateTimeFormatter.ofPattern(DATETIME_FORMAT_PATTERN));


  @Test
  public void shouldFormatDate() {
    String actual = LocalDateTimeUtils.formatLocalDateTime(DATETIME_ONE);
    assertEquals(DATE_ONE, actual);
  }

  @Test
  public void shouldFormatDateWithCustomPattern() {
    String actual = LocalDateTimeUtils
        .formatLocalDateTime(DATETIME_ONE_WITH_SECONDS, DATETIME_FORMAT_PATTERN_WITH_SECONDS);
    assertEquals(DATE_ONE_WITH_SECONDS, actual);
  }

  @Test
  public void shouldParseDateTimeWithCustomPattern() {
    LocalDateTime actual = LocalDateTimeUtils
        .parseLocalDateTime(DATE_ONE_WITH_SECONDS, DATETIME_FORMAT_PATTERN_WITH_SECONDS);
    assertEquals(DATETIME_ONE_WITH_SECONDS, actual);
  }

  @Test
  public void shouldParseDateTime() {
    LocalDateTime actual = LocalDateTimeUtils.parseLocalDateTime(DATE_ONE);
    assertEquals(DATETIME_ONE, actual);
  }

}

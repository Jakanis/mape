package com.mape.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PaginationTest {

  public static final int TOTAL_ROWS = 52;
  public static final int ROWS_PER_PAGE = 5;
  private static final Pagination PAGINATION = new Pagination(TOTAL_ROWS, ROWS_PER_PAGE);

  @Test
  public void shouldReturnPageCount() {
    int actual = PAGINATION.getPagesNumber();
    int expected = (int) Math.ceil((float) TOTAL_ROWS / ROWS_PER_PAGE);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldReturnFirstPage() {
    int actual = PAGINATION.getPagesNumber();
    int expected = (int) Math.ceil((float) TOTAL_ROWS / ROWS_PER_PAGE);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldCorrectlyValidateCurrentPage() {
    int actual = PAGINATION.validateCurrentPage(-1);
    int expected = Pagination.DEFAULT_CURRENT_PAGE;
    assertEquals(expected, actual);

    actual = PAGINATION.validateCurrentPage(1);
    expected = 1;
    assertEquals(expected, actual);

    actual = PAGINATION.validateCurrentPage(12);
    expected = PAGINATION.getPagesNumber();
    assertEquals(expected, actual);

    actual = PAGINATION.validateCurrentPage(10);
    expected = 10;
    assertEquals(expected, actual);
  }

  @Test
  public void shouldCorrectlyReturnStartRow() {
    int actual = PAGINATION.getStartRow(1);
    int expected = 0;
    assertEquals(expected, actual);

    actual = PAGINATION.getStartRow(3);
    expected = 10;
    assertEquals(expected, actual);

    actual = PAGINATION.getStartRow(11);
    expected = 50;
    assertEquals(expected, actual);

    actual = PAGINATION.getStartRow(14);
    expected = 50;
    assertEquals(expected, actual);

    actual = PAGINATION.getStartRow(-1);
    expected = 0;
    assertEquals(expected, actual);
  }

  @Test
  public void shouldCorrectlyReturnRowCountForPage() {
    int actual = PAGINATION.getRowsForCurrentPage(1);
    int expected = ROWS_PER_PAGE;
    assertEquals(expected, actual);

    actual = PAGINATION.getRowsForCurrentPage(3);
    expected = ROWS_PER_PAGE;
    assertEquals(expected, actual);

    actual = PAGINATION.getRowsForCurrentPage(11);
    expected = 2;
    assertEquals(expected, actual);
  }


}

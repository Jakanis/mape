package com.mape.util;

public final class Pagination {

  public static final int DEFAULT_CURRENT_PAGE = 1;
  public static final int DEFAULT_RECORDS_PER_PAGE = 5;
  private int totalRows;
  private int rowsPerPage;

  public Pagination(int totalRows, int rowsPerPage) {
    this.totalRows = totalRows;
    this.rowsPerPage = rowsPerPage;
  }

  public int getPagesNumber() {
    return (int) Math.ceil((float) totalRows / rowsPerPage);
  }

  public int getStartRow(int currentPage) {
    if (currentPage < 1) {
      return 0;
    }
    if (currentPage > getPagesNumber()) {
      return getStartRow(getPagesNumber());
    }
    return (currentPage - 1) * rowsPerPage;
  }

  public int getRowsForCurrentPage(int currentPage) {
    int startRow = getStartRow(currentPage);
    if (startRow + rowsPerPage > totalRows) {
      return totalRows - startRow;
    } else {
      return rowsPerPage;
    }
  }

  public int validateCurrentPage(int currentPage) {
    if (currentPage < 1)
      return DEFAULT_CURRENT_PAGE;
    if (currentPage > getPagesNumber()) {
      return getPagesNumber();
    }
    return currentPage;
  }
}

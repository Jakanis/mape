package com.mape.controller;

import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.Conference;
import com.mape.service.ConferenceService;
import com.mape.service.impl.ConferenceServiceImpl;
import com.mape.util.Pagination;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import org.apache.commons.validator.routines.IntegerValidator;

@Log4j
@WebServlet(urlPatterns = "/conferences")
public class ConferencesServlet extends HttpServlet {

  private static final int DEFAULT_RECORDS_PER_PAGE = 5;
  private final ConferenceService conferenceService = new ConferenceServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getConferenceDao());

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      IntegerValidator integerValidator = IntegerValidator.getInstance();
      int recordsPerPage = Optional
          .ofNullable(integerValidator.validate(req.getParameter("recordsPerPage")))
          .orElse(DEFAULT_RECORDS_PER_PAGE);
      Pagination pagination = new Pagination(conferenceService.count(), recordsPerPage);
      int currentPage = pagination.validateCurrentPage(
          Optional.ofNullable(integerValidator.validate(req.getParameter("currentPage")))
              .orElse(Pagination.DEFAULT_CURRENT_PAGE));
      List<Conference> conferences = conferenceService
          .findPartOfAll(pagination.getStartRow(currentPage),
              pagination.getRowsForCurrentPage(currentPage));

      req.setAttribute("noOfPages", pagination.getPagesNumber());
      req.getSession().setAttribute("currentPage", currentPage);
      req.getSession().setAttribute("recordsPerPage", recordsPerPage);
      req.setAttribute("conferences", conferences);
      req.getRequestDispatcher("/jsp/conferences.jsp").forward(req, resp);
    } catch (Exception e) {
      log.error("Error displaying all conferences", e);
    }
  }
}

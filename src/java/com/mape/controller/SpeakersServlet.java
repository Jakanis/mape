package com.mape.controller;

import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.Speaker;
import com.mape.service.SpeakerService;
import com.mape.service.impl.SpeakerServiceImpl;
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
@WebServlet(urlPatterns = "/speakers")
public class SpeakersServlet extends HttpServlet {

  private final SpeakerService speakerService = new SpeakerServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getSpeakerDao());

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      IntegerValidator integerValidator = IntegerValidator.getInstance();
      int recordsPerPage = Optional.ofNullable(integerValidator.validate(req.getParameter("recordsPerPage")))
          .orElse(Pagination.DEFAULT_RECORDS_PER_PAGE);

      int count = speakerService.count();
      Pagination pagination = new Pagination(count, recordsPerPage);
      int currentPage = pagination.validateCurrentPage(
          Optional.ofNullable(integerValidator.validate(req.getParameter("currentPage")))
              .orElse(Pagination.DEFAULT_CURRENT_PAGE));
      List<Speaker> speakers = speakerService
          .findPartOfAll(pagination.getStartRow(currentPage),
              pagination.getRowsForCurrentPage(currentPage));

      req.setAttribute("noOfPages", pagination.getPagesNumber());
      req.getSession().setAttribute("currentPage", currentPage);
      req.setAttribute("users", speakers);
      req.getRequestDispatcher("/jsp/speakers.jsp").forward(req, resp);
    } catch (Exception e) {
      log.error("Error displaying all speakers", e);
    }
  }


}

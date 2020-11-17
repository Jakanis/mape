package com.mape.controller;

import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.User;
import com.mape.service.UserService;
import com.mape.service.impl.UserServiceImpl;
import com.mape.util.Pagination;
import java.util.List;
import java.util.Optional;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import org.apache.commons.validator.routines.IntegerValidator;

@Log4j
@WebServlet(urlPatterns = "/users")
public class UsersServlet extends HttpServlet {

  private final UserService userService = new UserServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getUserDao());

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    try {
      RequestDispatcher requestDispatcher;
      if ("Admin".equals(req.getSession().getAttribute("userRole"))) {
        IntegerValidator integerValidator = IntegerValidator.getInstance();
        int recordsPerPage = Optional.ofNullable(integerValidator.validate(req.getParameter("recordsPerPage")))
            .orElse(Pagination.DEFAULT_RECORDS_PER_PAGE);
        Pagination pagination = new Pagination(userService.count(), recordsPerPage);
        int currentPage = pagination.validateCurrentPage(
            Optional.ofNullable(integerValidator.validate(req.getParameter("currentPage")))
                .orElse(Pagination.DEFAULT_CURRENT_PAGE));
        List<User> users = userService
            .findPartOfAll(pagination.getStartRow(currentPage),
                pagination.getRowsForCurrentPage(currentPage));

        req.setAttribute("recordsPerPage", recordsPerPage);
        req.setAttribute("noOfPages", pagination.getPagesNumber());
        req.getSession().setAttribute("currentPage", currentPage);
        req.setAttribute("users", users);
        requestDispatcher = req.getRequestDispatcher("/jsp/users.jsp");
        requestDispatcher.forward(req, resp);
      } else {
        requestDispatcher = req.getRequestDispatcher("jsp/error.jsp");
        requestDispatcher.forward(req, resp);
      }
    } catch (Exception e) {
      log.error("Error displaying all users", e);
    }
  }
}

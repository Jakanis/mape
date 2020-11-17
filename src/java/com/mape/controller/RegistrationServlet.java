package com.mape.controller;

import com.mape.dao.exception.InconsistentDatabaseException;
import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.User;
import com.mape.service.RoleService;
import com.mape.service.UserService;
import com.mape.service.impl.RoleServiceImpl;
import com.mape.service.impl.UserServiceImpl;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;

@Log4j
@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {

  private static final UserService userService = new UserServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getUserDao());

  private static final RoleService roleService = new RoleServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getRoleDao());

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/registration.jsp");
    requestDispatcher.forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String email = req.getParameter("email");
    String firstName = req.getParameter("first_name");
    String lastName = req.getParameter("last_name");
    String login = req.getParameter("login");
    String password = req.getParameter("password");
    String passwordRepeat = req.getParameter("password-repeat");

    String validateResult = userService
        .validateRegistrationData(login, password, passwordRepeat, email);
    if (!validateResult.equals(UserServiceImpl.VALID_DATA)) {
      req.setAttribute("registration_error", validateResult);
      RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/registration.jsp");
      requestDispatcher.forward(req, resp);
    } else {
      try {
        userService.create(
            User.builder().withRole(roleService.findByName("User").orElseThrow(InconsistentDatabaseException::new))
                .withFirstName(firstName).withLastName(lastName).withEmail(email)
                .withLogin(login).withPassword(password).build());

        resp.sendRedirect("./login");
      } catch (Exception e) {
        log.error("Error register user " + login, e);
      }
    }
  }
}

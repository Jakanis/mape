package com.mape.controller;

import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.Conference;
import com.mape.entity.User;
import com.mape.service.ConferenceService;
import com.mape.service.UserService;
import com.mape.service.impl.ConferenceServiceImpl;
import com.mape.service.impl.UserServiceImpl;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

  private static final UserService userService = new UserServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getUserDao());

  private static final ConferenceService conferenceService = new ConferenceServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getConferenceDao());

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/login.jsp");
    requestDispatcher.forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String login = req.getParameter("login");
    String password = req.getParameter("password");
    String validateResult = userService.validateLoginData(login, password);
    if (!validateResult.equals(UserServiceImpl.VALID_DATA)) {
      req.setAttribute("login_error", validateResult);
      RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/login.jsp");
      requestDispatcher.forward(req, resp);
    } else {
      User user = userService.findByLogin(login).orElseThrow(ServletException::new);
      req.getSession().setAttribute("userLogin", user.getLogin());
      req.getSession().setAttribute("userId", user.getId());
      req.getSession().setAttribute("userRole", user.getRole().getName());
      req.getSession().setAttribute("userFirstName", user.getFirstName());
      req.getSession().setAttribute("userLastName", user.getLastName());
      List<Conference> registratedConferences = conferenceService.findByVisitor(user);
      if (!registratedConferences.isEmpty() && registratedConferences.get(0).getTime()
          .isBefore(LocalDateTime.now().plusDays(7L))) {
        req.getSession().setAttribute("notify", true);
      }
      resp.sendRedirect("./");
    }
  }
}

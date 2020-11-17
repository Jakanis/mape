package com.mape.controller;

import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.Conference;
import com.mape.entity.User;
import com.mape.service.ConferenceService;
import com.mape.service.SpeakerService;
import com.mape.service.UserService;
import com.mape.service.impl.ConferenceServiceImpl;
import com.mape.service.impl.SpeakerServiceImpl;
import com.mape.service.impl.UserServiceImpl;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.commons.validator.routines.LongValidator;

@Log4j
@WebServlet(urlPatterns = "/user")
public class UserServlet extends HttpServlet {

  public static final String USER_ID_ATTRIBUTE = "userId";
  public static final String AVERAGE_RATE_ATTRIBUTE = "averageRate";
  private static final ConferenceService conferenceService = new ConferenceServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getConferenceDao());
  private final UserService userService = new UserServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getUserDao());
  private final SpeakerService speakerService = new SpeakerServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getSpeakerDao());
  private final LongValidator longValidator = LongValidator.getInstance();
  private final IntegerValidator integerValidator = IntegerValidator.getInstance();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      Optional<Long> sessionUserID = Optional.empty();
      Optional<Object> sessionUserAttribute = Optional
          .ofNullable(req.getSession().getAttribute(USER_ID_ATTRIBUTE));
      if (sessionUserAttribute.isPresent()) {
        sessionUserID = Optional
            .ofNullable(longValidator.validate(sessionUserAttribute.get().toString()));
      }

      Optional<Long> userId = Optional.ofNullable(longValidator.validate(req.getParameter("id")));
      Optional<User> optionalUser = Optional.empty();
      if (userId.isPresent()) {
        optionalUser = userService.findById(userId.get());
      } else if (sessionUserID.isPresent()) {
        optionalUser = userService.findById(sessionUserID.get());
        userId = Optional.of(sessionUserID.get());
      }
      RequestDispatcher requestDispatcher;
      if (!optionalUser.isPresent()) {
        requestDispatcher = req.getRequestDispatcher("jsp/error.jsp");
      } else {
        if (sessionUserID.isPresent()) {
          int currentRate = speakerService.checkRate(userId.get(), sessionUserID.get());
          req.setAttribute("currentRate", currentRate);
        }
        if (optionalUser.get().getRole().getName().equals("Speaker")) {
          double averageRate = speakerService.getAverageRate(userId.get());
          req.setAttribute(AVERAGE_RATE_ATTRIBUTE, averageRate);
        }
        req.setAttribute("user", optionalUser.get());
        req.setAttribute(USER_ID_ATTRIBUTE, userId);
        if (userId.get().equals(sessionUserID.orElse(null))) {
          req.getSession().setAttribute("notify", false);
          List<Conference> registratedConferences = conferenceService.findByVisitor(optionalUser.get());
          req.setAttribute("userConferences", registratedConferences);
        }
        requestDispatcher = req.getRequestDispatcher("jsp/user.jsp");
      }
      requestDispatcher.forward(req, resp);
    } catch (RuntimeException e) {
      log.error("Error displaying user", e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      if (req.getParameter("action").equals("Rate")) {
        int newRate = integerValidator.validate(req.getParameter("newRate"));
        long speakerId = longValidator.validate(req.getParameter("id"));
        long userId = longValidator.validate(req.getSession().getAttribute(USER_ID_ATTRIBUTE).toString());
        speakerService.rateSpeakerById(speakerId, userId, newRate);
        doGet(req, resp);
      }
    } catch (RuntimeException e) {
      log.error("Error rating speaker", e);
    }

  }

}

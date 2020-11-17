package com.mape.controller;

import com.mape.dao.exception.ConferenceRegistrationException;
import com.mape.dao.exception.InconsistentDatabaseException;
import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.Conference;
import com.mape.entity.Lecture;
import com.mape.entity.User;
import com.mape.service.ConferenceService;
import com.mape.service.LectureService;
import com.mape.service.UserService;
import com.mape.service.impl.ConferenceServiceImpl;
import com.mape.service.impl.LectureServiceImpl;
import com.mape.service.impl.UserServiceImpl;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import org.apache.commons.validator.routines.LongValidator;

@Log4j
@WebServlet(urlPatterns = "/conference")
public class ConferenceServlet extends HttpServlet {

  public static final String REGISTRATION_ERROR = "registrationError";
  private final ConferenceService conferenceService = new ConferenceServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getConferenceDao());
  private final LectureService lectureService = new LectureServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getLectureDao());
  private final UserService userService = new UserServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getUserDao());

  private final LongValidator longValidator = LongValidator.getInstance();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/error.jsp");
    try {
      Optional<Long> sessionUserID = Optional.empty();
      Optional<Object> sessionUserAttribute = Optional.ofNullable(req.getSession().getAttribute("userId"));
      if (sessionUserAttribute.isPresent()) {
        sessionUserID = Optional.ofNullable(longValidator.validate(sessionUserAttribute.get().toString()));
      }
      Optional<Long> optionalConferenceId = Optional.ofNullable(longValidator.validate(req.getParameter("id")));
      Optional<Conference> optionalConference = Optional.empty();
      if (optionalConferenceId.isPresent()) {
        optionalConference = conferenceService.findById(optionalConferenceId.get());
      }
      if (optionalConference.isPresent()) {
        Conference conference = optionalConference.get();
        if (sessionUserID.isPresent()) {
          User user = userService.findById(sessionUserID.get()).orElseThrow(ConferenceRegistrationException::new);
          boolean isCurrentUserRegistered = conferenceService.checkRegistration(conference, user);
          req.setAttribute("currentUserRegistered", isCurrentUserRegistered);
          boolean isConferencePassed = conference.getTime().isBefore(LocalDateTime.now());
          boolean isConferenceFull = conference.getRegisteredVisitors() >= conference.getMaxVisitors();
          String conferenceState;
          if (isConferencePassed) {
            conferenceState = "passed";
          } else if (isConferenceFull && !isCurrentUserRegistered) {
            conferenceState = "full";
          } else {
            conferenceState = "open";
          }
          req.setAttribute("conferenceState", conferenceState);
        }
        req.setAttribute("conference", conference);
        List<Lecture> conferenceLectures = conference.getLectures();
        String userRole = Optional.ofNullable(req.getSession().getAttribute("userRole")).orElse("").toString();
        if (!userRole.equals("Admin") && !userRole.equals("Moderator") && !userRole.equals("Speaker")) {
          conferenceLectures = lectureService.filterByApproving(conferenceLectures, true, true);
        }
        req.setAttribute("conferenceLectures", conferenceLectures);
        requestDispatcher = req.getRequestDispatcher("jsp/conference.jsp");
      }
    } catch (RuntimeException e) {
      log.error("Error displaying conference", e);
    }
    requestDispatcher.forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      req.setAttribute(REGISTRATION_ERROR, "ok");
      String action = req.getParameter("button");
      User user = userService.findById(longValidator.validate(req.getSession().getAttribute("userId").toString()))
          .orElseThrow(InconsistentDatabaseException::new);
      String userRole = user.getRole().getName();
      Conference conference = conferenceService.findById(longValidator.validate(req.getParameter("id")))
          .orElseThrow(InconsistentDatabaseException::new);
      if (action.startsWith("delete") && isEditorRole(userRole)) {
        long lectureId = Long.parseLong(action.replace("delete", ""));
        Lecture lecture = lectureService.findById(lectureId).orElseThrow(InconsistentDatabaseException::new);
        lectureService.deleteById(lecture.getId());
      } else {
        if (conference.getTime().isBefore(LocalDateTime.now())) {
          req.setAttribute(REGISTRATION_ERROR, "passed");
          throw new ConferenceRegistrationException("Conference registration expired");
        }
        if (action.equals("register")) {
          if (conference.getRegisteredVisitors() >= conference.getMaxVisitors()) {
            req.setAttribute(REGISTRATION_ERROR, "full");
            throw new ConferenceRegistrationException("Conference full");
          }
          conferenceService.registerUser(conference, user);
        } else if (action.equals("unregister")) {
          conferenceService.unregisterUser(conference, user);
        } else if (action.startsWith("approve") && isEditorRole(userRole)) {
          long lectureId = Long.parseLong(action.replace("approve", ""));
          Lecture lecture = lectureService.findById(lectureId).orElseThrow(InconsistentDatabaseException::new);
          lectureService.approveById(lecture.getId());
        }
      }
    } catch (RuntimeException e) {
      log.error("Error during registration actions", e);
    }
    doGet(req, resp);
  }

  private boolean isEditorRole(String userRole) {
    return userRole.equals("Speaker") || userRole.equals("Admin") || userRole.equals("Moderator");
  }

}

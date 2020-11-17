package com.mape.controller;

import com.mape.controller.exception.UnauthorizedAccessException;
import com.mape.dao.exception.InconsistentDatabaseException;
import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.Conference;
import com.mape.entity.Lecture;
import com.mape.service.ConferenceService;
import com.mape.service.LectureService;
import com.mape.service.SpeakerService;
import com.mape.service.impl.ConferenceServiceImpl;
import com.mape.service.impl.LectureServiceImpl;
import com.mape.service.impl.SpeakerServiceImpl;
import com.mape.util.LocalDateTimeUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import org.apache.commons.validator.routines.LongValidator;

@Log4j
@WebServlet(urlPatterns = "/edit_conference")
public class ConferenceEditingServlet extends HttpServlet {

  private final ConferenceService conferenceService = new ConferenceServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getConferenceDao());
  private final LectureService lectureService = new LectureServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getLectureDao());
  private final SpeakerService speakerService = new SpeakerServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getSpeakerDao());

  private final LongValidator longValidator = LongValidator.getInstance();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/error.jsp");
    try {
      Optional<Long> optionalConferenceId = Optional.ofNullable(longValidator.validate(req.getParameter("id")));
      Optional<Conference> optionalConference = Optional.empty();
      if (optionalConferenceId.isPresent()) {
        optionalConference = conferenceService.findById(optionalConferenceId.get());
      }
      if (!"Admin".equals(req.getSession().getAttribute("userRole")) &&
          !"Moderator".equals(req.getSession().getAttribute("userRole"))) {
        throw new UnauthorizedAccessException();
      }
      if (optionalConference.isPresent()) {
        Conference conference = optionalConference.get();
        req.setAttribute("conference", conference);
        List<Lecture> conferenceLectures = lectureService.findByConference(conference);
        req.setAttribute("conferenceLectures", conferenceLectures);
        req.setAttribute("availableSpeakers", speakerService.findAll());
        requestDispatcher = req.getRequestDispatcher("jsp/conferenceEditing.jsp");
      }
    } catch (RuntimeException e) {
      log.error("Error making conference editing", e);
    }
    requestDispatcher.forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String conferenceIdString = req.getParameter("conferenceId");
    try {
      Long conferenceId = Long.parseLong(conferenceIdString);
      String conferenceName = req.getParameter("conferenceName");
      String conferenceDateTimeString = req.getParameter("conferenceDate") + " " + req.getParameter("conferenceTime");
      LocalDateTime conferenceDatetime = LocalDateTimeUtils.parseLocalDateTime(conferenceDateTimeString);
      String conferenceAddress = req.getParameter("conferenceAddress");
      Integer maxVisitors = Integer.parseInt(req.getParameter("maxVisitors"));
      String[] lectureTopics = req.getParameterValues("lectureTopic");
      List<Lecture> lectures = new ArrayList<>();
      if (lectureTopics != null && lectureTopics.length != 0) {
        List<Long> lectureIds = Arrays.stream(req.getParameterValues("lectureId")).map(Long::parseLong)
            .collect(Collectors.toList());
        String[] speakersIds = req.getParameterValues("Speakers");
        for (int i = 0; i < lectureTopics.length; i++) {
          Long speakerId;
          boolean approvedBySpeaker = false;
          if (speakersIds[i].startsWith("DEFAULT")) {
            speakerId = Long.parseLong(speakersIds[i].replace("DEFAULT", ""));
            approvedBySpeaker = true;
          } else {
            speakerId = Long.parseLong(speakersIds[i]);
          }
          lectures.add(
              new Lecture(lectureIds.get(i), lectureTopics[i], speakerService.findById(speakerId).orElseThrow(
                  InconsistentDatabaseException::new), conferenceId, true, approvedBySpeaker));
        }
      }

      Conference conference = Conference.builder().withId(conferenceId).withName(conferenceName)
          .withAddress(conferenceAddress)
          .withTime(conferenceDatetime).withMaxVisitors(maxVisitors).withRegisteredVisitors(0)
          .withLectures(lectures).build();
      conferenceService.save(conference);
    } catch (RuntimeException e) {
      log.error("Error during registration actions", e);
    }
    resp.sendRedirect("./conference?id=" + conferenceIdString);
  }
}

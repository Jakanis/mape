package com.mape.controller;

import com.mape.controller.exception.UnauthorizedAccessException;
import com.mape.dao.exception.InconsistentDatabaseException;
import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.Conference;
import com.mape.entity.Lecture;
import com.mape.service.ConferenceService;
import com.mape.service.SpeakerService;
import com.mape.service.impl.ConferenceServiceImpl;
import com.mape.service.impl.SpeakerServiceImpl;
import com.mape.util.LocalDateTimeUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;

@Log4j
@WebServlet(urlPatterns = "/add_conference")
public class ConferenceAddingServlet extends HttpServlet {

  private final ConferenceService conferenceService = new ConferenceServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getConferenceDao());
  private final SpeakerService speakerService = new SpeakerServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getSpeakerDao());

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/error.jsp");
    try {
      if (!"Admin".equals(req.getSession().getAttribute("userRole")) &&
          !"Moderator".equals(req.getSession().getAttribute("userRole"))) {
        throw new UnauthorizedAccessException();
      }
      req.setAttribute("availableSpeakers", speakerService.findAll());
      requestDispatcher = req.getRequestDispatcher("jsp/conferenceAdding.jsp");
    } catch (RuntimeException e) {
      log.error("Error during conference adding", e);
    } finally {
      requestDispatcher.forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    try {
      String conferenceName = req.getParameter("conferenceName");
      String conferenceDateTimeString = req.getParameter("conferenceDate") + " " + req.getParameter("conferenceTime");
      LocalDateTime conferenceDatetime = LocalDateTimeUtils.parseLocalDateTime(conferenceDateTimeString);
      String conferenceAddress = req.getParameter("conferenceAddress");
      int maxVisitors = Integer.parseInt(req.getParameter("maxVisitors"));
      String[] lectureTopics = req.getParameterValues("lectureTopic");
      List<Lecture> lectures = new ArrayList<>();
      if (lectureTopics != null && lectureTopics.length != 0) {
        List<Long> lectureIds = Arrays.stream(req.getParameterValues("lectureId")).map(Long::parseLong)
            .collect(Collectors.toList());
        List<Long> speakersIds = Arrays.stream(req.getParameterValues("Speakers")).map(Long::parseLong)
            .collect(Collectors.toList());
        for (int i = 0; i < lectureTopics.length; i++) {
          lectures.add(
              new Lecture(lectureIds.get(i), lectureTopics[i], speakerService.findById(speakersIds.get(i)).orElseThrow(
                  InconsistentDatabaseException::new), null, true, true));
        }
      }
      Conference conference = Conference.builder().withName(conferenceName).withAddress(conferenceAddress)
          .withTime(conferenceDatetime).withMaxVisitors(maxVisitors).withRegisteredVisitors(0).withLectures(lectures)
          .build();
      conferenceService.save(conference);
      resp.sendRedirect("./conference?id=" + conference.getId());
    } catch (RuntimeException e) {
      log.error("Error during conference adding", e);
    }
  }

}

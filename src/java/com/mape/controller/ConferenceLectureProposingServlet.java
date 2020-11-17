package com.mape.controller;

import com.mape.controller.exception.UnauthorizedAccessException;
import com.mape.dao.exception.InconsistentDatabaseException;
import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.Conference;
import com.mape.entity.Lecture;
import com.mape.entity.Speaker;
import com.mape.service.ConferenceService;
import com.mape.service.LectureService;
import com.mape.service.SpeakerService;
import com.mape.service.impl.ConferenceServiceImpl;
import com.mape.service.impl.LectureServiceImpl;
import com.mape.service.impl.SpeakerServiceImpl;
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
import org.apache.commons.validator.routines.LongValidator;

@Log4j
@WebServlet(urlPatterns = "/propose_lecture")
public class ConferenceLectureProposingServlet extends HttpServlet {

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
      if (!"Speaker".equals(req.getSession().getAttribute("userRole"))) {
        throw new UnauthorizedAccessException();
      }
      if (optionalConference.isPresent()) {
        Conference conference = optionalConference.get();
        req.setAttribute("conference", conference);
        List<Lecture> conferenceLectures = lectureService.findByConference(conference);
        req.setAttribute("conferenceLectures", conferenceLectures);
        req.setAttribute("availableSpeakers", speakerService.findAll());
        requestDispatcher = req.getRequestDispatcher("jsp/conferenceLectionPropose.jsp");
      }
    } catch (RuntimeException e) {
      log.error("Error making conference editing", e);
    }
    requestDispatcher.forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String conferenceIdString = req.getParameter("id");
    try {
      Long conferenceId = Long.parseLong(conferenceIdString);
      String lectureTopic = req.getParameter("lectureTopic");
      Long speakerId = longValidator.validate(req.getSession().getAttribute("userId").toString());
      Speaker speaker = speakerService.findById(speakerId).orElseThrow(InconsistentDatabaseException::new);
      lectureService.save(new Lecture(lectureTopic, speaker, conferenceId, false, true));

    } catch (RuntimeException e) {
      log.error("Error during registration actions", e);
    }
    resp.sendRedirect("conference?id=" + conferenceIdString);
  }
}

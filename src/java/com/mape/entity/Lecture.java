package com.mape.entity;

import java.util.Objects;

public class Lecture extends Entity {

  private final String topic;
  private final Speaker speaker;
  private Long conferenceId;
  private boolean approvedByModerator;
  private boolean approvedBySpeaker;

  public Lecture(Long id, String topic, Speaker speaker, Long conferenceId,
      boolean approvedByModerator, boolean approvedBySpeaker) {
    super(id);
    this.topic = topic;
    this.speaker = speaker;
    this.conferenceId = conferenceId;
    this.approvedByModerator = approvedByModerator;
    this.approvedBySpeaker = approvedBySpeaker;
  }

  public Lecture(String topic, Speaker speaker, Long conferenceId, boolean approvedByModerator,
      boolean approvedBySpeaker) {
    this.topic = topic;
    this.speaker = speaker;
    this.conferenceId = conferenceId;
    this.approvedByModerator = approvedByModerator;
    this.approvedBySpeaker = approvedBySpeaker;
  }

  public String getTopic() {
    return topic;
  }

  public Speaker getSpeaker() {
    return speaker;
  }

  public Long getConferenceId() {
    return conferenceId;
  }

  public void setConferenceId(Long conferenceId) {
    this.conferenceId = conferenceId;
  }

  public boolean isApprovedByModerator() {
    return approvedByModerator;
  }

  public boolean isApprovedBySpeaker() {
    return approvedBySpeaker;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Lecture lecture = (Lecture) o;
    return approvedByModerator == lecture.approvedByModerator &&
        approvedBySpeaker == lecture.approvedBySpeaker &&
        topic.equals(lecture.topic) &&
        speaker.equals(lecture.speaker) &&
        Objects.equals(conferenceId, lecture.conferenceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), topic, speaker, conferenceId, approvedByModerator, approvedBySpeaker);
  }
}

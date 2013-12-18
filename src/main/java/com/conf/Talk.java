package com.conf;

import org.joda.time.Duration;
import org.joda.time.LocalTime;

class Talk
{
  static final Duration SHORT_TALK_DURATION = Duration.standardMinutes(5L);
  String talkName;
  Duration talkDuration;
  private LocalTime startsOn;
  LocalTime endsOn;

  static Talk normalTalk(String talkName, Duration talkDuration, LocalTime startsOn)
  {
    return new Talk(talkName, talkDuration, startsOn);
  }

  static Talk shortTalk(String talkName, LocalTime startsOn)
  {
    return new Talk(talkName, SHORT_TALK_DURATION, startsOn);
  }

  Talk(String talkName, Duration talkDuration, LocalTime startsOn)
  {
    this.talkName = talkName;
    this.talkDuration = talkDuration;
    this.startsOn = startsOn;
    this.endsOn = startsOn.plus(talkDuration.toStandardSeconds());
  }

  public boolean isTalkOverlapping(LocalTime proposedStartTimeOfTalk, LocalTime proposedEndsTimeOfTalk)
  {
    return ((proposedStartTimeOfTalk.isAfter(this.startsOn)) && (proposedStartTimeOfTalk.isBefore(this.endsOn))) || ((proposedStartTimeOfTalk.isBefore(this.startsOn)) && (proposedEndsTimeOfTalk.isAfter(this.startsOn)));
  }

  public boolean isShortTalk()
  {
    return this.talkDuration.equals(SHORT_TALK_DURATION);
  }

  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    Talk talk = (Talk)o;
    if (this.talkName != null ? !this.talkName.equals(talk.talkName) : talk.talkName != null) {
      return false;
    }
    return true;
  }

  public int hashCode()
  {
    return this.talkName != null ? this.talkName.hashCode() : 0;
  }

  static boolean isValidNetworkingEventStartTime(LocalTime startsOn)
  {
    return (startsOn.isAfter(LocalTime.parse("T16:00:00"))) && (startsOn.isBefore(LocalTime.parse("T17:00:00")));
  }
}


/* Location:           F:\codebases\codekata\confmangt\target\classes\
 * Qualified Name:     com.conf.Talk
 * JD-Core Version:    0.7.0.1
 */
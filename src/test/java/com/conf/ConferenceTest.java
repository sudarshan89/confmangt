package com.conf;

import org.joda.time.Duration;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class ConferenceTest {
    Conference conferenceWithThreeTracksCapacity;

    @Before
    public void setUp()
            throws Exception {
        this.conferenceWithThreeTracksCapacity = Conference.Plan("Performance Conference", 3);
    }

    @Test
    public void whenConferenceWithThreeTracksIsPlanned_itShouldCreateThreeSeparateTracksWithinConference() {
        Assert.assertThat(this.conferenceWithThreeTracksCapacity.noOfTracks(), is(Integer.valueOf(3)));
    }

    @Test
    public void givenAConferenceWithThreeTracks_WhenFirstTrackIsAdded_itShouldReturnTrue() {
        Boolean isTrackAdded = Boolean.valueOf(this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning"));
        Assert.assertThat(isTrackAdded, is(Boolean.TRUE));
    }

    @Test
    public void givenAConferenceWithOneTrack_WhenSecondTrackIsAdded_itShouldReturnFalse() {
        Conference conferenceWithOneTrack = Conference.Plan("SingleTrackConf", 1);
        Boolean isTrackAdded = Boolean.valueOf(conferenceWithOneTrack.addTrack("First Track"));
        isTrackAdded = Boolean.valueOf(conferenceWithOneTrack.addTrack("Second Track"));
        Assert.assertThat(isTrackAdded, is(Boolean.FALSE));
    }

    @Test(expected = Track.InValidTalkTimingsException.class)
    public void whenTalkIsScheduledBeforeNineAM_itShouldThrowException() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T8:50:00"));
    }

    @Test(expected = Track.InValidTalkTimingsException.class)
    public void whenTalkIsScheduledBetweenOneAndTwoPM_itShouldThrowException() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T1:50:00"));
    }

    @Test(expected = Track.InValidTalkTimingsException.class)
    public void whenTalkIsScheduledAfterFivePM_itShouldThrowException() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T17:50:00"));
    }

    @Test(expected = Track.InValidTalkTimingsException.class)
    public void givenATalkIsAddedToConference_whenEndTimeExceedsFivePM_itShouldThrowException() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T16:20:00"));
    }

    @Test(expected = Track.InValidTalkTimingsException.class)
    public void givenATalkIsAddedToConference_whenEndTimeExceedsOnePM_itShouldThrowException() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T12:50:00"));
    }

    @Test
    public void givenConferenceWithTrack_whenTalkIsScheduled_itShouldFindTalkInTheTrack() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        String talkName = "Adv Tuning";
        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T13:50:00"));

        Talk talk = this.conferenceWithThreeTracksCapacity.findTrack("JVM Tunning").findTalk("Adv Tuning");
        Assert.assertThat(talk.talkName, is("Adv Tuning"));
        Assert.assertThat(talk.endsOn, is(equalTo(LocalTime.parse("T14:30:00"))));
    }

    @Test
    public void givenConferenceWithTrack_whenShortTalkIsScheduled_itShouldFinishInFiveMinutes() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        String talkName = "Adv Tuning";
        this.conferenceWithThreeTracksCapacity.scheduleShortTalk("JVM Tunning", "Adv Tuning", LocalTime.parse("T13:50:00"));

        Talk talk = this.conferenceWithThreeTracksCapacity.findTrack("JVM Tunning").findTalk("Adv Tuning");
        Assert.assertThat(talk.talkName, is("Adv Tuning"));
        Assert.assertThat(talk.endsOn, is(equalTo(LocalTime.parse("T13:55:00"))));
    }

    @Test
    public void givenAScheduledTalk_whenItIsCancelled_itShouldReturnTrue() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        String talkName = "Adv Tuning";
        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T13:50:00"));

        boolean isCancelled = this.conferenceWithThreeTracksCapacity.cancelTalk("JVM Tunning", "Adv Tuning");
        Assert.assertThat(Boolean.valueOf(isCancelled), is(Boolean.TRUE));
    }

    @Test
    public void givenAInvalidTalk_whenItIsCancelled_itShouldReturnFalse() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        String talkName = "InvalidTalk";

        boolean isCancelled = this.conferenceWithThreeTracksCapacity.cancelTalk("JVM Tunning", "InvalidTalk");
        Assert.assertThat(Boolean.valueOf(isCancelled), is(Boolean.FALSE));
    }

    @Test
    public void givenANetworkingEvent_itShouldStartBetween4And5PM() {
    }

    @Test(expected = Track.TalkTimingsOverlapException.class)
    public void givenAScheduledTalk_whenANewTalkIsScheduledInBetweenAExistingTalk_itShouldThrowException() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(30L), LocalTime.parse("T13:00:01"));

        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Clash Talk", Duration.standardMinutes(30L), LocalTime.parse("T13:15:00"));
    }

    @Test(expected = Track.TalkTimingsOverlapException.class)
    public void givenAScheduledTalk_whenANewTalkIsXXXXXX_itShouldThrowException() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(30L), LocalTime.parse("T14:00:00"));

        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Clash Talk", Duration.standardMinutes(30L), LocalTime.parse("T13:45:00"));
    }

    @Test(expected = Track.InValidNetworkingEventTimingException.class)
    public void whenNetworkingEventSchduledAtFourPM_itShouldThrowException() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        this.conferenceWithThreeTracksCapacity.scheduleNetworkingEvent("JVM Tunning", LocalTime.parse("T16:00:00"));
    }

    @Test(expected = Track.TalkTimingsOverlapException.class)
    public void givenConferenceWithTalks_whenNetworkingEventOverlapsWithScheduledTalk_itShouldThrowException() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");

        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(60L), LocalTime.parse("T15:30:00"));

        this.conferenceWithThreeTracksCapacity.scheduleNetworkingEvent("JVM Tunning", LocalTime.parse("T16:15:00"));
    }

    @Test
    public void givenAConferenceWithTalks_whenATalkIsRescheduled_itShouldChangeTheTalkStartTime() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        String talkName = "Adv Tuning";
        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(59L), LocalTime.parse("T15:30:00"));

        this.conferenceWithThreeTracksCapacity.rescheduleTalk("JVM Tunning", "Adv Tuning", LocalTime.parse("T16:00:00"));
        Talk rescheduledTalk = this.conferenceWithThreeTracksCapacity.findTrack("JVM Tunning").findTalk("Adv Tuning");

        Assert.assertThat(rescheduledTalk.endsOn, is(equalTo(LocalTime.parse("T16:59:00"))));
    }

    @Test
    public void givenAConferenceWithTalks_whenAShortTalkIsRescheduled_itShouldChangeTheTalkStartTime() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        String talkName = "Adv Tuning";
        this.conferenceWithThreeTracksCapacity.scheduleShortTalk("JVM Tunning", "Adv Tuning", LocalTime.parse("T15:30:00"));

        this.conferenceWithThreeTracksCapacity.rescheduleTalk("JVM Tunning", "Adv Tuning", LocalTime.parse("T16:00:00"));
        Talk rescheduledTalk = this.conferenceWithThreeTracksCapacity.findTrack("JVM Tunning").findTalk("Adv Tuning");

        Assert.assertThat(rescheduledTalk.endsOn, is(equalTo(LocalTime.parse("T16:05:00"))));
    }

    @Test(expected = Track.TalkTimingsOverlapException.class)
    public void givenATalkIsRescheduled_whenItOverlapsWithExistingTalk_itShouldThrowException() {
        String trackName = "JVM Tunning";
        this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
        String talkName = "Adv Tuning";
        String talkName1 = "talkName1 Adv Tuning";
        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(30L), LocalTime.parse("T15:30:00"));
        this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "talkName1 Adv Tuning", Duration.standardMinutes(30L), LocalTime.parse("T16:00:00"));

        this.conferenceWithThreeTracksCapacity.rescheduleTalk("JVM Tunning", "Adv Tuning", LocalTime.parse("T16:05:00"));
    }
}


/* Location:           F:\codebases\codekata\confmangt\target\test-classes\
 * Qualified Name:     com.conf.ConferenceTest
 * JD-Core Version:    0.7.0.1
 */
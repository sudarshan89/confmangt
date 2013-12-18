/*     */ package com.conf;
/*     */ 
/*     */ import org.hamcrest.CoreMatchers;
/*     */ import org.joda.time.Duration;
/*     */ import org.joda.time.LocalTime;
/*     */ import org.junit.Assert;
/*     */ import org.junit.Before;
/*     */ import org.junit.Test;
/*     */ 
/*     */ public class ConferenceTest
/*     */ {
/*     */   Conference conferenceWithThreeTracksCapacity;
/*     */   
/*     */   @Before
/*     */   public void setUp()
/*     */     throws Exception
/*     */   {
/*  22 */     this.conferenceWithThreeTracksCapacity = Conference.Plan("Performance Conference", 3);
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void whenConferenceWithThreeTracksIsPlanned_itShouldCreateThreeSeparateTracksWithinConference()
/*     */   {
/*  27 */     Assert.assertThat(this.conferenceWithThreeTracksCapacity.noOfTracks(), CoreMatchers.is(Integer.valueOf(3)));
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void givenAConferenceWithThreeTracks_WhenFirstTrackIsAdded_itShouldReturnTrue()
/*     */   {
/*  32 */     Boolean isTrackAdded = Boolean.valueOf(this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning"));
/*  33 */     Assert.assertThat(isTrackAdded, CoreMatchers.is(Boolean.TRUE));
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void givenAConferenceWithOneTrack_WhenSecondTrackIsAdded_itShouldReturnFalse()
/*     */   {
/*  38 */     Conference conferenceWithOneTrack = Conference.Plan("SingleTrackConf", 1);
/*  39 */     Boolean isTrackAdded = Boolean.valueOf(conferenceWithOneTrack.addTrack("First Track"));
/*  40 */     isTrackAdded = Boolean.valueOf(conferenceWithOneTrack.addTrack("Second Track"));
/*  41 */     Assert.assertThat(isTrackAdded, CoreMatchers.is(Boolean.FALSE));
/*     */   }
/*     */   
/*     */   @Test(expected=Track.InValidTalkTimingsException.class)
/*     */   public void whenTalkIsScheduledBeforeNineAM_itShouldThrowException()
/*     */   {
/*  46 */     String trackName = "JVM Tunning";
/*  47 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/*  48 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T8:50:00"));
/*     */   }
/*     */   
/*     */   @Test(expected=Track.InValidTalkTimingsException.class)
/*     */   public void whenTalkIsScheduledBetweenOneAndTwoPM_itShouldThrowException()
/*     */   {
/*  53 */     String trackName = "JVM Tunning";
/*  54 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/*  55 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T1:50:00"));
/*     */   }
/*     */   
/*     */   @Test(expected=Track.InValidTalkTimingsException.class)
/*     */   public void whenTalkIsScheduledAfterFivePM_itShouldThrowException()
/*     */   {
/*  60 */     String trackName = "JVM Tunning";
/*  61 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/*  62 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T17:50:00"));
/*     */   }
/*     */   
/*     */   @Test(expected=Track.InValidTalkTimingsException.class)
/*     */   public void givenATalkIsAddedToConference_whenEndTimeExceedsFivePM_itShouldThrowException()
/*     */   {
/*  68 */     String trackName = "JVM Tunning";
/*  69 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/*  70 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T16:20:00"));
/*     */   }
/*     */   
/*     */   @Test(expected=Track.InValidTalkTimingsException.class)
/*     */   public void givenATalkIsAddedToConference_whenEndTimeExceedsOnePM_itShouldThrowException()
/*     */   {
/*  75 */     String trackName = "JVM Tunning";
/*  76 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/*  77 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T12:50:00"));
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void givenConferenceWithTrack_whenTalkIsScheduled_itShouldFindTalkInTheTrack()
/*     */   {
/*  82 */     String trackName = "JVM Tunning";
/*  83 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/*  84 */     String talkName = "Adv Tuning";
/*  85 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T13:50:00"));
/*     */     
/*  87 */     Talk talk = this.conferenceWithThreeTracksCapacity.findTrack("JVM Tunning").findTalk("Adv Tuning");
/*  88 */     Assert.assertThat(talk.talkName, CoreMatchers.is("Adv Tuning"));
/*  89 */     Assert.assertThat(talk.endsOn, CoreMatchers.is(CoreMatchers.equalTo(LocalTime.parse("T14:30:00"))));
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void givenConferenceWithTrack_whenShortTalkIsScheduled_itShouldFinishInFiveMinutes()
/*     */   {
/*  95 */     String trackName = "JVM Tunning";
/*  96 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/*  97 */     String talkName = "Adv Tuning";
/*  98 */     this.conferenceWithThreeTracksCapacity.scheduleShortTalk("JVM Tunning", "Adv Tuning", LocalTime.parse("T13:50:00"));
/*     */     
/* 100 */     Talk talk = this.conferenceWithThreeTracksCapacity.findTrack("JVM Tunning").findTalk("Adv Tuning");
/* 101 */     Assert.assertThat(talk.talkName, CoreMatchers.is("Adv Tuning"));
/* 102 */     Assert.assertThat(talk.endsOn, CoreMatchers.is(CoreMatchers.equalTo(LocalTime.parse("T13:55:00"))));
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void givenAScheduledTalk_whenItIsCancelled_itShouldReturnTrue()
/*     */   {
/* 109 */     String trackName = "JVM Tunning";
/* 110 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/* 111 */     String talkName = "Adv Tuning";
/* 112 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(40L), LocalTime.parse("T13:50:00"));
/*     */     
/* 114 */     boolean isCancelled = this.conferenceWithThreeTracksCapacity.cancelTalk("JVM Tunning", "Adv Tuning");
/* 115 */     Assert.assertThat(Boolean.valueOf(isCancelled), CoreMatchers.is(Boolean.TRUE));
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void givenAInvalidTalk_whenItIsCancelled_itShouldReturnFalse()
/*     */   {
/* 120 */     String trackName = "JVM Tunning";
/* 121 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/* 122 */     String talkName = "InvalidTalk";
/*     */     
/* 124 */     boolean isCancelled = this.conferenceWithThreeTracksCapacity.cancelTalk("JVM Tunning", "InvalidTalk");
/* 125 */     Assert.assertThat(Boolean.valueOf(isCancelled), CoreMatchers.is(Boolean.FALSE));
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void givenANetworkingEvent_itShouldStartBetween4And5PM() {}
/*     */   
/*     */   @Test(expected=Track.TalkTimingsOverlapException.class)
/*     */   public void givenAScheduledTalk_whenANewTalkIsScheduledInBetweenAExistingTalk_itShouldThrowException()
/*     */   {
/* 135 */     String trackName = "JVM Tunning";
/* 136 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/* 137 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(30L), LocalTime.parse("T13:00:01"));
/*     */     
/* 139 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Clash Talk", Duration.standardMinutes(30L), LocalTime.parse("T13:15:00"));
/*     */   }
/*     */   
/*     */   @Test(expected=Track.TalkTimingsOverlapException.class)
/*     */   public void givenAScheduledTalk_whenANewTalkIsXXXXXX_itShouldThrowException()
/*     */   {
/* 144 */     String trackName = "JVM Tunning";
/* 145 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/* 146 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(30L), LocalTime.parse("T14:00:00"));
/*     */     
/* 148 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Clash Talk", Duration.standardMinutes(30L), LocalTime.parse("T13:45:00"));
/*     */   }
/*     */   
/*     */   @Test(expected=Track.InValidNetworkingEventTimingException.class)
/*     */   public void whenNetworkingEventSchduledAtFourPM_itShouldThrowException()
/*     */   {
/* 153 */     String trackName = "JVM Tunning";
/* 154 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/* 155 */     this.conferenceWithThreeTracksCapacity.scheduleNetworkingEvent("JVM Tunning", LocalTime.parse("T16:00:00"));
/*     */   }
/*     */   
/*     */   @Test(expected=Track.TalkTimingsOverlapException.class)
/*     */   public void givenConferenceWithTalks_whenNetworkingEventOverlapsWithScheduledTalk_itShouldThrowException()
/*     */   {
/* 161 */     String trackName = "JVM Tunning";
/* 162 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/*     */     
/* 164 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(60L), LocalTime.parse("T15:30:00"));
/*     */     
/* 166 */     this.conferenceWithThreeTracksCapacity.scheduleNetworkingEvent("JVM Tunning", LocalTime.parse("T16:15:00"));
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void givenAConferenceWithTalks_whenATalkIsRescheduled_itShouldChangeTheTalkStartTime()
/*     */   {
/* 172 */     String trackName = "JVM Tunning";
/* 173 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/* 174 */     String talkName = "Adv Tuning";
/* 175 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(59L), LocalTime.parse("T15:30:00"));
/*     */     
/* 177 */     this.conferenceWithThreeTracksCapacity.rescheduleTalk("JVM Tunning", "Adv Tuning", LocalTime.parse("T16:00:00"));
/* 178 */     Talk rescheduledTalk = this.conferenceWithThreeTracksCapacity.findTrack("JVM Tunning").findTalk("Adv Tuning");
/*     */     
/* 180 */     Assert.assertThat(rescheduledTalk.endsOn, CoreMatchers.is(CoreMatchers.equalTo(LocalTime.parse("T16:59:00"))));
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void givenAConferenceWithTalks_whenAShortTalkIsRescheduled_itShouldChangeTheTalkStartTime()
/*     */   {
/* 188 */     String trackName = "JVM Tunning";
/* 189 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/* 190 */     String talkName = "Adv Tuning";
/* 191 */     this.conferenceWithThreeTracksCapacity.scheduleShortTalk("JVM Tunning", "Adv Tuning", LocalTime.parse("T15:30:00"));
/*     */     
/* 193 */     this.conferenceWithThreeTracksCapacity.rescheduleTalk("JVM Tunning", "Adv Tuning", LocalTime.parse("T16:00:00"));
/* 194 */     Talk rescheduledTalk = this.conferenceWithThreeTracksCapacity.findTrack("JVM Tunning").findTalk("Adv Tuning");
/*     */     
/* 196 */     Assert.assertThat(rescheduledTalk.endsOn, CoreMatchers.is(CoreMatchers.equalTo(LocalTime.parse("T16:05:00"))));
/*     */   }
/*     */   
/*     */   @Test(expected=Track.TalkTimingsOverlapException.class)
/*     */   public void givenATalkIsRescheduled_whenItOverlapsWithExistingTalk_itShouldThrowException()
/*     */   {
/* 203 */     String trackName = "JVM Tunning";
/* 204 */     this.conferenceWithThreeTracksCapacity.addTrack("JVM Tunning");
/* 205 */     String talkName = "Adv Tuning";
/* 206 */     String talkName1 = "talkName1 Adv Tuning";
/* 207 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "Adv Tuning", Duration.standardMinutes(30L), LocalTime.parse("T15:30:00"));
/* 208 */     this.conferenceWithThreeTracksCapacity.scheduleTalk("JVM Tunning", "talkName1 Adv Tuning", Duration.standardMinutes(30L), LocalTime.parse("T16:00:00"));
/*     */     
/* 210 */     this.conferenceWithThreeTracksCapacity.rescheduleTalk("JVM Tunning", "Adv Tuning", LocalTime.parse("T16:05:00"));
/*     */   }
/*     */ }


/* Location:           F:\codebases\codekata\confmangt\target\test-classes\
 * Qualified Name:     com.conf.ConferenceTest
 * JD-Core Version:    0.7.0.1
 */
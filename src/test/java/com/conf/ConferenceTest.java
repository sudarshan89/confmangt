package com.conf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ConferenceTest {
    Conference conferenceWithThreeTracksCapacity;

    @Before
    public void setUp()
            throws Exception {
        this.conferenceWithThreeTracksCapacity = Conference.Plan("Performance Conference", 3);
    }

    @Test
    public void whenConferenceWithThreeTracksIsPlanned_itShouldCreateThreeSeparateTracksWithinConference() {
        assertThat(this.conferenceWithThreeTracksCapacity.noOfTracks(), is(Integer.valueOf(3)));
    }

    /*************************************************************************************************************************************************************************************/

    @Test
    public void givenALineWithNormalTalk_whenCreatingAUnscheduledTalk_itShouldCreateTalkWithCorrectName(){
        Stream<String> normalTalkString = Stream.of("Writing Fast Tests Against Enterprise Rails 60min");
        final Talk normalTalk = Conference.MapStringsIntoTalks(normalTalkString).get(0);

        assertThat(normalTalk.talkName, is("Writing Fast Tests Against Enterprise Rails"));
    }

    @Test
    public void givenALineWithNormalTalk_whenCreatingAUnscheduledTalk_itShouldCreateTalkWithCorrectDuration(){
        Stream<String> normalTalkString = Stream.of("Writing Fast Tests Against Enterprise Rails 60min");
        final Talk normalTalk = Conference.MapStringsIntoTalks(normalTalkString).get(0);

        normalTalkString = Stream.of("Writing Fast Tests Against Enterprise Rails 6min");
        final Talk normalTalkWithSingleDigitMinuteDuration = Conference.MapStringsIntoTalks(normalTalkString).get(0);


        assertThat(normalTalk.talkDuration, is(equalTo(Duration.ofMinutes(60L))));
        assertThat(normalTalkWithSingleDigitMinuteDuration.talkDuration, is(equalTo(Duration.ofMinutes(6L))));


    }

    @Test
    public void givenALineWithLightningTalk_whenCreatingAUnscheduledTalk_itShouldCreateTalkWithFiveMinuteDuration(){
        Stream<String> normalTalkString = Stream.of("Rails for Python Developers lightning");
        final Talk normalTalk = Conference.MapStringsIntoTalks(normalTalkString).get(0);

        assertThat(normalTalk.talkName, is("Rails for Python Developers"));
        assertThat(normalTalk.talkDuration, is(equalTo(Duration.ofMinutes(5L))));
    }

    @Test
    public void givenAListOfTalks_whenFindingTheTalkToScheduleInSession_itShouldReturnTalkWhoseDurationMatchesClosestWithTimeRemainingInSession(){
        Talk talk1 = Talk.normalTalk("Talk 1",Duration.ofMinutes(15L));
        Talk talk2 = Talk.normalTalk("Talk 2",Duration.ofMinutes(35L));
        Talk talk3 = Talk.normalTalk("Talk 3",Duration.ofMinutes(45L));
        Talk talk4 = Talk.normalTalk("Talk 3",Duration.ofMinutes(4500L));

        List<Talk> unscheduledTalks = new ArrayList();
        unscheduledTalks.add(talk1);
        unscheduledTalks.add(talk2);
        unscheduledTalks.add(talk3);
        unscheduledTalks.add(talk4);

        final Optional<Talk> talk = Track.FindTalkToSchedule(unscheduledTalks, Track.Session.MORNING);
        assertThat(talk.get().talkDuration,is(equalTo(talk3.talkDuration)));
    }

    @Test
    public void givenAListOfTalks_whenSchedulingTalksInATrack_itShouldReturnAllTheScheduledTalks(){
        Talk talk1 = Talk.normalTalk("Talk 1",Duration.ofMinutes(140L));
        Talk talk2 = Talk.normalTalk("Talk 2",Duration.ofMinutes(30L));
        Talk talk3 = Talk.normalTalk("Talk 3",Duration.ofMinutes(200L));
        Talk talk4 = Talk.normalTalk("Talk 4",Duration.ofMinutes(20L));
        Talk talk5 = Talk.normalTalk("Invalid talk",Duration.ofMinutes(300L));

        List<Talk> unscheduledTalks = new ArrayList();
        unscheduledTalks.add(talk1);
        unscheduledTalks.add(talk2);
        unscheduledTalks.add(talk3);
        unscheduledTalks.add(talk4);
        unscheduledTalks.add(talk5);

        Track track = new Track("Track 1");
        final List<Talk> talks = track.scheduleTalks(unscheduledTalks);

        assertThat(talks.size(),is(4));
        assertThat(talks.get(0).endsOn, equalTo(LocalTime.of(11,20)));
        assertThat(talks.get(1).endsOn, equalTo(LocalTime.of(11,50)));
        assertThat(talks.get(2).endsOn, equalTo(LocalTime.of(16,20)));
        assertThat(talks.get(3).endsOn, equalTo(LocalTime.of(16,40)));



    }

    /*************************************************************************************************************************************************************************************/
    /**
     * @TODO
     */




}

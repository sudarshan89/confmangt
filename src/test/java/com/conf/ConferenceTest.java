package com.conf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
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

    /*************************************************************************************************************************************************************************************/
    /**
     * @TODO
     */



}

package com.conf;

import com.google.common.collect.Sets;

import java.util.Set;

import org.joda.time.Duration;
import org.joda.time.LocalTime;

class Track {
    final String name;


    public enum Session {
        MORNING {
            @Override
            public LocalTime startsAt() {
                return LocalTime.parse("T09:00:00");
            }

            @Override
            public LocalTime endsAt() {
                return LocalTime.parse("T12:00:00");
            }
        }, NOON {
            @Override
            public LocalTime startsAt() {
                return LocalTime.parse("T13:00:00");
            }

            @Override
            public LocalTime endsAt() {
                return LocalTime.parse("T17:00:00");
            }
        };

        private Session() {
        }

        public abstract LocalTime startsAt();

        public abstract LocalTime endsAt();

        public boolean isValidEndTime(LocalTime endsOn) {
            return (endsOn.isAfter(startsAt())) && (endsOn.isBefore(endsAt()));
        }

        public boolean isValidStartTime(LocalTime startsOn) {
            return (startsOn.isAfter(startsAt())) && (startsOn.isBefore(endsAt()));
        }
    }

    final Set<Talk> talks = Sets.newHashSet();

    Track(String name) {
        this.name = name;
    }

    Talk findTalk(String talkName) {
        for (Talk talk : this.talks) {
            if (talk.talkName.equals(talkName)) {
                return talk;
            }
        }
        throw new NoTalkFoundException();
    }

    boolean cancelTalk(String talkName) {
        for (Talk talk : this.talks) {
            if (talk.talkName.equals(talkName)) {
                return this.talks.remove(talk);
            }
        }
        return false;
    }

    void scheduleNetworkingEvent(LocalTime startsOn) {
        checkRulesBeforeAddingNetworkingEvent(startsOn);
    }

    private void checkRulesBeforeAddingNetworkingEvent(LocalTime startsOn) {
        checkNetworkingEventStartTime(startsOn);
        checkTalkOverLapping(startsOn, Duration.standardHours(2L));
    }

    private void checkNetworkingEventStartTime(LocalTime startsOn) {
        if (!Talk.isValidNetworkingEventStartTime(startsOn)) {
            throw new InValidNetworkingEventTimingException();
        }
    }

    void scheduleTalk(String talkName, Duration talkDuration, LocalTime startsOn) {
        checkRulesBeforeAddingTalk(talkDuration, startsOn);
        Talk talk = Talk.normalTalk(talkName, talkDuration, startsOn);
        this.talks.add(talk);
    }

    void scheduleShortTalk(String talkName, LocalTime startsOn) {
        checkRulesBeforeAddingTalk(Talk.SHORT_TALK_DURATION, startsOn);
        Talk talk = Talk.shortTalk(talkName, startsOn);
        this.talks.add(talk);
    }

    void checkRulesBeforeAddingTalk(Duration talkDuration, LocalTime startsOn) {
        checkTalkStartTime(startsOn);
        checkTalkEndTime(startsOn, talkDuration);
        checkTalkOverLapping(startsOn, talkDuration);
    }

    private void checkTalkStartTime(LocalTime startsOn) {
        if (!isValidTalkStartTime(startsOn)) {
            throw new InValidTalkTimingsException();
        }
    }

    private boolean isValidTalkStartTime(LocalTime startsOn) {
        return (Session.MORNING.isValidStartTime(startsOn)) || (Session.NOON.isValidStartTime(startsOn));
    }

    private void checkTalkEndTime(LocalTime startsOn, Duration talkDuration) {
        LocalTime endsOn = startsOn.plus(talkDuration.toStandardSeconds());
        if (!isValidTalkEndTime(endsOn)) {
            throw new InValidTalkTimingsException();
        }
    }

    private boolean isValidTalkEndTime(LocalTime endsOn) {
        return (Session.MORNING.isValidEndTime(endsOn)) || (Session.NOON.isValidEndTime(endsOn));
    }

    private void checkTalkOverLapping(LocalTime startsOn, Duration talkDuration) {
        LocalTime proposedEndTimeOfTalk = startsOn.plus(talkDuration.toStandardSeconds());
        for (Talk scheduledTalk : this.talks) {
            if (scheduledTalk.isTalkOverlapping(startsOn, proposedEndTimeOfTalk)) {
                throw new TalkTimingsOverlapException();
            }
        }
    }

    public class InValidNetworkingEventTimingException
            extends RuntimeException {
        public InValidNetworkingEventTimingException() {
        }
    }

    public class TalkTimingsOverlapException
            extends RuntimeException {
        public TalkTimingsOverlapException() {
        }
    }

    public class InValidTalkTimingsException
            extends RuntimeException {
        public InValidTalkTimingsException() {
        }
    }

    public class NoTalkFoundException extends RuntimeException {
    }
}

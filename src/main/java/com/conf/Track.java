package com.conf;


import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

class Track {

    public static final String NETWORKING_SESSION = "Networking Session";
    public static final String EMPTY_TALK = "EMPTY TALK";

    final String name;

    public void resetSessionTimeConsumed() {
        for (Session session : Session.values()) {
            session.resetTimeConsumed();
        }
    }


    public enum Session {
        MORNING {

            private Duration timeConsumed = Duration.ofMinutes(0L);

            @Override
            public LocalTime startsAt() {
                return LocalTime.parse("09:00:00");
            }

            @Override
            public LocalTime endsAt() {
                return LocalTime.parse("12:00:00");
            }

            @Override
            public Duration timeConsumed() {
                return timeConsumed;
            }

            @Override
            public Duration maxDuration() {
                return Duration.ofMinutes(180L);
            }

            public Duration minDuration() {
                return Duration.ofMinutes(180L);
            }

            @Override
            public boolean canScheduleTalk(Duration talkDuration) {
                final Duration spareTime = timeAvailable().minus(talkDuration);
                return spareTime.compareTo(Duration.ofMinutes(0L)) >= 0;

            }

            @Override
            public Duration addTimeConsumed(Duration talkDuration) {
                return timeConsumed = timeConsumed.plus(talkDuration);
            }

            @Override
            public Duration timeAvailable() {
                return maxDuration().minus(timeConsumed);
            }

            @Override
            public Talk scheduleFillerTalk() {
                LocalTime startsOn = startsAt().plusMinutes(timeConsumed().toMinutes());
                Talk fillerTalk = new Talk(EMPTY_TALK, timeAvailable());
                fillerTalk.schedule(startsOn);
                return fillerTalk;
            }

            @Override
            public void resetTimeConsumed() {
                timeConsumed = Duration.ofMinutes(0L);
            }

        }, NOON {
            private Duration timeConsumed = Duration.ofMinutes(0L);

            @Override
            public LocalTime startsAt() {
                return LocalTime.parse("13:00:00");
            }

            @Override
            public LocalTime endsAt() {
                return LocalTime.parse("17:00:00");
            }

            @Override
            public Duration timeConsumed() {
                return timeConsumed;
            }

            @Override
            public Duration maxDuration() {
                return Duration.ofMinutes(240L);
            }

            public Duration minDuration() {
                return Duration.ofMinutes(180L);
            }

            @Override
            public boolean canScheduleTalk(Duration talkDuration) {
                final Duration spareTime = timeAvailable().minus(talkDuration);
                return isGreaterThan(spareTime, Duration.ofMinutes(0L));

            }

            @Override
            public Duration addTimeConsumed(Duration talkDuration) {
                return timeConsumed = timeConsumed.plus(talkDuration);
            }

            @Override
            public Duration timeAvailable() {
                return maxDuration().minus(timeConsumed);
            }

            @Override
            public Talk scheduleFillerTalk() {
                LocalTime startsOn = startsAt().plusMinutes(timeConsumed().toMinutes());
                Duration fillerTalkDuration = Duration.ofMinutes(Math.min(Duration.ofHours(1L).toMinutes(), timeAvailable().toMinutes()));
                Talk networkingEvent = new Talk(NETWORKING_SESSION, fillerTalkDuration);
                if (isNetworkingSessionForOneHour(fillerTalkDuration)) {
                    networkingEvent.schedule(LocalTime.of(16, 0));
                } else {
                    networkingEvent.schedule(startsOn);
                }
                return networkingEvent;
            }

            @Override
            public void resetTimeConsumed() {
                timeConsumed = Duration.ofMinutes(0L);
            }

            private boolean isNetworkingSessionForOneHour(Duration fillerTalkDuration) {
                return fillerTalkDuration.toMinutes() == 60;
            }
        };

        public abstract Duration maxDuration();

        public abstract LocalTime startsAt();

        public abstract LocalTime endsAt();

        public abstract Duration timeConsumed();

        public abstract boolean canScheduleTalk(Duration talkDuration);

        public abstract Duration addTimeConsumed(Duration talkDuration);

        public abstract Duration timeAvailable();

        public abstract Talk scheduleFillerTalk();

        public abstract void resetTimeConsumed();

    }

    final List<Talk> talks = new ArrayList<>();

    Track(String name) {
        this.name = name;
    }

    List<Talk> scheduleTalks(List<Talk> unscheduledTalks) {
        Optional<Talk> smallestTalk = unscheduledTalks.stream().min(Comparator.comparing(unscheduledTalk -> unscheduledTalk.talkDuration));

        for (Session session : Session.values()) {
            while (smallestTalk.isPresent() && session.canScheduleTalk(smallestTalk.get().talkDuration)) {

                final Optional<Talk> talkToSchedule = FindTalkToSchedule(unscheduledTalks, session);

                if (talkToSchedule.isPresent()) {
                    Talk talk = talkToSchedule.get();
                    scheduleTalk(talk, session);
                    unscheduledTalks.remove(talk);
                    smallestTalk = unscheduledTalks.stream().min(Comparator.comparing(unscheduledTalk -> unscheduledTalk.talkDuration));
                } else {
                    break;
                }
            }
        }
        return talks;
    }

    /**
     * @param unscheduledTalks
     * @param session
     * @return
     * @TODO Test
     */
    static Optional<Talk> FindTalkToSchedule(List<Talk> unscheduledTalks, Session session) {
        return unscheduledTalks.stream().filter(unscheduledTalk -> session.canScheduleTalk(unscheduledTalk.talkDuration))
                .max(Comparator.comparing(unscheduledTalk -> unscheduledTalk.talkDuration));
    }

    private void scheduleTalk(Talk talk, Session session) {
        final LocalTime startsOn = session.startsAt().plusMinutes(session.timeConsumed().toMinutes());
        session.addTimeConsumed(talk.talkDuration);
        talk.schedule(startsOn);
        talks.add(talk);

    }

    void scheduleEmptyTalks() {
        for (Session session : Session.values()) {
            final Duration timeAvailableInSession = session.timeAvailable();
            if (isGreaterThan(timeAvailableInSession, Duration.ofMinutes(0L))) {
                final Talk fillerTalk = session.scheduleFillerTalk();
                talks.add(fillerTalk);
            }
        }
    }

    static boolean isGreaterThan(Duration left, Duration right) {
        return left.compareTo(right) > 0;
    }

    @Override
    public String toString() {
        String talksToString="";
        talks.sort((p1, p2) -> p1.startsOn.compareTo(p2.startsOn));
        for (Talk talk : talks) {
            talksToString = talksToString + talk.toString() + "\n";
        }
        return name + "\n" + talksToString;
    }
}

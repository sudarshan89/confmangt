package com.conf;


import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingLong;

class Track {

    public static final String NETWORKING_SESSION = "Networking Session";
    public static final String EMPTY_TALK = "EMPTY TALK";
    private static final Talk LUNCH = Talk.lunch();

    final String name;

    private final Session morning = new Session(LocalTime.parse("09:00:00"),LocalTime.parse("12:00:00"),Duration.ofMinutes(180L));
    private final Session afternoon = new Session(LocalTime.parse("13:00:00"),LocalTime.parse("17:00:00"),Duration.ofMinutes(180L),Duration.ofMinutes(240L));

    class Session {

        private Duration timeConsumed = Duration.ofMinutes(0L);
        final LocalTime startsAt;
        final LocalTime endsAt;
        final Duration maxDuration;
        final Duration minDuration;


        public Session(LocalTime startsAt, LocalTime endsAt, Duration minDuration, Duration maxDuration) {
            this.startsAt = startsAt;
            this.endsAt = endsAt;
            this.minDuration = minDuration;
            this.maxDuration = maxDuration;
        }

        public Session(LocalTime startsAt, LocalTime endsAt, Duration maxDuration) {
            this.startsAt = startsAt;
            this.endsAt = endsAt;
            this.maxDuration = maxDuration;
            this.minDuration = Duration.from(maxDuration);
        }

        public Duration addTimeConsumed(Duration talkDuration) {
            return timeConsumed = timeConsumed.plus(talkDuration);
        }

        public Duration timeAvailable() {
            return maxDuration.minus(timeConsumed);
        }

        public boolean canScheduleTalk(Duration talkDuration) {
            final Duration spareTime = timeAvailable().minus(talkDuration);
            return spareTime.compareTo(Duration.ofMinutes(0L)) >= 0;
        }

        public Talk scheduleFillerTalk() {
            LocalTime startsOn = startsAt.plusMinutes(timeConsumed.toMinutes());
            Talk fillerTalk = new Talk(EMPTY_TALK, timeAvailable());
            fillerTalk.schedule(startsOn);
            return fillerTalk;
        }
    }

    private Collection<Session> allSessions(){
        return asList(morning,afternoon);
    }


    final List<Talk> talks = new ArrayList<>();

    Track(String name) {
        this.name = name;
    }

    List<Talk> scheduleTalks(List<Talk> unscheduledTalks) {
        Optional<Talk> smallestTalk = unscheduledTalks.stream().min(comparing(unscheduledTalk -> unscheduledTalk.talkDuration));

        for (Session session : allSessions()) {
            while (smallestTalk.isPresent() && session.canScheduleTalk(smallestTalk.get().talkDuration)) {

                final Optional<Talk> talkToSchedule = FindTalkToSchedule(unscheduledTalks, session);

                if (talkToSchedule.isPresent()) {
                    Talk talk = talkToSchedule.get();
                    scheduleTalk(talk, session);
                    unscheduledTalks.remove(talk);
                    smallestTalk = unscheduledTalks.stream().min(comparing(unscheduledTalk -> unscheduledTalk.talkDuration));
                } else {
                    break;
                }
            }
        }
        talks.add(LUNCH);
        return talks;
    }

    /**
     * @param unscheduledTalks
     * @param session
     * @return It will pick the talk which can fit into the available time in the session amongst all the candidate talks it will pick
     * the talk where (available time % talk duration) is smallest and if there are multiple talks
     * with the same value, then it will pick the talk with highest duration
     */
    static Optional<Talk> FindTalkToSchedule(List<Talk> unscheduledTalks, Session session) {
        final Stream<Talk> eligibleTalks = unscheduledTalks.stream().filter(unscheduledTalk -> session.canScheduleTalk(unscheduledTalk.talkDuration));
        final Comparator<Talk> byAscendingSessionAvailableTimeModuloTalkDuration = comparing(
                (Talk unscheduledTalk) -> session.timeAvailable().toMinutes() % unscheduledTalk.talkDuration.toMinutes());
        final Comparator<Talk> byDescendingTalkByDuration = comparingLong((Talk unscheduledTalk) -> unscheduledTalk.talkDuration.toMinutes()).reversed();
        final Comparator<Talk> talkComparator = byAscendingSessionAvailableTimeModuloTalkDuration.thenComparing(byDescendingTalkByDuration);
        return eligibleTalks.min(talkComparator);
    }

    private void scheduleTalk(Talk talk, Session session) {
        final LocalTime startsOn = session.startsAt.plusMinutes(session.timeConsumed.toMinutes());
        session.addTimeConsumed(talk.talkDuration);
        talk.schedule(startsOn);
        talks.add(talk);

    }

    void scheduleEmptyTalks() {
        for (Session session : allSessions()) {
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
        String talksToString = "";
        talks.sort((p1, p2) -> p1.startsOn.compareTo(p2.startsOn));
        for (Talk talk : talks) {
            talksToString = talksToString + talk.toString() + "\n";
        }
        return name + "\n" + talksToString;
    }

}

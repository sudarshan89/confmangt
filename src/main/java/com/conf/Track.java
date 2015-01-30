package com.conf;


import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

class Track {

    private static final Talk BUFFER_TALK = new Talk("EMPTY TALK");
    private static final Talk NETWORKING_SESSION = new Talk("Networking Session");

    final String name;


    public enum Session {
        MORNING {

            private Duration timeConsumed = Duration.ofMinutes(0L);

            @Override
            public LocalTime startsAt() {
                return LocalTime.parse("T09:00:00");
            }

            @Override
            public LocalTime endsAt() {
                return LocalTime.parse("T12:00:00");
            }

            @Override
            public Duration timeConsumed() {
                return timeConsumed;
            }

            public Duration maxDuration() {
                return Duration.ofMinutes(180L);
            }

            public Duration minDuration() {
                return Duration.ofMinutes(180L);
            }

            @Override
            public boolean canScheduleTalk(Duration talkDuration) {
                final Duration spareTime = timeAvailable().minus(talkDuration);
                return spareTime.compareTo(Duration.ofMinutes(0L)) > 0;

            }

            @Override
            public Duration addTimeConsumed(Duration talkDuration) {
                return timeConsumed = timeConsumed.plus(talkDuration);
            }

            @Override
            public Duration timeAvailable() {
                return maxDuration().minus(timeConsumed);
            }

        }, NOON {
            private Duration timeConsumed = Duration.ofMinutes(0L);

            @Override
            public LocalTime startsAt() {
                return LocalTime.parse("T13:00:00");
            }

            @Override
            public LocalTime endsAt() {
                return LocalTime.parse("T17:00:00");
            }

            @Override
            public Duration timeConsumed() {
                return timeConsumed;
            }

            public Duration maxDuration() {
                return Duration.ofMinutes(240L);
            }

            public Duration minDuration() {
                return Duration.ofMinutes(180L);
            }

            @Override
            public boolean canScheduleTalk(Duration talkDuration) {
                final Duration spareTime = timeAvailable().minus(talkDuration);
                return isGreaterThan(spareTime,Duration.ofMinutes(0L));

            }

            @Override
            public Duration addTimeConsumed(Duration talkDuration) {
                return timeConsumed = timeConsumed.plus(talkDuration);
            }

            @Override
            public Duration timeAvailable() {
                return maxDuration().minus(timeConsumed);
            }
        };

        private Session() {
        }

        public abstract LocalTime startsAt();

        public abstract LocalTime endsAt();

        public abstract Duration timeConsumed();

        public abstract boolean canScheduleTalk(Duration talkDuration);

        public abstract Duration addTimeConsumed(Duration talkDuration);

        public abstract Duration timeAvailable();

    }

    final List<Talk> talks = new ArrayList<>();

    Track(String name) {
        this.name = name;
    }

    /**
     * @TODO Test
     * @return
     */

    List<Talk> scheduleTalks(List<Talk> unscheduledTalks) {
        Talk smallestTalk =unscheduledTalks.stream().min(Comparator.comparing(unscheduledTalk -> unscheduledTalk.talkDuration)).get();

        for (Session session : Session.values()) {
            while (session.canScheduleTalk(smallestTalk.talkDuration)){

                final Optional<Talk> talkToSchedule = findTalkToSchedule(unscheduledTalks, session);

                if(talkToSchedule.isPresent()){
                    Talk talk = talkToSchedule.get();
                    scheduleTalk(talk, session);
                    unscheduledTalks.remove(talk);
                    smallestTalk =unscheduledTalks.stream().min(Comparator.comparing(unscheduledTalk -> unscheduledTalk.talkDuration)).get();
                }else{
                    break;
                }
            }

        }

        return talks;
    }

    /**
     * @TODO Test
     * @param unscheduledTalks
     * @param session
     * @return
     */
    Optional<Talk> findTalkToSchedule(List<Talk> unscheduledTalks, Session session) {
        return unscheduledTalks.stream().filter(unscheduledTalk -> session.canScheduleTalk(unscheduledTalk.talkDuration))
                            .max(Comparator.comparing(unscheduledTalk -> unscheduledTalk.talkDuration));
    }

    private void scheduleTalk(Talk talk, Session session) {
        session.addTimeConsumed(talk.talkDuration);
        final LocalTime startsOn = session.startsAt().plus(session.timeConsumed());
        talk.schedule(startsOn);
        talks.add(talk);

    }

    public class InValidNetworkingEventTimingException
            extends RuntimeException {
        public InValidNetworkingEventTimingException() {
        }
    }

    public void scheduleNetworkingEvent() {

    }

    public void scheduleEmptyTalks() {

    }


    static boolean isGreaterThan(Duration left,Duration right){
        return left.compareTo(right) > 0;
    }

    static boolean isLesserThan(Duration left,Duration right){
        return left.compareTo(right) < 0;
    }

    public static void main(String[] args) {
        System.out.println(isLesserThan(Duration.ofMinutes(1),Duration.ofMinutes(2)));
    }

}

package com.conf;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Created by s.sreenivasan on 1/9/2016.
 */
class Session {

    public static final String NETWORKING_SESSION = "Networking Session";
    public static final String EMPTY_TALK = "EMPTY TALK";

    private Duration timeConsumed = Duration.ofMinutes(0L);
    final LocalTime startsAt;
    final LocalTime endsAt;
    final Duration maxDuration;
    final Duration minDuration;
    SessionType sessionType;

    private enum SessionType {
        MORNING {
            @Override
            Talk scheduleFillerTalk(final LocalTime startsAt, final Duration timeConsumed, final Duration timeAvailable) {
                LocalTime startsOn = startsAt.plusMinutes(timeConsumed.toMinutes());
                Talk fillerTalk = new Talk(EMPTY_TALK, timeAvailable);
                fillerTalk.schedule(startsOn);
                return fillerTalk;
            }
        }, NOON {
            @Override
            Talk scheduleFillerTalk(final LocalTime startsAt, final Duration timeConsumed, final Duration timeAvailable) {
                LocalTime startsOn = startsAt.plusMinutes(timeConsumed.toMinutes());
                Duration fillerTalkDuration = Duration.ofMinutes(Math.min(Duration.ofHours(1L).toMinutes(), timeAvailable.toMinutes()));
                Talk networkingEvent = new Talk(NETWORKING_SESSION, fillerTalkDuration);
                if (isNetworkingSessionForOneHour(fillerTalkDuration)) {
                    networkingEvent.schedule(LocalTime.of(16, 0));
                } else {
                    networkingEvent.schedule(startsOn);
                }
                return networkingEvent;
            }

            private boolean isNetworkingSessionForOneHour(Duration fillerTalkDuration) {
                return fillerTalkDuration.toMinutes() == 60;
            }
        };

        abstract Talk scheduleFillerTalk(final LocalTime startsAt, final Duration timeConsumed, final Duration timeAvailable);
    }

    public static Session MorningSession(LocalTime startsAt, LocalTime endsAt, Duration minDuration) {
        return new Session(startsAt, endsAt, minDuration, minDuration, SessionType.MORNING);
    }

    public static Session NoonSession(LocalTime startsAt, LocalTime endsAt, Duration minDuration, Duration maxDuration) {
        return new Session(startsAt, endsAt, minDuration, maxDuration, SessionType.NOON);
    }

    Session(LocalTime startsAt, LocalTime endsAt, Duration minDuration, Duration maxDuration, SessionType sessionType) {
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.sessionType = sessionType;
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
        return sessionType.scheduleFillerTalk(startsAt, timeConsumed, timeAvailable());
    }

    public Duration getTimeConsumed() {
        return timeConsumed;
    }
}

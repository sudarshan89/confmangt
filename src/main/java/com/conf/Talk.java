package com.conf;


import java.time.Duration;
import java.time.LocalTime;

class Talk {
    static final Duration SHORT_TALK_DURATION = Duration.ofMinutes(5L);
    String talkName;
    Duration talkDuration;
    LocalTime startsOn;
    LocalTime endsOn;

    static Talk normalTalk(String talkName, Duration talkDuration) {
        return new Talk(talkName, talkDuration);
    }

    static Talk shortTalk(String talkName) {
        return new Talk(talkName, SHORT_TALK_DURATION);
    }

    static Talk lunch() {
        final Talk lunch = new Talk("Lunch", Duration.ofHours(1L));
        lunch.schedule(LocalTime.of(12, 00));
        return lunch;
    }

    Talk(String talkName, Duration talkDuration) {
        this.talkName = talkName;
        this.talkDuration = talkDuration;
    }

    public void schedule(LocalTime startsOn) {
        this.startsOn = startsOn;
        this.endsOn = startsOn.plus(talkDuration);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        Talk talk = (Talk) o;
        if (this.talkName != null ? !this.talkName.equals(talk.talkName) : talk.talkName != null) {
            return false;
        }
        return true;
    }

    public LocalTime getStartsOn() {
        return LocalTime.from(startsOn);
    }

    public int hashCode() {
        return this.talkName != null ? this.talkName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return startsOn + " " + talkName + " " + talkDuration.toMinutes() + "min";
    }
}

package com.conf;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Duration;
import org.joda.time.LocalTime;

public class Conference {
    private Integer noOfTracks;
    private final String name;
    private List<Track> tracks;

    public Conference(String name, int noOfTracks) {
        this.noOfTracks = Integer.valueOf(noOfTracks);
        this.name = name;
        this.tracks = new ArrayList(noOfTracks);
    }

    public static Conference Plan(String name, int noOfTracks) {
        Conference conference = new Conference(name, noOfTracks);
        return conference;
    }

    public Integer noOfTracks() {
        return this.noOfTracks;
    }

    public boolean addTrack(String name) {
        if (this.tracks.size() < noOfTracks().intValue()) {
            return this.tracks.add(new Track(name));
        }
        return false;
    }

    public void scheduleTalk(String trackName, String talkName, Duration talkDuration, LocalTime startsOn) {
        Track track = findTrack(trackName);
        track.scheduleTalk(talkName, talkDuration, startsOn);
    }

    public void scheduleShortTalk(String trackName, String talkName, LocalTime startsOn) {
        Track track = findTrack(trackName);
        track.scheduleShortTalk(talkName, startsOn);
    }

    public void scheduleNetworkingEvent(String trackName, LocalTime startsOn) {
        Track track = findTrack(trackName);
        track.scheduleNetworkingEvent(startsOn);
    }

    public void rescheduleTalk(String trackName, String talkName, LocalTime startsOn) {
        Track track = findTrack(trackName);
        Talk rescheduledTalk = track.findTalk(talkName);
        track.cancelTalk(talkName);
        if (rescheduledTalk.isShortTalk()) {
            scheduleShortTalk(trackName, talkName, startsOn);
        } else {
            scheduleTalk(trackName, talkName, rescheduledTalk.talkDuration, startsOn);
        }
    }

    Track findTrack(String trackName) {
        for (Track track : this.tracks) {
            if (track.name.equals(trackName)) {
                return track;
            }
        }
        throw new NoTrackFoundException();
    }

    public boolean cancelTalk(String trackName, String talkName) {
        Track track = findTrack(trackName);
        return track.cancelTalk(talkName);
    }

    public class NoTrackFoundException extends RuntimeException {
    }
}
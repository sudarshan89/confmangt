package com.conf;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


/**
 * Lets move to lambdas
 */
public class Conference {
    public static final String CONFERENCE_NAME = "ThoughWorks Conference";
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
        for (int i = 0; i < noOfTracks; i++) {
            conference.addTrack("Track " + i + " : ");

        }
        return conference;
    }

    public Integer noOfTracks() {
        return this.noOfTracks;
    }

    void addTrack(String name) {
        this.tracks.add(new Track(name));
    }

    public void scheduleTalk(String trackName, String talkName, Duration talkDuration, LocalTime startsOn) {
    }

    public void scheduleNetworkingEvent(String trackName, LocalTime startsOn) {

    }

    private void scheduleTalks(List<Talk> unscheduledTalks) {
            for (Track track : tracks) {
                final List<Talk> talksScheduledInThisTrack = track.scheduleTalks(unscheduledTalks);
                unscheduledTalks.removeAll(talksScheduledInThisTrack);
                track.scheduleEmptyTalks();
                track.scheduleNetworkingEvent();
            }
        unscheduledTalks.forEach(System.out::println);

    }

    public static void Kickstart(Stream<String> lines, int numberOfTracks) {
        final List<Talk> unscheduledTalks = MapStringsIntoTalks(lines);
        Conference conference = Conference.Plan(CONFERENCE_NAME, numberOfTracks);
        conference.scheduleTalks(unscheduledTalks);

    }

    static List<Talk> MapStringsIntoTalks(Stream<String> lines) {
        return lines.map(line -> {
            String trimmedLine = line.trim();
            int lastSpaceIndex = line.lastIndexOf(" ");
            if (lastSpaceIndex == -1)
                throw new InvalidTalkNameException("Invalid talk name in file " + line);

            String talkName = trimmedLine.substring(0, lastSpaceIndex);
            String duration = trimmedLine.substring(lastSpaceIndex + 1);
            if (duration.endsWith("min")) {
                String minutes = duration.replaceFirst("min", "");
                return Talk.normalTalk(talkName, Duration.ofMinutes(Long.valueOf(minutes)));
            } else if (duration.endsWith("lightning")) {
                return Talk.shortTalk(talkName);

            } else {
                throw new InvalidTalkNameException("Invalid talk name in file " + line);
            }
        }).collect(toList());

    }

    private static class InvalidTalkNameException extends RuntimeException {
        public InvalidTalkNameException(String description) {
            super(description);
        }
    }


    public class NoTrackFoundException extends RuntimeException {
    }
}
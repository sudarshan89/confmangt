/*  1:   */ package com.conf;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.joda.time.Duration;
/*  6:   */ import org.joda.time.LocalTime;
/*  7:   */ 
/*  8:   */ public class Conference
/*  9:   */ {
/* 10:   */   private Integer noOfTracks;
/* 11:   */   private final String name;
/* 12:   */   private List<Track> tracks;
/* 13:   */   
/* 14:   */   public Conference(String name, int noOfTracks)
/* 15:   */   {
/* 16:20 */     this.noOfTracks = Integer.valueOf(noOfTracks);
/* 17:21 */     this.name = name;
/* 18:22 */     this.tracks = new ArrayList(noOfTracks);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public static Conference Plan(String name, int noOfTracks)
/* 22:   */   {
/* 23:26 */     Conference conference = new Conference(name, noOfTracks);
/* 24:27 */     return conference;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Integer noOfTracks()
/* 28:   */   {
/* 29:31 */     return this.noOfTracks;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean addTrack(String name)
/* 33:   */   {
/* 34:35 */     if (this.tracks.size() < noOfTracks().intValue()) {
/* 35:36 */       return this.tracks.add(new Track(name));
/* 36:   */     }
/* 37:38 */     return false;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void scheduleTalk(String trackName, String talkName, Duration talkDuration, LocalTime startsOn)
/* 41:   */   {
/* 42:42 */     Track track = findTrack(trackName);
/* 43:43 */     track.scheduleTalk(talkName, talkDuration, startsOn);
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void scheduleShortTalk(String trackName, String talkName, LocalTime startsOn)
/* 47:   */   {
/* 48:47 */     Track track = findTrack(trackName);
/* 49:48 */     track.scheduleShortTalk(talkName, startsOn);
/* 50:   */   }
/* 51:   */   
/* 52:   */   public void scheduleNetworkingEvent(String trackName, LocalTime startsOn)
/* 53:   */   {
/* 54:52 */     Track track = findTrack(trackName);
/* 55:53 */     track.scheduleNetworkingEvent(startsOn);
/* 56:   */   }
/* 57:   */   
/* 58:   */   public void rescheduleTalk(String trackName, String talkName, LocalTime startsOn)
/* 59:   */   {
/* 60:63 */     Track track = findTrack(trackName);
/* 61:64 */     Talk rescheduledTalk = track.findTalk(talkName);
/* 62:65 */     track.cancelTalk(talkName);
/* 63:66 */     if (rescheduledTalk.isShortTalk()) {
/* 64:67 */       scheduleShortTalk(trackName, talkName, startsOn);
/* 65:   */     } else {
/* 66:69 */       scheduleTalk(trackName, talkName, rescheduledTalk.talkDuration, startsOn);
/* 67:   */     }
/* 68:   */   }
/* 69:   */   
/* 70:   */   Track findTrack(String trackName)
/* 71:   */   {
/* 72:76 */     for (Track track : this.tracks) {
/* 73:77 */       if (track.name.equals(trackName)) {
/* 74:78 */         return track;
/* 75:   */       }
/* 76:   */     }
/* 77:81 */     throw new RuntimeException();
/* 78:   */   }
/* 79:   */   
/* 80:   */   public boolean cancelTalk(String trackName, String talkName)
/* 81:   */   {
/* 82:85 */     Track track = findTrack(trackName);
/* 83:86 */     return track.cancelTalk(talkName);
/* 84:   */   }
/* 85:   */ }


/* Location:           F:\codebases\codekata\confmangt\target\classes\
 * Qualified Name:     com.conf.Conference
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package com.conf;
/*  2:   */ 
/*  3:   */ import org.joda.time.Duration;
/*  4:   */ import org.joda.time.LocalTime;
/*  5:   */ 
/*  6:   */ class Talk
/*  7:   */ {
/*  8:12 */   static final Duration SHORT_TALK_DURATION = Duration.standardMinutes(5L);
/*  9:   */   String talkName;
/* 10:   */   Duration talkDuration;
/* 11:   */   private LocalTime startsOn;
/* 12:   */   LocalTime endsOn;
/* 13:   */   
/* 14:   */   static Talk normalTalk(String talkName, Duration talkDuration, LocalTime startsOn)
/* 15:   */   {
/* 16:20 */     return new Talk(talkName, talkDuration, startsOn);
/* 17:   */   }
/* 18:   */   
/* 19:   */   static Talk shortTalk(String talkName, LocalTime startsOn)
/* 20:   */   {
/* 21:24 */     return new Talk(talkName, SHORT_TALK_DURATION, startsOn);
/* 22:   */   }
/* 23:   */   
/* 24:   */   Talk(String talkName, Duration talkDuration, LocalTime startsOn)
/* 25:   */   {
/* 26:28 */     this.talkName = talkName;
/* 27:29 */     this.talkDuration = talkDuration;
/* 28:30 */     this.startsOn = startsOn;
/* 29:31 */     this.endsOn = startsOn.plus(talkDuration.toStandardSeconds());
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean isTalkOverlapping(LocalTime proposedStartTimeOfTalk, LocalTime proposedEndsTimeOfTalk)
/* 33:   */   {
/* 34:35 */     return ((proposedStartTimeOfTalk.isAfter(this.startsOn)) && (proposedStartTimeOfTalk.isBefore(this.endsOn))) || ((proposedStartTimeOfTalk.isBefore(this.startsOn)) && (proposedEndsTimeOfTalk.isAfter(this.startsOn)));
/* 35:   */   }
/* 36:   */   
/* 37:   */   public boolean isShortTalk()
/* 38:   */   {
/* 39:40 */     return this.talkDuration.equals(SHORT_TALK_DURATION);
/* 40:   */   }
/* 41:   */   
/* 42:   */   public boolean equals(Object o)
/* 43:   */   {
/* 44:45 */     if (this == o) {
/* 45:45 */       return true;
/* 46:   */     }
/* 47:46 */     if ((o == null) || (getClass() != o.getClass())) {
/* 48:46 */       return false;
/* 49:   */     }
/* 50:48 */     Talk talk = (Talk)o;
/* 51:50 */     if (this.talkName != null ? !this.talkName.equals(talk.talkName) : talk.talkName != null) {
/* 52:50 */       return false;
/* 53:   */     }
/* 54:52 */     return true;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public int hashCode()
/* 58:   */   {
/* 59:57 */     return this.talkName != null ? this.talkName.hashCode() : 0;
/* 60:   */   }
/* 61:   */   
/* 62:   */   static boolean isValidNetworkingEventStartTime(LocalTime startsOn)
/* 63:   */   {
/* 64:61 */     return (startsOn.isAfter(LocalTime.parse("T16:00:00"))) && (startsOn.isBefore(LocalTime.parse("T17:00:00")));
/* 65:   */   }
/* 66:   */ }


/* Location:           F:\codebases\codekata\confmangt\target\classes\
 * Qualified Name:     com.conf.Talk
 * JD-Core Version:    0.7.0.1
 */
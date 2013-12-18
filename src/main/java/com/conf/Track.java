/*   1:    */ package com.conf;
/*   2:    */ 
/*   3:    */ import com.google.common.collect.Sets;
/*   4:    */ import java.util.Set;
/*   5:    */ import org.joda.time.Duration;
/*   6:    */ import org.joda.time.LocalTime;
/*   7:    */ 
/*   8:    */ class Track
/*   9:    */ {
/*  10:    */   final String name;
/*  11:    */   
/*  12:    */   public class InValidNetworkingEventTimingException
/*  13:    */     extends RuntimeException
/*  14:    */   {
/*  15:    */     public InValidNetworkingEventTimingException() {}
/*  16:    */   }
/*  17:    */   
/*  18:    */   public class TalkTimingsOverlapException
/*  19:    */     extends RuntimeException
/*  20:    */   {
/*  21:    */     public TalkTimingsOverlapException() {}
/*  22:    */   }
/*  23:    */   
/*  24:    */   public class InValidTalkTimingsException
/*  25:    */     extends RuntimeException
/*  26:    */   {
/*  27:    */     public InValidTalkTimingsException() {}
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static abstract enum Session
/*  31:    */   {
/*  32: 17 */     MORNING,  NOON;
/*  33:    */     
/*  34:    */     private Session() {}
/*  35:    */     
/*  36:    */     public abstract LocalTime startsAt();
/*  37:    */     
/*  38:    */     public abstract LocalTime endsAt();
/*  39:    */     
/*  40:    */     public boolean isValidEndTime(LocalTime endsOn)
/*  41:    */     {
/*  42: 46 */       return (endsOn.isAfter(startsAt())) && (endsOn.isBefore(endsAt()));
/*  43:    */     }
/*  44:    */     
/*  45:    */     public boolean isValidStartTime(LocalTime startsOn)
/*  46:    */     {
/*  47: 50 */       return (startsOn.isAfter(startsAt())) && (startsOn.isBefore(endsAt()));
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51: 56 */   final Set<Talk> talks = Sets.newHashSet();
/*  52:    */   
/*  53:    */   Track(String name)
/*  54:    */   {
/*  55: 59 */     this.name = name;
/*  56:    */   }
/*  57:    */   
/*  58:    */   Talk findTalk(String talkName)
/*  59:    */   {
/*  60: 64 */     for (Talk talk : this.talks) {
/*  61: 65 */       if (talk.talkName.equals(talkName)) {
/*  62: 66 */         return talk;
/*  63:    */       }
/*  64:    */     }
/*  65: 69 */     throw new IllegalStateException();
/*  66:    */   }
/*  67:    */   
/*  68:    */   boolean cancelTalk(String talkName)
/*  69:    */   {
/*  70: 73 */     for (Talk talk : this.talks) {
/*  71: 74 */       if (talk.talkName.equals(talkName)) {
/*  72: 75 */         return this.talks.remove(talk);
/*  73:    */       }
/*  74:    */     }
/*  75: 78 */     return false;
/*  76:    */   }
/*  77:    */   
/*  78:    */   void scheduleNetworkingEvent(LocalTime startsOn)
/*  79:    */   {
/*  80: 86 */     checkRulesBeforeAddingNetworkingEvent(startsOn);
/*  81:    */   }
/*  82:    */   
/*  83:    */   private void checkRulesBeforeAddingNetworkingEvent(LocalTime startsOn)
/*  84:    */   {
/*  85: 91 */     checkNetworkingEventStartTime(startsOn);
/*  86: 92 */     checkTalkOverLapping(startsOn, Duration.standardHours(2L));
/*  87:    */   }
/*  88:    */   
/*  89:    */   private void checkNetworkingEventStartTime(LocalTime startsOn)
/*  90:    */   {
/*  91: 96 */     if (!Talk.isValidNetworkingEventStartTime(startsOn)) {
/*  92: 97 */       throw new InValidNetworkingEventTimingException();
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   void scheduleTalk(String talkName, Duration talkDuration, LocalTime startsOn)
/*  97:    */   {
/*  98:103 */     checkRulesBeforeAddingTalk(talkDuration, startsOn);
/*  99:104 */     Talk talk = Talk.normalTalk(talkName, talkDuration, startsOn);
/* 100:105 */     this.talks.add(talk);
/* 101:    */   }
/* 102:    */   
/* 103:    */   void scheduleShortTalk(String talkName, LocalTime startsOn)
/* 104:    */   {
/* 105:109 */     checkRulesBeforeAddingTalk(Talk.SHORT_TALK_DURATION, startsOn);
/* 106:110 */     Talk talk = Talk.shortTalk(talkName, startsOn);
/* 107:111 */     this.talks.add(talk);
/* 108:    */   }
/* 109:    */   
/* 110:    */   void checkRulesBeforeAddingTalk(Duration talkDuration, LocalTime startsOn)
/* 111:    */   {
/* 112:115 */     checkTalkStartTime(startsOn);
/* 113:116 */     checkTalkEndTime(startsOn, talkDuration);
/* 114:117 */     checkTalkOverLapping(startsOn, talkDuration);
/* 115:    */   }
/* 116:    */   
/* 117:    */   private void checkTalkStartTime(LocalTime startsOn)
/* 118:    */   {
/* 119:121 */     if (!isValidTalkStartTime(startsOn)) {
/* 120:122 */       throw new InValidTalkTimingsException();
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   private boolean isValidTalkStartTime(LocalTime startsOn)
/* 125:    */   {
/* 126:127 */     return (Session.MORNING.isValidStartTime(startsOn)) || (Session.NOON.isValidStartTime(startsOn));
/* 127:    */   }
/* 128:    */   
/* 129:    */   private void checkTalkEndTime(LocalTime startsOn, Duration talkDuration)
/* 130:    */   {
/* 131:131 */     LocalTime endsOn = startsOn.plus(talkDuration.toStandardSeconds());
/* 132:132 */     if (!isValidTalkEndTime(endsOn)) {
/* 133:133 */       throw new InValidTalkTimingsException();
/* 134:    */     }
/* 135:    */   }
/* 136:    */   
/* 137:    */   private boolean isValidTalkEndTime(LocalTime endsOn)
/* 138:    */   {
/* 139:138 */     return (Session.MORNING.isValidEndTime(endsOn)) || (Session.NOON.isValidEndTime(endsOn));
/* 140:    */   }
/* 141:    */   
/* 142:    */   private void checkTalkOverLapping(LocalTime startsOn, Duration talkDuration)
/* 143:    */   {
/* 144:142 */     LocalTime proposedEndTimeOfTalk = startsOn.plus(talkDuration.toStandardSeconds());
/* 145:143 */     for (Talk scheduledTalk : this.talks) {
/* 146:144 */       if (scheduledTalk.isTalkOverlapping(startsOn, proposedEndTimeOfTalk)) {
/* 147:145 */         throw new TalkTimingsOverlapException();
/* 148:    */       }
/* 149:    */     }
/* 150:    */   }
/* 151:    */ }


/* Location:           F:\codebases\codekata\confmangt\target\classes\
 * Qualified Name:     com.conf.Track
 * JD-Core Version:    0.7.0.1
 */
//package sk.tuke.gamestudio.service;
//
//import org.junit.jupiter.api.Test;
//import sk.tuke.gamestudio.entity.Score;
//
//import java.sql.Timestamp;
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class ScoreServiceTest {
//    private ScoreService scoreService = new ScoreServiceJDBC();
//    @Test
//    public void reset() {
//        scoreService.reset();
//        assertEquals(0, scoreService.getTopScores("mines").size());
//    }
//
//    @Test
//    public void addScore() {
//        var timestamp = new Timestamp(System.currentTimeMillis());
//        var score = new Score("Jaro", "mines", 120, timestamp);
//        scoreService.reset();
//        scoreService.addScore(score);
//        var scores = scoreService.getTopScores("mines");
//        assertEquals(1, scores.size());
//        assertEquals(score.toString(), scores.get(0).toString());
//    }
//
//    @Test
//    public void getTopScores() {
//        var timestamp = new Timestamp(System.currentTimeMillis());
//        scoreService.reset();
//        scoreService.addScore(new Score("Jaro", "mines", 200, timestamp));
//        scoreService.addScore(new Score("Jozo", "mines", 250, timestamp));
//        scoreService.addScore(new Score("Anca", "mines", 150, timestamp));
//
//        var scores = scoreService.getTopScores("mines");
//        assertEquals(3, scores.size());
//        assertEquals("Jozo", scores.get(0).getPlayer());
//        assertEquals(250, scores.get(0).getPoints());
//        assertEquals("Jaro", scores.get(1).getPlayer());
//        assertEquals(200, scores.get(1).getPoints());
//        assertEquals("Anca", scores.get(2).getPlayer());
//        assertEquals(150, scores.get(2).getPoints());
//    }
//}

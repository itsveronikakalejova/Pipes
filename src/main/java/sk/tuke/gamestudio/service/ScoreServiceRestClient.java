package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Score;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScoreServiceRestClient implements ScoreService {
    private final String url = "http://localhost:8080/api/score";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addScore(Score score) {
        restTemplate.postForEntity(url, score, Score.class);
    }

    @Override
    public List<Score> getTopScores(String gameName) {
        ResponseEntity<Score[]> scoreListEntity = restTemplate.getForEntity(url + "/" + gameName, Score[].class);
        if (scoreListEntity.getBody() != null) {
            return Arrays.asList(scoreListEntity.getBody());
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    public Score getScore(String gameName, String playerName) {
        ResponseEntity<Score> scoreEntity = restTemplate.getForEntity(url + "/" + gameName + "/" + playerName, Score.class);
        Score score = scoreEntity.getBody();
        if (score != null) {
            return score;
        } else {
            System.out.println("No score found for player: " + playerName);
            return null;
        }
    }


    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
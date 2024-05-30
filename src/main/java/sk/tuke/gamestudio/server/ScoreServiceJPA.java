package sk.tuke.gamestudio.server;

import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreException;
import sk.tuke.gamestudio.service.ScoreService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) throws ScoreException {
        try {
            Score existingScore = entityManager.createNamedQuery("Score.getScoreByPlayer", Score.class)
                    .setParameter("game", score.getGame())
                    .setParameter("player", score.getPlayer())
                    .getSingleResult();

            existingScore.setPoints(existingScore.getPoints() + score.getPoints());
            entityManager.merge(existingScore);
        } catch (NoResultException ex) {
            entityManager.merge(score);
        }
    }


    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        TypedQuery<Score> query = entityManager.createNamedQuery("Score.getTopScores", Score.class);
        return query.setParameter("game", game).setMaxResults(10).getResultList();
    }

    @Override
    public Score getScore(String game, String playerName) {

        Score score = (Score) entityManager.createNamedQuery("Score.getScoreByPlayer")
                .setParameter("game", game)
                .setParameter("player", playerName)
                .getSingleResult();
        if (score != null) {
            return score;
        }
        return null;
    }


    @Override
    public void reset() {
        entityManager.createNamedQuery("Score.resetScores").executeUpdate();
    }
}
package sk.tuke.gamestudio.server;

import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        try {
            Rating existingRating = entityManager.createNamedQuery("Rating.getRatingByPlayer", Rating.class)
                    .setParameter("game", rating.getGame())
                    .setParameter("player", rating.getPlayer())
                    .getSingleResult();

            existingRating.setRating(rating.getRating());
            entityManager.merge(existingRating);
        } catch (NoResultException ex) {
            entityManager.merge(rating);
        }
    }


    @Override
    public int getAverageRating(String game) throws RatingException {
        return ((Number) entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game)
                .getSingleResult()).intValue();
    }


    @Override
    public int getRating(String game, String player) throws RatingException {
        Rating rating = (Rating) entityManager.createNamedQuery("Rating.getRatingByPlayer")
                .setParameter("game", game)
                .setParameter("player", player)
                .getSingleResult();
        if (rating != null) {
            return rating.getRating();
        }
        return 0;
    }


    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRating").executeUpdate();
    }
}

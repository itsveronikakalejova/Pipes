package sk.tuke.gamestudio.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
@Getter
@Setter
@NamedQuery(name = "Rating.getAverageRating",
        query = "SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game")
@NamedQuery( name = "Rating.resetRating",
        query = "DELETE FROM Rating")
@NamedQuery(
        name = "Rating.getRatingByPlayer",
        query = "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player"
)
@Entity
public class Rating implements Serializable {
    @Id
    @GeneratedValue
    private int ident;

    private String player;
    private String game;
    private int rating;
    private Date ratedOn;

    public Rating() {
    }

    public Rating(String player, String game, int rating, Date ratedOn) {
        this.game = game;
        this.player = player;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

    @Override
    public String toString() {
        return "\u001B[33m" + "Score: " +
                "game = " + getGame() +
                " | player = " + getPlayer() +
                " | rating = " + getRating() +
                " | ratedOn = " + getRatedOn() +
                "\u001B[0m";
    }
}

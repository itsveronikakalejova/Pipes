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
@NamedQuery( name = "Score.getTopScores",
        query = "SELECT s FROM Score s WHERE s.game=:game ORDER BY s.points DESC")
@NamedQuery( name = "Score.resetScores",
        query = "DELETE FROM Score")
@NamedQuery( name = "Score.getScoreByPlayer",
        query = "SELECT s FROM Score s WHERE s.game = :game AND s.player = :player"
)

@Entity
public class Score implements Serializable {
    @Id
    @GeneratedValue
    private int ident;
    private String player;
    private String game;
    private int points;
    private Date playedOn;

    public Score() {
    }

    public Score(String player, String game, int points, Date playedOn) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedOn = playedOn;
    }

    @Override
    public String toString() {
        return "\u001B[33m" + "Score: " +
                "game = " + getGame() +
                " | player = " + getPlayer() +
                " | points = " + getPoints() +
                " | playedOn = " + getPlayedOn() +
                "\u001B[0m";
    }
}

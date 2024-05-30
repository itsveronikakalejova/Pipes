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
@NamedQuery(
        name = "Comment.getComments",
        query = "SELECT c FROM Comment c WHERE c.game = :game"
)
@NamedQuery( name = "Comment.resetComments",
        query = "DELETE FROM Comment")
@Entity
public class Comment implements Serializable {
    @Id
    @GeneratedValue
    private int ident;
    private String player;
    private String game;
    private String comment;
    private Date commentedOn;

    public Comment() {
    }

    public Comment(String game, String player, String comment, Date commentedOn) {
        this.game = game;
        this.player = player;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

    @Override
    public String toString() {
        return "\u001B[33m" + "Score: " +
                "game = " + getGame() +
                " | player = " + getPlayer() +
                " | comment = " + getComment() +
                " | commentedOn = " + getCommentedOn() +
                "\u001B[0m";
    }
}

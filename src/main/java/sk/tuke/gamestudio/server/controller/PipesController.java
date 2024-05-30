package sk.tuke.gamestudio.server.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.game.pipes.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.pipes.core.*;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.ScoreService;

import javax.persistence.NoResultException;
import java.io.FileDescriptor;
import java.util.Date;
import java.util.List;

@Getter
@Controller
@Qualifier("pipesController")
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/pipes")
public class PipesController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserController userController;

    private Field field = new Field(1);
    private int points = 0;
    private Score newScore;
    @RequestMapping
    public String pipes(@RequestParam(required = false) String row, @RequestParam(required = false) String column, Model model) {
        try {
            field.turnPipe(field.getArray()[Integer.parseInt(row)][Integer.parseInt(column)]);
        } catch (Exception e) {
            // Handle the exception if needed
        }

        String playerName = getPlayerName();
        System.out.println(playerName);

        if (field.getGameState() != GameState.SOLVED && field.getGameState() != GameState.OUT_OF_MOVES) {
            model.addAttribute("htmlField", getHtmlField());
            model.addAttribute("scores", scoreService.getTopScores("pipes"));
            model.addAttribute("comments", commentService.getComments("pipes"));
            model.addAttribute("gameState", field.getGameState());
        } else if (field.getGameState() == GameState.SOLVED) {
            try {
                newScore = scoreService.getScore("pipes", playerName);
                setPoints(playerName, getScore());
                model.addAttribute("playerScore", newScore != null ? newScore.getPoints() : 0);
            } catch (NoResultException e) {
                setPoints(playerName, 0);
                model.addAttribute("playerScore", 0);
            }
            model.addAttribute("playerName", playerName);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Handle interruption if needed
            }
            return "redirect:/pipes/winner";
        } else if (field.getGameState() == GameState.OUT_OF_MOVES) {
            return "redirect:/login";
        }
        return "pipes";
    }

    private String getPlayerName() {
        if (userController.getLoggedUser() == null) {
            return "Anonymous";
        }
        return userController.getLoggedUser().getLogin();
    }

    public int getScore() {
        return newScore != null ? newScore.getPoints() : 0;
    }

    @RequestMapping("/new")
    public String newGame(Model model) {
        field = new Field(1);
        model.addAttribute("htmlField", getHtmlField());
        model.addAttribute("scores", scoreService.getTopScores("pipes"));
        model.addAttribute("gameState", field.getGameState());
        return "pipes";
    }

    @RequestMapping("/winner")
    public String winnerPage(Model model) {
        model.addAttribute("scores", commentService.getComments("pipes"));
        return "winner"; // Return the view name for the winner page
    }

    @RequestMapping("/comments")
    public String showComments(Model model) {
        List<Comment> comments = commentService.getComments("pipes");
        model.addAttribute("comments", comments);
        return "comments";
    }

    @PostMapping("/submitComment")
    public String submitComment(@RequestParam(required = false) String commentText) {
        Comment comment = new Comment("pipes", getPlayerName(), commentText, new Date());
        commentService.addComment(comment);

        return "redirect:/pipes/comments";
    }

    private String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='field'>\n");
        sb.append("<table class='game-table'>\n");
        for (int i = 0; i < field.getSize(); i++) {
            sb.append("<tr>\n");
            for (int j = 0; j < field.getSize(); j++) {
                Pipe pipe = field.getArray()[i][j];
                sb.append("<td class='game-cell'>\n");
                sb.append("<a href='/pipes?row=" + i + "&column=" + j + "'>\n");
                sb.append("<img src='/images.pipes/" + getImageName(pipe) + ".png' class='pipes'>\n");
                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        sb.append("</div>\n");
        return sb.toString();
    }

    private String getImageName(Pipe pipe) {
        // CORNER pipe
        if (pipe.getPipeType() == PipeType.CORNER) {
            if (pipe.getDirection() == Direction.TOP) {
                return "pipe_corner_1";
            } else if (pipe.getDirection() == Direction.RIGHT) {
                return "pipe_corner_2";
            } else if (pipe.getDirection() == Direction.BOTTOM) {
                return "pipe_corner_3";
            } else if (pipe.getDirection() == Direction.LEFT) {
                return "pipe_corner_4";
            }
        }
        // STRAIGHT pipe
        else if (pipe.getPipeType() == PipeType.STRAIGHT) {
            if (pipe.getDirection() == Direction.TOP) {
                return "pipe_straight_2";
            } else if (pipe.getDirection() == Direction.RIGHT) {
                return "pipe_straight_1";
            } else if (pipe.getDirection() == Direction.BOTTOM) {
                return "pipe_straight_2";
            } else if (pipe.getDirection() == Direction.LEFT) {
                return "pipe_straight_1";
            }
        }
        // FORK pipe
        else if (pipe.getPipeType() == PipeType.FORK) {
            if (pipe.getDirection() == Direction.TOP) {
                return "pipe_fork_4";
            } else if (pipe.getDirection() == Direction.RIGHT) {
                return "pipe_fork_3";
            } else if (pipe.getDirection() == Direction.BOTTOM) {
                return "pipe_fork_2";
            } else if (pipe.getDirection() == Direction.LEFT) {
                return "pipe_fork_1";
            }
        }
        // CROSS pipe
        else if (pipe.getPipeType() == PipeType.CROSS) {
            return "pipe_cross";
        }
        return null;
    }
    public void setPoints(String playerName, int newPoints) {
        int difficulty = 1;
        field.setSizeAndMovesLeft(1);
        int moves = field.getRemainingMoves();

        if (difficulty == 1) {
            if (moves < 5) points = 5;
            else if (moves < 10) points = 3;
            else if (moves <= 15) points = 1;
        } else if (difficulty == 2) {
            if (moves < 10) points = 8;
            else if (moves < 15) points = 5;
            else if (moves <= 20) points = 3;
        } else if (difficulty == 3) {
            if (moves < 15) points = 15;
            else if (moves < 20) points = 10;
            else if (moves <= 30) points = 5;
        } else if (difficulty == 4) {
            if (moves < 25) points = 20;
            else if (moves < 35) points = 15;
            else if (moves <= 40) points = 10;
        }
        Score score = new Score(playerName, "pipes", points + newPoints, new Date());
        scoreService.addScore(score);
    }
}

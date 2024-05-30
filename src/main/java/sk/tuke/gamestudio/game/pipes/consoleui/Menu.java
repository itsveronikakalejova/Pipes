package sk.tuke.gamestudio.game.pipes.consoleui;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.pipes.core.Field;
import sk.tuke.gamestudio.service.*;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Getter
public class Menu {

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    private String playerName;

    @Autowired
    public Menu(ScoreService scoreService, CommentService commentService, RatingService ratingService) {
        this.scoreService = scoreService;
        this.commentService = commentService;
        this.ratingService = ratingService;
    }

    public void printMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        playerName = scanner.next();

        String resetColor = "\u001B[0m";
        String purpleColor = "\u001b[38;5;147m";


        var field = new Field(1);
        var ui = new ConsoleUI(field, scoreService, this);

        logo(purpleColor, resetColor);

        int choice;

        do {
            menu();

            System.out.print("Your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    difficulty();

                    System.out.print("Your choice: ");
                    int difficulty = scanner.nextInt();

                    field = new Field(difficulty);
                    ui = new ConsoleUI(field, scoreService, this);

                    while (difficulty < 1 || difficulty > 4) {
                        System.out.print("Enter your choice again: ");
                        difficulty = scanner.nextInt();
                    }

                    ui.play();
                    break;
                case 2:
                    showMyScore(playerName);
                    break;
                case 3:
                    showHallOfFame();
                    break;
                case 4:
                    showComments();
                    System.out.print("Write comment: ");
                    scanner.nextLine();
                    String commentText = scanner.nextLine();
                    commentService.addComment(new Comment("pipes", playerName, commentText, new Date()));
                    break;
                case 5:
                    System.out.print("Rate our game: ");
                    int rating = scanner.nextInt();
                    while (rating < 1 || rating > 5) {
                        System.out.print("Invalid number. Rate again: ");
                        rating = scanner.nextInt();
                    }
                    ratingService.setRating(new Rating(playerName, "pipes", rating, new Date()));
                    showRating();
                    break;
            }
            if (choice == 6) break;
            System.out.println(purpleColor + "====================" + resetColor);
        } while (true);
    }


    private static void difficulty() {
        String greenColor = "\u001B[32m";
        String yellowColor = "\u001B[33m";
        String resetColor = "\u001B[0m";
        String redColor = "\u001B[31m";
        String orangeColor = "\u001B[38;5;208m";
        System.out.println("Choose the level difficulty!");
        System.out.println(greenColor + "(1) Easy" + resetColor);
        System.out.println(yellowColor + "(2) Intermediate" + resetColor);
        System.out.println(orangeColor + "(3) Difficult" + resetColor);
        System.out.println(redColor + "(4) Hardcore" + resetColor);
    }

    private static void menu() {
        String resetColor = "\u001B[0m";
        String orangeColor = "\u001B[38;5;208m";
        System.out.println(orangeColor);
        System.out.println("|1| Play game");
        System.out.println("|2| Show my score");
        System.out.println("|3| Show HOF");
        System.out.println("|4| Show comments");
        System.out.println("|5| Show rating");
        System.out.println("|6| Exit game");
        System.out.println(resetColor);
    }

    public static void logo(String purpleColor, String resetColor) {
        System.out.println(purpleColor);
        System.out.println("  ____  _                 ");
        System.out.println(" |  _ \\(_)_ __   ___  ___ ");
        System.out.println(" | |_) | | '_ \\ / _ \\/ __|");
        System.out.println(" |  __/| | |_) |  __/\\__ \\");
        System.out.println(" |_|   |_| .__/ \\___||___/");
        System.out.println("         |_|              ");
        System.out.println("===========================");
        System.out.println(resetColor);

    }

    public void showMyScore(String playerName) {
        Score playerScore = scoreService.getScore("pipes", playerName);
        System.out.println(playerScore.toString());
    }

    public void showHallOfFame() {
        List<Score> list = scoreService.getTopScores("pipes");
        for (Score score : list) {
            System.out.println(score.toString());
        }
    }

    public void showComments() {
        List<Comment> comments = commentService.getComments("pipes");

        System.out.println();
        System.out.println("Comments for the game pipes: ");
        for (Comment comment : comments) {
            System.out.println("Player: " + comment.getPlayer());
            System.out.println("Comment: " + comment.getComment());
            System.out.println("Commented On: " + comment.getCommentedOn());
            System.out.println();
        }
    }

    public void showRating() {
        System.out.println("Average rating of our game: " + ratingService.getAverageRating("pipes"));
    }
}

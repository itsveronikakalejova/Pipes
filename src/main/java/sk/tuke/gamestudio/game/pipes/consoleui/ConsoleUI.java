package sk.tuke.gamestudio.game.pipes.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.*;
import sk.tuke.gamestudio.game.pipes.core.*;
import sk.tuke.gamestudio.service.*;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private static final Pattern INPUT_PATTERN = Pattern.compile("([A-I])([1-9])");
    private Field _field;
    private Pipe[][] _array;
    private Score _score;
    private int _points;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    private Menu menu;

    @Autowired
    public ConsoleUI(Field field, ScoreService scoreService, Menu menu) {
        this.menu = menu;
        _field = field;
        this.scoreService = scoreService;
    }

    public void play() {
        _array = _field.getArray();
        displayBoard();
        do {
            processInput();
            displayBoard();
        } while (_field.getGameState() == GameState.PLAYING);

        setPoints();
        displayScore();
    }

    public int getPoints() {
        return _points;
    }



    private void setPoints() {
        int difficulty = _field.getDifficulty();
        _field.setSizeAndMovesLeft(difficulty);
        int moves = _field.getRemainingMoves();
        int size = _field.getSize();

        if (difficulty == 1) {
            if (moves < 5) _points = 1;
            else if (moves < 10) _points = 2;
            else if (moves <= 15) _points = 5;
        }
        if (difficulty == 2) {
            if (moves < 10) _points = 1;
            else if (moves < 15) _points = 2;
            else if (moves <= 20) _points = 5;
        }
        if (difficulty == 3) {
            if (moves < 15) _points = 1;
            else if (moves < 20) _points = 2;
            else if (moves <= 30) _points = 5;
        }
        if (difficulty == 4) {
            if (moves < 25) _points = 1;
            else if (moves < 35) _points = 2;
            else if (moves <= 40) _points = 5;
        }
        _score = new Score(menu.getPlayerName(), "pipes", _points, new Date());
        scoreService.addScore(_score);
    }

    private void displayScore() {
        System.out.println("Score:   " +
                "game = pipes" +
                " | player = " + menu.getPlayerName() +
                " | points = " + getPoints() +
                " | playedOn = " + new Date());
    }

    private void displayBoard() {
        printMovesLeft();
        printHeader();
        printPipes();
    }

    private void printMovesLeft() {
        System.out.println("Moves left: " + _field.getRemainingMoves());
    }

    private void printHeader() {
        // header
        System.out.print(" ");
        for (int i = 1; i <= _field.getSize(); i++) {
            System.out.print(" " + i);
        }
        System.out.println();
    }

    private void printPipes() {
        for (int i = 0; i < _field.getSize(); i++) {
            char rowLabel = (char) ('A' + i);

            System.out.print(rowLabel + " ");
            for (int j = 0; j < _field.getSize(); j++) {
                Pipe pipe = _array[i][j];
                printPipe(pipe);
            }
            System.out.println();
        }
    }

    private void printPipe(Pipe pipe) {
        String VERTICAL = "\u2502"; // │
        String HORIZONTAL = "\u2500"; // ─
        String TOP_LEFT_CORNER = "\u250C"; // ┌
        String TOP_RIGHT_CORNER = "\u2510"; // ┐
        String BOTTOM_LEFT_CORNER = "\u2514"; // └
        String BOTTOM_RIGHT_CORNER = "\u2518"; // ┘
        String LEFT_FORK = "\u251C"; // ├
        String RIGHT_FORK = "\u2524"; // ┤
        String TOP_FORK = "\u252C"; // ┬
        String BOTTOM_FORK = "\u2534"; // ┴
        String CROSS = "\u253C"; // +
        // CORNER pipe
        if (pipe.getPipeType() == PipeType.CORNER) {
            if (pipe.getDirection() == Direction.TOP) {
                System.out.print(TOP_LEFT_CORNER);
            } else if (pipe.getDirection() == Direction.RIGHT) {
                System.out.print(TOP_RIGHT_CORNER);
            } else if (pipe.getDirection() == Direction.BOTTOM) {
                System.out.print(BOTTOM_RIGHT_CORNER);
            } else if (pipe.getDirection() == Direction.LEFT) {
                System.out.print(BOTTOM_LEFT_CORNER);
            }
        }
        // STRAIGHT pipe
        else if (pipe.getPipeType() == PipeType.STRAIGHT) {
            if (pipe.getDirection() == Direction.TOP) {
                System.out.print(VERTICAL);
            } else if (pipe.getDirection() == Direction.RIGHT) {
                System.out.print(HORIZONTAL);
            } else if (pipe.getDirection() == Direction.BOTTOM) {
                System.out.print(VERTICAL);
            } else if (pipe.getDirection() == Direction.LEFT) {
                System.out.print(HORIZONTAL);
            }
        }
        // FORK pipe
        else if (pipe.getPipeType() == PipeType.FORK) {
            if (pipe.getDirection() == Direction.TOP) {
                System.out.print(TOP_FORK);
            } else if (pipe.getDirection() == Direction.RIGHT) {
                System.out.print(RIGHT_FORK);
            } else if (pipe.getDirection() == Direction.BOTTOM) {
                System.out.print(BOTTOM_FORK);
            } else if (pipe.getDirection() == Direction.LEFT) {
                System.out.print(LEFT_FORK);
            }
        }
        // CROSS pipe
        else if (pipe.getPipeType() == PipeType.CROSS) {
            if (pipe instanceof Start) System.out.print("\u001B[32m" + CROSS + "\u001B[0m");
            else if (pipe instanceof End) System.out.print("\u001B[31m" + CROSS + "\u001B[0m");
            else System.out.print(CROSS);
        }
        System.out.print(" ");
    }

    private boolean pipeExists(int row, int column) {
        return row >= 0 && row < _field.getSize() && column >= 0 && column < _field.getSize();
    }

    protected void processInput() {
        System.out.print("Select pipe to turn: ");
        var command = scanner.nextLine().trim().toUpperCase();
        if ("Q".equals(command))
            System.exit(0);
        var matcher = INPUT_PATTERN.matcher(command);
        if (matcher.matches()) {
            var row = matcher.group(1).charAt(0) - 'A';
            var column = Integer.parseInt(matcher.group(2)) - 1;
            if (row >= 0 && row < _field.getSize()
                    && column >= 0 && column < _field.getSize()) {
                _field.turnPipe(_field.getArray()[row][column]);
                printFlowDirections(_field.getArray(), row, column);

            }
        } else {
            System.err.println("Bad input!");
        }
    }

    private void printFlowDirections(Pipe[][] array, int row, int column) {
        char rowChar = (char) ('A' + row);

        System.out.println();
        System.out.println("Flow directions for: " + rowChar + (column + 1));

        // ANSI color codes
        final String GREEN = "\u001B[32m";
        final String RED = "\u001B[31m";
        final String RESET = "\u001B[0m";

        // Print flow directions
        System.out.println("TOP: " + (array[row][column].getPipeType().hasWaterTop() ? GREEN + "true" + RESET : RED + "false" + RESET) +
                " | RIGHT: " + (array[row][column].getPipeType().hasWaterRight() ? GREEN + "true" + RESET : RED + "false" + RESET) +
                " | BOTTOM: " + (array[row][column].getPipeType().hasWaterBottom() ? GREEN + "true" + RESET : RED + "false" + RESET) +
                " | LEFT: " + (array[row][column].getPipeType().hasWaterLeft() ? GREEN + "true" + RESET : RED + "false" + RESET));
        System.out.println();
    }
}

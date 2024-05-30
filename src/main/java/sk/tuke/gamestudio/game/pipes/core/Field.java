package sk.tuke.gamestudio.game.pipes.core;

import javax.swing.plaf.IconUIResource;
import java.util.Random;

public class Field {
    private int _movesLeft;
    private int _size;
    private int _remainingMoves;
    private GameState _gameState = GameState.PLAYING;
    private Pipe[][] _array;
    private int _difficulty;

    public Field(int difficulty) {
        _movesLeft = 0;
        _difficulty = difficulty;
        setSizeAndMovesLeft(_difficulty);
        generateField(_size, _movesLeft);
        while (getGameState() == GameState.SOLVED) {
            generateField(_size, _movesLeft);
        }
    }

    public int getDifficulty() {
        return _difficulty;
    }

    public void setSize(int size) {
        _size = size;
    }

    public void setMoves(int moves) {
        _remainingMoves = moves;
        _movesLeft = moves;
    }

    public void setSizeAndMovesLeft(int difficulty) {
        switch (difficulty) {
            case 1:
                setSize(4);
                setMoves(15);
                break;
            case 2:
                setSize(5);
                setMoves(20);
                break;
            case 3:
                setSize(7);
                setMoves(30);
                break;
            case 4:
                setSize(9);
                setMoves(45);
                break;
        }
    }
    public Pipe[][] getArray() {
        return _array;
    }
    public int getSize() {
        return _size;
    }
    public boolean isOutOfMoves() {
        if (_remainingMoves == 0) {
            return true;
        }
        return false;
    }
    public GameState getGameState() {
        resetVisitedStatus();
        boolean solved = checkPath(getArray()[0][0]);
        if (!solved && !isOutOfMoves()) {
            System.out.println("KEEP TRYING!");
            _gameState = GameState.PLAYING;
        } else if (solved) {
            System.out.println("YOU WON!");
            _gameState = GameState.SOLVED;
        } else if (isOutOfMoves()) {
            System.out.println("OUT OF MOVES!");
            _gameState = GameState.OUT_OF_MOVES;
        }
        return _gameState;
    }

    public void turnPipe(Pipe pipe) {
        if (_remainingMoves > 0) _remainingMoves--;
        if (pipe.getDirection() == Direction.TOP) turnPipeOnce(pipe);
        else if (pipe.getDirection() == Direction.RIGHT) turnPipeTwice(pipe);
        else if (pipe.getDirection() == Direction.BOTTOM) turnPipeThrice(pipe);
        else if (pipe.getDirection() == Direction.LEFT) turnPipeQuadrice(pipe);
        resetVisitedStatus();
    }

    public int getRemainingMoves()
    {
        return _remainingMoves;
    }

    // private functions
    private boolean checkPath(Pipe currentPipe) {
        if (!pipeExists(currentPipe)) {
            return false;
        }
        if (currentPipe instanceof End) {
            return true;
        }
        currentPipe.setVisited(true);

        int currentX = currentPipe.getDirX();
        int currentY = currentPipe.getDirY();

        boolean returnValue = false;

        returnValue |= checkSide(currentX, currentY, Direction.RIGHT);
        returnValue |= checkSide(currentX, currentY, Direction.BOTTOM);
        returnValue |= checkSide(currentX, currentY, Direction.TOP);
        returnValue |= checkSide(currentX, currentY, Direction.LEFT);

        return returnValue;
    }

    private boolean checkSide(int currentX, int currentY, Direction direction) {
        int nextX = currentX;
        int nextY = currentY;

        switch (direction) {
            case RIGHT:
                nextY += 1;
                break;
            case BOTTOM:
                nextX += 1;
                break;
            case TOP:
                nextX -= 1;
                break;
            case LEFT:
                nextY -= 1;
                break;
        }

        if (nextX >= 0 && nextX < getSize() && nextY >= 0 && nextY < getSize()) {
            Pipe currentPipe = getArray()[currentX][currentY];
            Pipe nextPipe = getArray()[nextX][nextY];

            if (currentPipe.allowsFlowFrom(direction) && nextPipe.allowsFlowFrom(direction.opposite()) && !nextPipe.isVisited()) {
                return checkPath(nextPipe);
            }
        }

        return false;
    }
    private boolean pipeExists(Pipe currentPipe) {
        int i = currentPipe.getDirX();
        int j = currentPipe.getDirY();
        return i >= 0 && i < getSize() && j >= 0 && j < getSize();
    }

    private void generateField(int size, int moves) {
        _array = new Pipe[size][size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                PipeType randomPipeType = getRandomPipeType(random);
                Direction direction = Direction.values()[random.nextInt(Direction.values().length)];
                Pipe pipe = new Pipe(randomPipeType, direction, i, j);
                _array[i][j] = pipe;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Pipe pipe = _array[i][j];
                PipeType pipeType = pipe.getPipeType();
                Direction direction = pipe.getDirection();

                if (direction == Direction.RIGHT) {
                    pipe.rotateBooleansRight();
                } else if (direction == Direction.BOTTOM) {
                    pipe.rotateBooleansRight();
                    pipe.rotateBooleansRight();
                } else if (direction == Direction.LEFT) {
                    pipe.rotateBooleansRight();
                    pipe.rotateBooleansRight();
                    pipe.rotateBooleansRight();
                }
            }
        }

        _array[0][0] = new Start(PipeType.CROSS, Direction.TOP, 0, 0);
        _array[size - 1][size - 1] = new End(PipeType.CROSS, Direction.TOP, size - 1, size - 1);
    }

    private PipeType getRandomPipeType(Random random) {
        // random number between 0 and 99
        int randomNumber = random.nextInt(100);

        if (randomNumber < 30) {
            return PipeType.STRAIGHT;
        } else if (randomNumber < 60) {
            return PipeType.CORNER;
        } else if (randomNumber < 90) {
            return PipeType.FORK;
        } else {
            return PipeType.CROSS;
        }
    }

    private void turnPipeOnce(Pipe pipe) {
        pipe.setDirection(Direction.RIGHT);
        pipe.resetBooleans();
        pipe.rotateBooleansRight();
    }

    private void turnPipeTwice(Pipe pipe) {
        if (pipe.getPipeType() == PipeType.STRAIGHT) pipe.setDirection(Direction.TOP);
        else pipe.setDirection(Direction.BOTTOM);
        pipe.resetBooleans();
        pipe.rotateBooleansRight();
        pipe.rotateBooleansRight();
    }

    private void turnPipeThrice(Pipe pipe) {
        if (pipe.getPipeType() == PipeType.STRAIGHT) pipe.setDirection(Direction.RIGHT);
        else pipe.setDirection(Direction.LEFT);
        pipe.resetBooleans();
        pipe.rotateBooleansRight();
        pipe.rotateBooleansRight();
        pipe.rotateBooleansRight();
    }
    private void turnPipeQuadrice(Pipe pipe) {
        pipe.setDirection(Direction.TOP);
        pipe.resetBooleans();
    }
    private void resetVisitedStatus() {
        for (int i = 0; i < _size; i++) {
            for (int j = 0; j < _size; j++) {
                if (_array[i][j] != null) {
                    _array[i][j].setVisited(false);
                }
            }
        }
    }
}
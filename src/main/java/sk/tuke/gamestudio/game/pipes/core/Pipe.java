package sk.tuke.gamestudio.game.pipes.core;

public class Pipe {
    private PipeType _pipeType;
    private Direction _direction;
    private int _dirX;
    private int _dirY;
    private boolean _visited;
    private boolean top;
    private boolean right;
    private boolean bottom;
    private boolean left;


    public Pipe(PipeType pipeType, Direction direction, int dirX, int dirY) {
        _pipeType = pipeType;
        _direction = direction;
        _dirX = dirX;
        _dirY = dirY;
        _visited = false;
        resetBooleans();
    }

    public void rotateBooleansRight() {
        boolean temp = top;
        top = left;
        left = bottom;
        bottom = right;
        right = temp;
    }

    public void resetBooleans() {
        top = _pipeType.hasWaterTop();
        right = _pipeType.hasWaterRight();
        bottom = _pipeType.hasWaterBottom();
        left = _pipeType.hasWaterLeft();
    }

    public PipeType getPipeType() {
        return _pipeType;
    }

    public Direction getDirection() {
        return _direction;
    }

    public void setDirection(Direction direction) {
        _direction = direction;
    }

    public boolean allowsFlowFrom(Direction direction) {
        switch (direction) {
            case TOP:
                return top;
            case RIGHT:
                return right;
            case BOTTOM:
                return bottom;
            case LEFT:
                return left;
            default:
                return false;
        }
    }

    public boolean isVisited() {
        return _visited;
    }

    public void setVisited(boolean visited) {
        _visited = visited;
    }

    public int getDirX() {
        return _dirX;
    }

    public int getDirY() {
        return _dirY;
    }
}

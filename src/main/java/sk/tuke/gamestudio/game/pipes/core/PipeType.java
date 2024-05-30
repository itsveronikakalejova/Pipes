package sk.tuke.gamestudio.game.pipes.core;

public enum PipeType {
    STRAIGHT(true, false, true, false),
    CORNER(false, true, true, false),
    FORK(false, true, true, true),
    CROSS(true, true, true, true);

    private boolean top;
    private boolean right;
    private boolean bottom;
    private boolean left;

    PipeType(boolean top, boolean right, boolean bottom, boolean left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public boolean hasWaterTop() {
        return top;
    }

    public boolean hasWaterRight() {
        return right;
    }

    public boolean hasWaterBottom() {
        return bottom;
    }

    public boolean hasWaterLeft() {
        return left;
    }
}

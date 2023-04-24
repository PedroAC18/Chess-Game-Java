package Boardgame;

public class Piece {
    protected Position position;//initial value == null
    private Board board;

    public Piece(Board board) {
        this.board = board;
        position = null;
    }

    //
    protected Board getBoard() {
        return board;
    }
}

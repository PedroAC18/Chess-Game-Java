package ChessPieces;

import Boardgame.Board;
import Chess.ChessPiece;
import Chess.Color;

public class Rook extends ChessPiece {

    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override//shows on board 'R' representing the rook
    public String toString() {
        return "R";
    }
}

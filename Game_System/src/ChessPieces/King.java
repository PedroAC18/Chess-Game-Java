package ChessPieces;

import Boardgame.Board;
import Chess.ChessPiece;
import Chess.Color;

public class King extends ChessPiece {

    public King(Board board, Color color) {
        super(board, color);
    }

    @Override//show on board 'K' representing King
    public String toString() {
        return "K";
    }
}

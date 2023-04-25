package Chess;

import Boardgame.Board;
import Boardgame.Position;
import ChessPieces.Rook;

//class wtih game rules
public class ChessMatch {
    private Board board;

    public ChessMatch(){
        board = new Board(8,8);//setting the board length
    }
    //return a piece matrix
    public ChessPiece[][] getPieces(){
        //ChessPiece can't interact with Boardgame.piece, so we downcast Boardgame.board to ChessPiece
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for(int i=0; i<board.getRows(); i++){
            for(int j=0; j<board.getColumns(); j++){
                mat[i][j] = (ChessPiece) board.piece(i,j);//downcasting exactly here
            }
        }
        return mat; //return of matrix with the pieces of the game
    }
    //responsable for placing the pieces on the board
    private void initalSetup(){
        board.placePiece(new Rook(board, Color.WHITE), new Position(2,1));

    }
}

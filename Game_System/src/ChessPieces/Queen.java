package ChessPieces;

import Boardgame.Board;
import Boardgame.Position;
import Chess.ChessPiece;
import Chess.Color;

public class Queen extends ChessPiece {

    public Queen (Board board, Color color) {
        super(board, color);
    }

    @Override//shows on board 'R' representing the rook
    public String toString() {
        return "Q";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0,0);//auxiliar position

        //above
        p.setValues(position.getRow()-1, position.getColumn());
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            mat[p.getRow()][p.getColumn()] = true;//indicates that the piece can move to that square
            p.setRow(p.getRow() - 1);
        }
        if(getBoard().positionExists(p) && isThereOpponentPiece(p)){//checks if there is a opponent piece on the rook way
            mat[p.getRow()][p.getColumn()] = true;
        }

        //left
        p.setValues(position.getRow(), position.getColumn()-1);
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            mat[p.getRow()][p.getColumn()] = true;//indicates that the piece can move to that square
            p.setColumn(p.getColumn()-1);
        }
        if(getBoard().positionExists(p) && isThereOpponentPiece(p)){//checks if there is a opponent piece on the rook way
            mat[p.getRow()][p.getColumn()] = true;
        }

        //right
        p.setValues(position.getRow(), position.getColumn()+1);
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            mat[p.getRow()][p.getColumn()] = true;//indicates that the piece can move to that square
            p.setColumn(p.getColumn()+1);
        }
        if(getBoard().positionExists(p) && isThereOpponentPiece(p)){//checks if there is a opponent piece on the rook way
            mat[p.getRow()][p.getColumn()] = true;
        }

        //below
        p.setValues(position.getRow()+1, position.getColumn());
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            mat[p.getRow()][p.getColumn()] = true;//indicates that the piece can move to that square
            p.setRow(p.getRow() + 1);
        }
        if(getBoard().positionExists(p) && isThereOpponentPiece(p)){//checks if there is a opponent piece on the rook way
            mat[p.getRow()][p.getColumn()] = true;
        }

        //nw
        p.setValues(position.getRow()-1, position.getColumn()-1);
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            mat[p.getRow()][p.getColumn()] = true;//indicates that the piece can move to that square
            p.setValues(p.getRow()-1, p.getColumn()-1);
        }
        if(getBoard().positionExists(p) && isThereOpponentPiece(p)){//checks if there is a opponent piece on the rook way
            mat[p.getRow()][p.getColumn()] = true;
        }

        //ne
        p.setValues(position.getRow()-1, position.getColumn()+1);
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            mat[p.getRow()][p.getColumn()] = true;//indicates that the piece can move to that square
            p.setValues(p.getRow()-1, p.getColumn()+1);
        }
        if(getBoard().positionExists(p) && isThereOpponentPiece(p)){//checks if there is a opponent piece on the rook way
            mat[p.getRow()][p.getColumn()] = true;
        }

        //se
        p.setValues(position.getRow()+1, position.getColumn()+1);
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            mat[p.getRow()][p.getColumn()] = true;//indicates that the piece can move to that square
            p.setValues(p.getRow()+1, p.getColumn()+1);
        }
        if(getBoard().positionExists(p) && isThereOpponentPiece(p)){//checks if there is a opponent piece on the rook way
            mat[p.getRow()][p.getColumn()] = true;
        }

        //sw
        p.setValues(position.getRow()+1, position.getColumn()-1);
        while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            mat[p.getRow()][p.getColumn()] = true;//indicates that the piece can move to that square
            p.setValues(p.getRow()+1, p.getColumn()-1);
        }
        if(getBoard().positionExists(p) && isThereOpponentPiece(p)){//checks if there is a opponent piece on the rook way
            mat[p.getRow()][p.getColumn()] = true;
        }
        return mat;
    }
}

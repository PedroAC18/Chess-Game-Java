package Chess;

import Boardgame.Position;

public class ChessPosition {

    private char column;
    private int row;

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public ChessPosition(char column, int row) {
        if(column < 'a' || column > 'h' || row < 1 || row > 8){
            throw new ChessException("Invalid movement"); //defensive method
        }
        this.column = column;
        this.row = row;
    }

    //matrix_row = 8 - chess_row
    //matrix_column = chess_colum - 'a'
    protected Position toPosition(){
        return new Position(8-row, column - 'a');
    }

    protected static ChessPosition fromPosition(Position position){
        return new ChessPosition((char)('a'+position.getColumn()), 8-position.getRow());
    }

    @Override
    public String toString() {
        return "" + column + row; //print a1 or b2 or c4...
    }
}

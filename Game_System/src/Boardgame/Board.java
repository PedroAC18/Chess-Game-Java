package Boardgame;

public class Board {

    private int rows;
    private int columns;
    private Piece[][] pieces;//matrix of pieces

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];//number of pieces on the board
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public Piece piece(int row, int column){
        return pieces[row][column];
    }

    //Override using Position to return the piece
    public Piece piece(Position position){
        return pieces[position.getRow()][position.getColumn()];
    }
    //responsable for placing the piece on the board
    public void placePiece(Piece piece, Position position){
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
        
    }

}

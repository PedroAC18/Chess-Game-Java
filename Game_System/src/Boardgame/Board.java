package Boardgame;

public class Board {

    private int rows;
    private int columns;
    private Piece[][] pieces;//matrix of pieces

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];//number of pieces on the board
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int row, int column) {
        if (!positionExists(row, column)) {//if position doesen't exisits
            throw new BoardException("Position not on the board");
        }
        return pieces[row][column];
    }

    //Override using Position to return the piece
    public Piece piece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Position not on the board");
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    //responsable for placing the piece on the board
    public void placePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)) {
            throw new BoardException("There is already a piece on position " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    //responsable for removing piece on the board
    public Piece removePiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Position not on the board");
        }
        if (piece(position) == null) {
            return null;
        }
        Piece aux = piece(position);
        aux.position = null;
        pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }

    //auxiliar method that uses the rows and colums to validate, it's easier using this than position
    private boolean positionExists(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
        //validating if the positions provided by the user is between the board setup
    }

    //reponsible for telling the user if the position provided is valid
    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }



    //compare if the position provided is already taken on the board
    public boolean thereIsAPiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Position not on the board");

        }
        return piece(position) != null;
        //if equals null then it's not taken, true equals position is taken
    }
}

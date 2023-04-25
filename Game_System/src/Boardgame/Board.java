package Boardgame;

public class Board {

    private int rows;
    private int columns;
    private Piece[][] pieces;//matrix of pieces

    public Board(int rows, int columns) {
        if(rows < 1 || columns < 1){
            throw new BoardException("Error: there must be at least 1 row and 1 column");
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

    public Piece piece(int row, int column){
        if(!positionExists(row, column)){//if position doesen't exisits
           throw new BoardException("Position not on the board");
        }
        return pieces[row][column];
    }

    //Override using Position to return the piece
    public Piece piece(Position position){
        return pieces[position.getRow()][position.getColumn()];
    }
    //responsable for placing the piece on the board
    public void placePiece(Piece piece, Position position){
        if(thereIsAPiece(position)){
            throw new BoardException("There is already a piece on position " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    //auxiliar method that uses the rows and colums to validate, it's easier using this than position
    private boolean positionExists(int row, int colum){
        return row >= 0 && row < rows && colum >=0 && columns < columns;
        //validating if the positions provided by the user is between the board setup

    }

     //reponsible for telling the user if the position provided is valid
     public boolean positionExists(Position position){
        return positionExists(position.getRow(), position.getColumn());

     }

     //compare if the position provided is already taken on the board
     public boolean thereIsAPiece(Position position){
        if(!positionExists(position)){//if position doesen't exisits
           throw new BoardException("Position not on the board");      
        }                                                              
        return piece(position) != null;
        //if equals null then it's not taken, true equals position is taken
     }
}

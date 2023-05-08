package Boardgame;

public abstract class Piece {
    protected Position position;//initial value == null
    private Board board;

    public Piece(Board board) {
        this.board = board;
        position = null;
    }

    protected Board getBoard() {
        return board;
    }

    //return true or false if it's possible to move to certain direction
    public abstract boolean[][] possibleMoves();

    //hook method, use the abstract method to know if the movement is possible
    public boolean possibleMove(Position position){
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    //call abstract method to see if is there a possible movement
    public boolean isThereAnyPossibleMove(){
        boolean[][] mat = possibleMoves();
        for (int i=0; i< mat.length; i++){
            for(int j=0; j< mat.length; j++){
                if(mat[i][j] == true){
                    return true; //return true if are some possible move
                }
            }
        }
        return false;
    }
}

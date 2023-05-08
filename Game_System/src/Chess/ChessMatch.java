package Chess;

import Boardgame.Board;
import Boardgame.Position;
import Boardgame.Piece;
import ChessPieces.King;
import ChessPieces.Rook;

//class with game rules
public class ChessMatch {
    private Board board;
    private int turn;
    private Color currentPlayer;

    public ChessMatch(){
        board = new Board(8,8);//setting the board length
        turn = 1;
        currentPlayer = Color.WHITE;
        inititalSetup();
    }

    public int getTurn(){
        return turn;
    }

    public Color getCurrentPlayer(){
        return currentPlayer;
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

    //print a matrix of possibles moves
    public boolean[][] possibleMoves(ChessPosition sourcePosition){
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    //reads the movement that the user want to make
    public ChessPiece performeChesMove(ChessPosition source, ChessPosition target){
        Position sourcePos = source.toPosition();
        Position targetPos = target.toPosition();
        validateSourcePos(sourcePos);
        Piece capturedPiece = makeMove(sourcePos, targetPos);//making the users movement
        nextTurn();
        return (ChessPiece) capturedPiece;//it's necessary to downcast Piece to ChessPiece
    }

    private void validateSourcePos(Position position){
        if(!board.thereIsAPiece(position)){
            throw new ChessException("There is no piece on source position");
        }
        if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()){
            throw new ChessException("The chosen piece is not yours");
        }
        if(!board.piece(position).isThereAnyPossibleMove()){
            throw new ChessException("Thre is no possible moves");
        }
    }

    private void validateTargetPosition(Position source, Position target){
        if(!board.piece(source).possibleMove(target)){
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private Piece makeMove(Position source, Position target){
        Piece p = board.removePiece(source);//removing piece from source
        Piece capturedPiece = board.removePiece(target); //if there is a piece on target, this method is going to capture it
        board.placePiece(p, target);//places the piece on the target
        return capturedPiece;
    }

    private void validateSourcePosition(Position position){
        if(!board.thereIsAPiece(position)){
            throw new ChessException("There is no piece on source position");
        }
        if(!board.piece(position).isThereAnyPossibleMove()){
            throw new ChessException("There is no moves for the chosen piece");
        }
    }

    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE);
    }

    //placing pieces according to board positions (b1, a2)
    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void inititalSetup(){
        placeNewPiece('b', 6, new Rook(board, Color.WHITE));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
    }
}

package Chess;

import Boardgame.Board;
import Boardgame.Position;
import Boardgame.Piece;
import ChessPieces.King;
import ChessPieces.Rook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//class with game rules
public class ChessMatch {
    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;

    private List<Piece> piecesOnBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();
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

    public boolean getCheck(){
        return check;
    }

    public boolean getCheckMate(){
        return checkMate;
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
        //player can't put himself in check state
        if(testCheck(currentPlayer)){
            undoMove(sourcePos, targetPos, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        //check if the opponent is in check
        check = (testCheck(opponent(currentPlayer))) ? true : false;

        //if the move generate a check mate, the game needs to end
        if(testCheckMate(opponent(currentPlayer))){
            checkMate = true;
        }else {
            nextTurn();
        }
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
        ChessPiece p = (ChessPiece)board.removePiece(source);//removing piece from source
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target); //if there is a piece on target, this method is going to capture it
        board.placePiece(p, target);//places the piece on the target

        if(capturedPiece != null){
            piecesOnBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        //special move castling kingside move
        if(p instanceof King && target.getColumn() == source.getColumn() +2){
            Position sourceT = new Position(source.getRow(), source.getColumn()+3);
            Position targetT = new Position(source.getRow(), source.getColumn()+1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT); //move manually the rook
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        //special move castling queenside move
        if(p instanceof King && target.getColumn() == source.getColumn() -2){
            Position sourceT = new Position(source.getRow(), source.getColumn()-4);
            Position targetT = new Position(source.getRow(), source.getColumn()-1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT); //move manually the rook
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        return capturedPiece;
    }

    //method to undo the makeMove
    private void undoMove(Position source, Position target, Piece capturedPiece){
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if(capturedPiece != null){//it means that a piece was captured and it needs to get replaced on the board
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnBoard.add(capturedPiece);
        }

        //special move castling kingside move
        if(p instanceof King && target.getColumn() == source.getColumn() +2){
            Position sourceT = new Position(source.getRow(), source.getColumn()+3);
            Position targetT = new Position(source.getRow(), source.getColumn()+1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT); //move manually the rook
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        //special move castling queenside move
        if(p instanceof King && target.getColumn() == source.getColumn() -2){
            Position sourceT = new Position(source.getRow(), source.getColumn()-4);
            Position targetT = new Position(source.getRow(), source.getColumn()-1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT); //move manually the rook
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

    }

    private void validateSourcePosition(Position position){
        if(!board.thereIsAPiece(position)){
            throw new ChessException("There is no piece on source position");
        }
        if(!board.piece(position).isThereAnyPossibleMove()){
            throw new ChessException("There is no moves for the chosen piece");
        }
    }

    //change players turns
    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE);
    }

    //change player color to play
    private Color opponent(Color color){
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    //list to filter what is the king's color
    private ChessPiece king(Color color){
        List<Piece> list = piecesOnBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for(Piece p : list){
            if( p instanceof King){
                return (ChessPiece) p;

            }
        }
        //exception throws if there is no king on board (should never been used)
        throw new IllegalStateException("There is no" + color + "king on the board");
    }

    //method to see if any piece on board is in a check state
    private boolean testCheck(Color color){
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
        for(Piece p : opponentPieces){
            boolean[][] mat = p.possibleMoves();
            if(mat[kingPosition.getRow()][kingPosition.getColumn()]){//check if any piece in check with the opponent king
                return true;
            }
        }
        return false;//there is no check state
    }

    //method to see if the game is in a check mate situation
    private boolean testCheckMate(Color color){
        if(!testCheck(color)){
            return false;//game is not in a check mate situation
        }
        //gets all pieces of the color
        List<Piece> list = piecesOnBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
        for(Piece p: list){
            boolean[][] mat = p.possibleMoves();
            for(int i=0; i<board.getRows(); i++){
                for(int j=0; j< board.getColumns(); j++){
                    if(mat[i][j]){
                        //simulate the piece move to see if it's gonna be a check mate
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i,j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if(!testCheck){
                            return false;//the piece avoided the check mate
                        }
                    }
                }
            }
        }
        return true;
    }

    //placing pieces according to board positions (b1, a2)
    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnBoard.add(piece);
    }

    private void inititalSetup(){
        placeNewPiece('b', 6, new Rook(board, Color.WHITE));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE, this));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK, this));
    }
}

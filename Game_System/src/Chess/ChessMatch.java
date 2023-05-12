package Chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Boardgame.Board;
import Boardgame.Piece;
import Boardgame.Position;
import ChessPieces.Bishop;
import ChessPieces.King;
import ChessPieces.Knight;
import ChessPieces.Pawn;
import ChessPieces.Queen;
import ChessPieces.Rook;

//class with game rules
public class ChessMatch {
    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;

    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

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

    public ChessPiece getEnPassantVulnerable(){
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
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
    public ChessPiece performChessMove(ChessPosition source, ChessPosition target){
        Position sourcePos = source.toPosition();
        Position targetPos = target.toPosition();
        validateSourcePos(sourcePos);
        validateTargetPosition(sourcePos, targetPos);
        Piece capturedPiece = makeMove(sourcePos, targetPos);//making the users movement

        //player can't put himself in check state
        if(testCheck(currentPlayer)){
            undoMove(sourcePos, targetPos, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        ChessPiece movedPiece = (ChessPiece)board.piece(targetPos);

        // #specialmove promotion
        promoted = null;
        if (movedPiece instanceof Pawn) {
            if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
                promoted = (ChessPiece)board.piece(targetPos);
                promoted = replacePromotedPiece("Q");
            }
        }

        //check if the opponent is in check
        check = (testCheck(opponent(currentPlayer))) ? true : false;

        //if the move generate a check mate, the game needs to end
        if(testCheckMate(opponent(currentPlayer))){
            checkMate = true;
        }else {
            nextTurn();
        }

        //special move en passant
        if(movedPiece instanceof Pawn && (targetPos.getRow() == sourcePos.getRow() - 2 || targetPos.getRow() == sourcePos.getRow() + 2 )){
            enPassantVulnerable = movedPiece;
        }else{
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;//it's necessary to downcast Piece to ChessPiece
    }

    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted");
        }
        if (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        if (type.equals("B")) return new Bishop(board, color);
        if (type.equals("N")) return new Knight(board, color);
        if (type.equals("Q")) return new Queen(board, color);
        return new Rook(board, color);
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

        // #specialmove en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                }
                else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnBoard.remove(capturedPiece);
            }
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

        // #specialmove en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece)board.removePiece(target);
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getColumn());
                }
                else {
                    pawnPosition = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }

    }

    private void validateSourcePosition(Position position){
        if(!board.thereIsAPiece(position)){
            throw new ChessException("There is no piece on source position");
        }
        if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
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
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));

    }
}

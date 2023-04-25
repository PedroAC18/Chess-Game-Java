package Boardgame;
//class responsible of dealing with possibles exceptions during the game
public class BoardException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public BoardException(String msg){
        super(msg);
    }
}

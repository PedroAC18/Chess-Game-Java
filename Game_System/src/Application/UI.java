package Application;

import Chess.ChessPiece;

public class UI {

    public static void printBoard(ChessPiece[][] pieces){
        for(int i=0; i<pieces.length; i++){
            System.out.print((8-i) + " ");//printing the row's number
            for (int j=0; j<pieces.length; j++){
                printPiece(pieces[i][j]);//printing the pieces in the rows
            }
            System.out.println();
        }
        System.out.print("  a b c d e f g h");


    }
    //auxiliar method that just print one piece
    private static void printPiece(ChessPiece piece){
        if(piece == null){
            System.out.print("-");
        }else{
            System.out.print(piece);
        }
        System.out.print(" ");
    }
}

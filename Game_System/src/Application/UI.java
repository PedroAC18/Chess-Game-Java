package Application;

import Chess.ChessMatch;
import Chess.ChessPiece;
import Chess.ChessPosition;
import Chess.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {

    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

    //text colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    //background colors
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    //clear console after each movement
    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    //reading the position that the user digits
    public static ChessPosition readChessPosition(Scanner sc){
        try{
            String s = sc.nextLine();//user digits a1, b2, c3....
            char column = s.charAt(0);//gets just the column
            int row = Integer.parseInt(s.substring(1));//gets the number of the row
            return new ChessPosition(column, row);
        }
        catch (RuntimeException e){
            throw new InputMismatchException("Error reading ChessPosition");
        }
    }

    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured){
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPieces(captured);
        System.out.println();
        System.out.println("Turn: " + chessMatch.getTurn());
        System.out.println("Waiting player: " + chessMatch.getCurrentPlayer());
    }


    public static void printBoard(ChessPiece[][] pieces){
        for(int i=0; i<pieces.length; i++){
            System.out.print((8-i) + " ");//printing the row's number
            for (int j=0; j<pieces.length; j++){
                printPiece(pieces[i][j], false);//printing the pieces in the rows
            }
            System.out.println();
        }
        System.out.print("  a b c d e f g h");
    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves){
        for(int i=0; i<pieces.length; i++){
            System.out.print((8-i) + " ");//printing the row's number
            for (int j=0; j<pieces.length; j++){
                printPiece(pieces[i][j], possibleMoves[i][j]);//printing the pieces in the rows
            }
            System.out.println();
        }
        System.out.print("  a b c d e f g h");
    }


    //auxiliar method that just print one piece
    private static void printPiece(ChessPiece piece, boolean background) {
        if(background){
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if (piece == null) {
            System.out.print("-" + ANSI_RESET);
        }
        else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_WHITE + piece + ANSI_RESET);
            }
            else {
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    private static void printCapturedPieces(List<ChessPiece> captured){
        //filter for white captured pieces
        List<ChessPiece> white = captured.stream().filter( x -> x.getColor() == Color.WHITE).collect(Collectors.toList());
        //filter for black captured pieces
        List<ChessPiece> black = captured.stream().filter( x -> x.getColor() == Color.BLACK).collect(Collectors.toList());

        System.out.println("Captured pieces: ");
        System.out.print("White: ");
        System.out.print(ANSI_WHITE);
        System.out.print(Arrays.toString(white.toArray()));
        System.out.print(ANSI_RESET);
        System.out.print("Black: ");
        System.out.print(ANSI_YELLOW);
        System.out.print(Arrays.toString(black.toArray()));
        System.out.print(ANSI_RESET);
    }
}

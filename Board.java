/*
    Generate a chess board in bitboard notation for a given position
 */

// random number : long time = System.currentTimeMillis();

import java.util.Arrays;

public class Board {

    /*
        Initialize a new game with starting board
     */
    public void initializeBoard() {
        // initialize empty bitboards for all 12 pieces
        long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L;
        /*
         initialize starting chess board.
         black is lower case, white is upper case
         r/R = rook, n/N = knight, b/B = bishop, p/P = pawn, q/Q = queen, k/K = king
         */
        String board [][] = {
            {"r", "n", "b", "q", "k", "b", "n", "r"},
            {"p", "p", "p", "p", "p", "p", "p", "p"},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {"P", "P", "P", "P", "P", "P", "P", "P"},
            {"R", "N", "B", "Q", "K", "B", "N", "R"}
        };
        generateBitboards(board, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
    }

    /*
        Generates bitboards for all pieces given a board in any state of a game

        @param
        board:                         two-dimension array with the string representation of the current board in play
        WP, WN, WB, WR, WQ, WK:        bitboards for all white pieces
        BP, BN, BB, BR, BQ, BK:        bitboards for all black pieces
          */
    public void generateBitboards(String[][] board, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK){
        StringBuilder binary;
        for (int i = 0; i < 64; i++) {
            binary = new StringBuilder("0000000000000000000000000000000000000000000000000000000000000000");
            binary.setCharAt(i, '1');
            switch (board[i/8][i%8]){
                case "P": WP += binaryToLong(binary); break;
                case "N": WN += binaryToLong(binary); break;
                case "B": WB += binaryToLong(binary); break;
                case "R": WR += binaryToLong(binary); break;
                case "Q": WQ += binaryToLong(binary); break;
                case "K": WK += binaryToLong(binary); break;
                case "p": BP += binaryToLong(binary); break;
                case "n": BN += binaryToLong(binary); break;
                case "b": BB += binaryToLong(binary); break;
                case "r": BR += binaryToLong(binary); break;
                case "q": BQ += binaryToLong(binary); break;
                case "k": BK += binaryToLong(binary); break;
            }
        }
        MovePiece m = new MovePiece();
        m.whitePossibleMoves("1636,", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
//        generateVisualBoard(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
    }

    /*
        Converts a binary string to long

        @param
        binary:         some binary string

        @return         the long value of the binary string
     */
    public long binaryToLong(StringBuilder binary) {
        if (binary.charAt(0) == '0') {//not going to be a negative number
            return Long.parseLong(binary.toString(), 2);
        } else {
            return Long.parseLong("1"+binary.substring(2), 2)*2;
        }
    }

    /*
        Prints out a command-line visual representation of a chess board given all 12 bitboards

        @param
        WP, WN, WB, WR, WQ, WK:        bitboards for all white pieces
        BP, BN, BB, BR, BQ, BK:        bitboards for all black pieces
     */
    public void generateVisualBoard(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK){
        String[][] board = new String[8][8];
        for (int i = 0; i < 64; i++) {
            board[i/8][i%8] = " ";
        }

        long highOrderBit = 1L << 63;

        for (int i = 0; i < 64; i++) {
            if (((WP<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="P";
            }
            if (((WN<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="N";
            }
            if (((WB<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="B";
            }
            if (((WR<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="R";
            }
            if (((WQ<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="Q";
            }
            if (((WK<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="K";
            }
            if (((BP<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="p";
            }
            if (((BN<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="n";
            }
            if (((BB<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="b";
            }
            if (((BR<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="r";
            }
            if (((BQ<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="q";
            }
            if (((BK<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="k";
            }
        }
        printBoard(board);
    }

    /*
        Prints a chess board in the correct format with white on bottom and a1 on the bottom legt

        @param
        board:      String representation of the current board in play
     */
    public void printBoard(String[][] board){
        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }
}

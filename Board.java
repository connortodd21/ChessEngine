/*
    Generate a chess board in bitboard notation for a given position
 */

// random number : long time = System.currentTimeMillis();

import java.util.Arrays;

public class Board {

    public void initializeBoard() {
        // initialize bitboards for all 12 pieces
        long WP = 0, WN = 0, WB = 0, WR = 0, WQ = 0, WK = 0, BP = 0, BN = 0, BB = 0, BR = 0, BQ = 0, BK = 0;
        /*
         initialize starting chess board.
         black is lower case, white is upper case
         r/R = rook, n/N = knight, b/B = bishop, p/P = pawn, q/Q = queen, k/K = king
         */
        String board [][] = {
            {"r", "n", "b", "q", "k", "b", "n", "r"},
            {"p", "p", "p", "p", "p", "p", "P", "p"},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", "p", " ", "p", " ", " ", " "},
            {"P", "P", "P", "P", "P", "P", "P", "P"},
            {"R", "N", "B", "Q", "K", "B", "N", "R"}
        };
        System.out.println(board[5][2]);
        System.out.println(board[6][1]);
        generateBitboards(board, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
    }

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
        m.whitePossibleMoves("", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        generateVisualBoard(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
    }

    private long binaryToLong(StringBuilder Binary) {
        if (Binary.charAt(0) == '0') {//not going to be a negative number
            return Long.parseLong(Binary.toString(), 2);
        } else {
            return Long.parseLong("1"+Binary.substring(2), 2)*2;
        }
    }

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

    public void printBoard(String[][] board){
        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }
}
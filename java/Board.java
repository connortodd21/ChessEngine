package java;/*
    Generate a chess board in bitboard notation for a given position
 */

// random number : long time = System.currentTimeMillis();

import java.util.Arrays;

public class Board {

    /*
        Initialize a new game with starting board
     */
    public static void initializeBoard() {
        // initialize empty bitboards for all 12 pieces
        long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L, EP=0L;
        boolean WQC = true, WKC = true, BQC = true, BKC = true;
        /*
         initialize starting chess board.
         black is lower case, white is upper case
         r/R = rook, n/N = knight, b/B = bishop, p/P = pawn, q/Q = queen, k/K = king
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
        generateBitboards(board, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, true, true, true, true);
    }

    /*
        Generates bitboards for all pieces given a board in any state of a game

        @param
        board:                         two-dimension array with the string representation of the current board in play
        WP, WN, WB, WR, WQ, WK:        bitboards for all white pieces
        BP, BN, BB, BR, BQ, BK:        bitboards for all black pieces
          */
    public static void generateBitboards(String[][] board, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean WQC, boolean WKC, boolean BQC, boolean BKC){
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
        Engine.WP=WP; Engine.WN=WN; Engine.WB=WB;
        Engine.WR=WR; Engine.WQ=WQ; Engine.WK=WK;
        Engine.BP=BP; Engine.BN=BN; Engine.BB=BB;
        Engine.BR=BR; Engine.BQ=BQ; Engine.BK=BK;
        Engine.WCK=true; Engine.WCQ=true;
        Engine.BCK=true; Engine.BCQ=true;
//        ChessUtilities.printBitboard(MovePiece.unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK));
//        ChessUtilities.printBitboard(WK);
//        String moves = MovePiece.whitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, WQC, WKC);
//        for (int i = 0; i < moves.length(); i+=5) {
//            System.out.println(Perft.moveToAlgebra(moves.substring(i, i+5)));
//        }
//        System.out.println(MovePiece.whitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, WQC, WKC));
//        MovePiece.whitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, WQC, WKC);
//        MovePiece.blackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, BQC, BKC);
//        ChessUtilities.printBitboard(m.unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK));
//        ChessUtilities.printBitboard(m.unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK));
//        Perft.perftRoot(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, WQC, WKC, BQC, BKC, true, 0);
//        System.out.println(Perft.perftMoveCounter);
//        long temp = MovePiece.makeMove(WP, "6050", 'P');
//        ChessUtilities.printBitboard(temp);
//        ChessUtilities.printEntireLongBinary(temp);

    }

    public static void importFEN(String fen){
        Engine.WP=0; Engine.WN=0; Engine.WB=0;
        Engine.WR=0; Engine.WQ=0; Engine.WK=0;
        Engine.BP=0; Engine.BN=0; Engine.BB=0;
        Engine.BR=0; Engine.BQ=0; Engine.BK=0;
        Engine.WCK=false; Engine.WCQ=false;
        Engine.BCK=false; Engine.BCQ=false;

        int index = 0;
        int board = 0;
        while (fen.charAt(index) != ' '){
            switch (fen.charAt(index++)){
                case 'P':
                    Engine.WP |= (1L << (63-board++));
                    break;
                case 'N':
                    Engine.WN |= (1L << (63 - board++));
                    break;
                case 'B':
                    Engine.WB |= (1L << (63 - board++));
                    break;
                case 'R':
                    Engine.WR |= (1L << (63 - board++));
                    break;
                case 'Q':
                    Engine.WQ |= (1L << (63 - board++));
                    break;
                case 'K':
                    Engine.WK |= (1L << (63 - board++));
                    break;
                case 'p':
                    Engine.BP |= (1L << (63 - board++));
                    break;
                case 'n':
                    Engine.BN |= (1L << (63 - board++));
                    break;
                case 'b':
                    Engine.BB |= (1L << (63 - board++));
                    break;
                case 'r':
                    Engine.BR |= (1L << (63 - board++));
                    break;
                case 'q':
                    Engine.BQ |= (1L << (63 - board++));
                    break;
                case 'k':
                    Engine.BK |= (1L << (63 - board++));
                    break;
                case '/':
                    break;
                case '1':
                    board++;
                    break;
                case '2':
                    board+=2;
                    break;
                case '3':
                    board+=3;
                    break;
                case '4':
                    board+=4;
                    break;
                case '5':
                    board+=5;
                    break;
                case '6':
                    board+=6;
                    break;
                case '7':
                    board+=7;
                    break;
                case '8':
                    board+=8;
                    break;
                default:  break;
            }
        }
        Engine.WhiteToMove = (fen.charAt(++index) == 'w');
        index += 2;
        while (fen.charAt(index) != ' '){
            switch (fen.charAt(index++)){
                case '-': break;
                case 'K': Engine.WCK = true;
                case 'Q': Engine.WCQ = true;
                case 'k': Engine.BCK = true;
                case 'q': Engine.BCQ = true;
                default: break;
            }
        }
        if (fen.charAt(++index) != '-'){
            Engine.EP = BitBoards.FILE_MASKS[fen.charAt(index++) - 'a'];
        }
        System.out.println(boardToFen(Engine.WP, Engine.WN, Engine.WB, Engine.WR, Engine.WQ, Engine.WK, Engine.BP, Engine.BN, Engine.BB, Engine.BR, Engine.BQ, Engine.BK, 0, true, true, true, true));
//        generateVisualBoard(Engine.WP, Engine.WN, Engine.WB, Engine.WR, Engine.WQ, Engine.WK, Engine.BP, Engine.BN, Engine.BB, Engine.BR, Engine.BQ, Engine.BK);
    }

    /*
        Converts a binary string to long

        @param
        binary:         some binary string

        @return         the long value of the binary string
     */
    public static long binaryToLong(StringBuilder binary) {
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
    public static String[][] generateVisualBoard(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK){
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
        return board;
    }

    public static void printEngineBoard(){
        String[][] board = new String[8][8];
        for (int i = 0; i < 64; i++) {
            board[i/8][i%8] = " ";
        }

        long highOrderBit = 1L << 63;

        for (int i = 0; i < 64; i++) {
            if (((Engine.WP<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="P";
            }
            if (((Engine.WN<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="N";
            }
            if (((Engine.WB<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="B";
            }
            if (((Engine.WR<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="R";
            }
            if (((Engine.WQ<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="Q";
            }
            if (((Engine.WK<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="K";
            }
            if (((Engine.BP<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="p";
            }
            if (((Engine.BN<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="n";
            }
            if (((Engine.BB<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="b";
            }
            if (((Engine.BR<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="r";
            }
            if (((Engine.BQ<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="q";
            }
            if (((Engine.BK<<i) & highOrderBit) == highOrderBit) {
                board[i/8][i%8]="k";
            }
        }
        printBoard(board);
    }

    public static String boardToFen(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean WQC, boolean WKC, boolean BQC, boolean BKC){
        String [][] board = generateVisualBoard(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        StringBuilder fen = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            int count = 0;
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].equals(" ")) {
                    count++;
                }
                else {
                    if (count != 0){
                        fen.append(count + "" + board[i][j]);
                        count = 0;
                    }
                    else {
                        fen.append(board[i][j]);
                    }
                }
            }
            if (count > 0){
                fen.append(count);
            }
            fen.append("/");
        }
        return fen.toString();
    }

    /*
        Prints a chess board in the correct format with white on bottom and a1 on the bottom legt

        @param
        board:      String representation of the current board in play
     */
    public static void printBoard(String[][] board){
        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }
}

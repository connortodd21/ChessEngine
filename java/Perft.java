package java;

import java.util.InputMismatchException;

public class Perft {

    public static String moveToAlgebra(String move) {
        String moveString="";
        moveString += "" + (char)(move.charAt(1) + 49);
        moveString += "" + ('8' - move.charAt(0));
        moveString += "" + (char)(move.charAt(3) + 49);
        moveString += "" + ('8' - move.charAt(2));
        return moveString;
    }

    static int perftTotalMoveCounter=0;
    static int perftMoveCounter=0;
    static int perftMaxDepth=0;
    static long startTime = 0;

    public static void perftRoot(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean WCQ, boolean WCK, boolean BCQ, boolean BCK, boolean WhiteToMove, int depth, int maxDepth) throws InputMismatchException {
        if (maxDepth <= 0){
            throw new InputMismatchException();
        }
        perftMaxDepth = maxDepth;
        startTime = System.currentTimeMillis();
        String moves;
        if (WhiteToMove){
            moves = MovePiece.whitePossibleMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,WCQ,WCK);
        }
        else {
            moves = MovePiece.blackPossibleMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,BCQ,BCK);
        }
        for (int i = 0; i < moves.length(); i+=5) {
            long WPt=MovePiece.makeMove(WP, moves.substring(i,i+4), 'P'), WNt=MovePiece.makeMove(WN, moves.substring(i,i+4), 'N'),
                    WBt=MovePiece.makeMove(WB, moves.substring(i,i+4), 'B'), WRt=MovePiece.makeMove(WR, moves.substring(i,i+4), 'R'),
                    WQt=MovePiece.makeMove(WQ, moves.substring(i,i+4), 'Q'), WKt=MovePiece.makeMove(WK, moves.substring(i,i+4), 'K'),
                    BPt=MovePiece.makeMove(BP, moves.substring(i,i+4), 'p'), BNt=MovePiece.makeMove(BN, moves.substring(i,i+4), 'n'),
                    BBt=MovePiece.makeMove(BB, moves.substring(i,i+4), 'b'), BRt=MovePiece.makeMove(BR, moves.substring(i,i+4), 'r'),
                    BQt=MovePiece.makeMove(BQ, moves.substring(i,i+4), 'q'), BKt=MovePiece.makeMove(BK, moves.substring(i,i+4), 'k'),
                    EPt=MovePiece.makeMoveEP(WP|BP,moves.substring(i,i+4));
            // same with castling booleans
            boolean WCQt = WCQ, WCKt = WCK, BCQt = BCQ, BCKt = BCK;
            if (Character.isDigit(moves.charAt(i+3))){
                // regular move
                int start=(Character.getNumericValue(moves.charAt(i)) * 8) + (Character.getNumericValue(moves.charAt(i+1)));
                if (((1L<<start) & WK)!=0) {
                    WCQt=false; WCKt=false;
                }
                if (((1L<<start) & BK)!=0) {
                    BCQt=false; BCKt=false;
                }
                if (((1L<<start) & WR & (1L<<63))!=0) {
                    WCKt=false;
                }
                if (((1L<<start) & WR & (1L<<56))!=0) {
                    WCQt=false;
                }
                if (((1L<<start) & BR & (1L<<7))!=0) {
                    BCKt=false;
                }
                if (((1L<<start) & BR & 1L)!=0) {
                    BCQt=false;
                }
                if (((WKt&MovePiece.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) || ((BKt&MovePiece.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
                    perft(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,WCQt,WCKt,BCQt,BCKt,!WhiteToMove,depth+1);
                    System.out.println(moveToAlgebra(moves.substring(i,i+4))+" "+( perftMoveCounter == 0 ? 1 : perftMoveCounter ));
                    perftTotalMoveCounter+=perftMoveCounter;
                    perftMoveCounter=0;
                }
            }
        }
        System.out.println("\ntotal: " + perftTotalMoveCounter);
        perftTotalMoveCounter = 0;
        System.out.println("time: " + ((float) (System.currentTimeMillis() - startTime)/1000) + " seconds");
    }

    public static void perft(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean WCQ, boolean WCK, boolean BCQ, boolean BCK, boolean WhiteToMove, int depth){
        if (depth < perftMaxDepth){
            String moves;
            if (WhiteToMove){
                moves = MovePiece.whitePossibleMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,WCQ,WCK);
            }
            else {
                moves = MovePiece.blackPossibleMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,BCQ,BCK);
            }
            
            for (int i = 0; i < moves.length(); i+=5) {
                // initialize temp bitboards for all pieces
                // initialize them as temp so the original bitboards don't get overridden
                long WPt=MovePiece.makeMove(WP, moves.substring(i,i+4), 'P'), WNt=MovePiece.makeMove(WN, moves.substring(i,i+4), 'N'),
                        WBt=MovePiece.makeMove(WB, moves.substring(i,i+4), 'B'), WRt=MovePiece.makeMove(WR, moves.substring(i,i+4), 'R'),
                        WQt=MovePiece.makeMove(WQ, moves.substring(i,i+4), 'Q'), WKt=MovePiece.makeMove(WK, moves.substring(i,i+4), 'K'),
                        BPt=MovePiece.makeMove(BP, moves.substring(i,i+4), 'p'), BNt=MovePiece.makeMove(BN, moves.substring(i,i+4), 'n'),
                        BBt=MovePiece.makeMove(BB, moves.substring(i,i+4), 'b'), BRt=MovePiece.makeMove(BR, moves.substring(i,i+4), 'r'),
                        BQt=MovePiece.makeMove(BQ, moves.substring(i,i+4), 'q'), BKt=MovePiece.makeMove(BK, moves.substring(i,i+4), 'k'),
                        EPt=MovePiece.makeMoveEP(WP|BP,moves.substring(i,i+4));
                // same with castling booleans
                boolean WCQt = WCQ, WCKt = WCK, BCQt = BCQ, BCKt = BCK;
                if (Character.isDigit(moves.charAt(i+3))){
                    // regular move
                    int start=(Character.getNumericValue(moves.charAt(i)) * 8) + (Character.getNumericValue(moves.charAt(i+1)));
                    if (((1L<<start) & WK)!=0) {
                        WCQt=false; WCKt=false;
                    }
                    if (((1L<<start) & BK)!=0) {
                        BCQt=false; BCKt=false;
                    }
                    if (((1L<<start) & WR & (1L<<63))!=0) {
                        WCKt=false;
                    }
                    if (((1L<<start) & WR & (1L<<56))!=0) {
                        WCQt=false;
                    }
                    if (((1L<<start) & BR & (1L<<7))!=0) {
                        BCKt=false;
                    }
                    if (((1L<<start) & BR & 1L)!=0) {
                        BCQt=false;
                    }
                }
                if (((WKt&MovePiece.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) || ((BKt&MovePiece.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
                    if (depth+1==perftMaxDepth) {
                        perftMoveCounter++;
                    }
                    perft(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,WCQt,WCKt,BCQt,BCKt,!WhiteToMove,depth+1);
                }
            }
        }
    }
}

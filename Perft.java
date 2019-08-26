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
    static int perftMaxDepth=4;
    static long startTime = 0;

    public static void perftRoot(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean WQC, boolean WKC, boolean BQC, boolean BKC, boolean WhiteToMove, int depth){
        startTime = System.currentTimeMillis();
        String moves;
        if (WhiteToMove){
            moves = MovePiece.whitePossibleMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,WQC,WKC);
        }
        else {
            moves = MovePiece.blackPossibleMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,BQC,BKC);
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
            boolean WQCt = WQC, WKCt = WKC, BQCt = BQC, BKCt = BKC;
            if (Character.isDigit(moves.charAt(i+3))){
                // regular move
                int start=(Character.getNumericValue(moves.charAt(i)) * 8) + (Character.getNumericValue(moves.charAt(i+1)));
                if (((1L<<start) & WK)!=0) {
                    WQCt=false; WKCt=false;
                }
                if (((1L<<start) & BK)!=0) {
                    BQCt=false; BKCt=false;
                }
                if (((1L<<start) & WR & (1L<<63))!=0) {
                    WKCt=false;
                }
                if (((1L<<start) & WR & (1L<<56))!=0) {
                    WQCt=false;
                }
                if (((1L<<start) & BR & (1L<<7))!=0) {
                    BKCt=false;
                }
                if (((1L<<start) & BR & 1L)!=0) {
                    BQCt=false;
                }
                if (((WKt&MovePiece.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) || ((BKt&MovePiece.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
                    perft(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,WQCt,WKCt,BQCt,BKCt,!WhiteToMove,depth+1);
//                    Board.generateVisualBoard(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt);
                    System.out.println(moveToAlgebra(moves.substring(i,i+4))+" "+perftMoveCounter);
                    perftTotalMoveCounter+=perftMoveCounter;
                    perftMoveCounter=0;
                }
            }
        }
        System.out.println("\ntotal: " + perftTotalMoveCounter);
        System.out.println("time: " + ((float) (System.currentTimeMillis() - startTime)/1000) + " seconds");
    }

    public static void perft(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean WQC, boolean WKC, boolean BQC, boolean BKC, boolean WhiteToMove, int depth){
        if (depth < perftMaxDepth){
            String moves;
            if (WhiteToMove){
                moves = MovePiece.whitePossibleMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,WQC,WKC);
            }
            else {
                moves = MovePiece.blackPossibleMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,BQC,BKC);
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
                boolean WQCt = WQC, WKCt = WKC, BQCt = BQC, BKCt = BKC;
                if (Character.isDigit(moves.charAt(i+3))){
                    // regular move
                    int start=(Character.getNumericValue(moves.charAt(i)) * 8) + (Character.getNumericValue(moves.charAt(i+1)));
                    if (((1L<<start) & WK)!=0) {
                        WQCt=false; WKCt=false;
                    }
                    if (((1L<<start) & BK)!=0) {
                        BQCt=false; BKCt=false;
                    }
                    if (((1L<<start) & WR & (1L<<63))!=0) {
                        WKCt=false;
                    }
                    if (((1L<<start) & WR & (1L<<56))!=0) {
                        WQCt=false;
                    }
                    if (((1L<<start) & BR & (1L<<7))!=0) {
                        BKCt=false;
                    }
                    if (((1L<<start) & BR & 1L)!=0) {
                        BQCt=false;
                    }
                }
                if (((WKt&MovePiece.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) || ((BKt&MovePiece.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
                    if (depth+1==perftMaxDepth) {
                        perftMoveCounter++;
                    }
                    perft(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,WQCt,WKCt,BQCt,BKCt,!WhiteToMove,depth+1);
                }
            }
        }
    }
}

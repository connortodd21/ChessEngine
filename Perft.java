public class Perft {

    static int perftMaxDepth = 1;
    static int perftMoveCounter = 0;

    public static void perft(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean WQC, boolean WKC, boolean BQC, boolean BKC, boolean WhiteToMove, int depth){
        MovePiece m = new MovePiece();
        if (depth < perftMaxDepth){
            String moves;
            if (WhiteToMove){
                moves = m.whitePossibleMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,WQC,WKC);
            }
            else {
                moves = m.blackPossibleMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,BQC,BKC);
            }
            for (int i = 0; i < moves.length(); i+=5) {
                // initialize temp bitboards for all pieces
                // initialize them as temp so the original bitboards don't get overridden
                long WPt=m.makeMove(WP, moves.substring(i,i+4), 'P'), WNt=m.makeMove(WN, moves.substring(i,i+4), 'N'),
                        WBt=m.makeMove(WB, moves.substring(i,i+4), 'B'), WRt=m.makeMove(WR, moves.substring(i,i+4), 'R'),
                        WQt=m.makeMove(WQ, moves.substring(i,i+4), 'Q'), WKt=m.makeMove(WK, moves.substring(i,i+4), 'K'),
                        BPt=m.makeMove(BP, moves.substring(i,i+4), 'p'), BNt=m.makeMove(BN, moves.substring(i,i+4), 'n'),
                        BBt=m.makeMove(BB, moves.substring(i,i+4), 'b'), BRt=m.makeMove(BR, moves.substring(i,i+4), 'r'),
                        BQt=m.makeMove(BQ, moves.substring(i,i+4), 'q'), BKt=m.makeMove(BK, moves.substring(i,i+4), 'k'),
                        EPt=m.makeMoveEP(WP|BP,moves.substring(i,i+4));
                // same with castling booleans
                boolean WQCt = WQC, WKCt = WKC, BQCt = BQC, BKCt = BKC;
                if (Character.isDigit(moves.charAt(3))){
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
                if (((WKt&m.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) || ((BKt&m.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
                    if (depth+1==perftMaxDepth) {
                        perftMoveCounter++;
                    }
                    perft(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,WQCt,WKCt,BQCt,BKCt,!WhiteToMove,depth+1);
                }
            }
        }
    }
}

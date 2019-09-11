package java;

public class PVS {

    public static int zeroWindow(){
        return 0;
    }

    public static int principalVariation(int alpha,int beta,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean WKC,boolean WQC,boolean BKC,boolean BQC,boolean WhiteToMove,int depth){
        int bestScore;
        int bestMoveIndex = -1;
        if(depth == Engine.searchDepth){
            return Rating.evaluate();
        }

        String moves;
        if(WhiteToMove){
            moves = MovePiece.whitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, WQC, WKC);
        }
        else{
            moves = MovePiece.blackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, BQC, BKC);
        }

        //sortMoves()

        return 0;
    }
}

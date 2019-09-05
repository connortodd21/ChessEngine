public class Engine {

    static long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L, EP = 0L;
    static boolean WCK = true, WCQ = true, BCK = true, BCQ = true, WhiteToMove = true;

    public static void main(String[] args) {
//        Board.initializeBoard();
        Board.importFEN("r2q1rk1/pppb1pp1/2n1p2p/3n4/1bBP3B/P1N1PN2/1P3PPP/R2Q1RK1 w - -");
//        RandomNumber.testDistribution();
//        Board.initializeBoard();
//        UCI.startUCI();
    }
}

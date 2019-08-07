public class Engine {

    public static void main(String[] args) {
        Board b = new Board();
        MovePiece m = new MovePiece();
        b.initializeBoard();
        System.out.println();
//        ChessUtilities.generateCustomBitboard();
//        ChessUtilities.printBitboard(Long.parseLong("0000000000000000000000001111101100000000000000000000000000000000",2));
//        System.out.println();
//        ChessUtilities.printBitboard(Long.parseLong("0000000000001111111111111111111111101111111111111111100000000000",2));
    }
}

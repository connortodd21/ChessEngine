import java.util.Arrays;

/*
    Utility class with methods to make coding the engine easier for the programmer
 */
public class ChessUtilities {
    /*
        Generate a bitboard based on the developer-specified board array
        Used to generate the static bitboards for diagonals and other challenging bitboards without having to think much
        or keep writing down 64-bit length strings that are prone to error
     */
    public static void generateCustomBitboard(){
        Board b = new Board();
        long bitBoard = 0L;
        String board [][] = {
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}
        };
        StringBuilder binary;
        for (int i = 0; i < 64; i++) {
            binary = new StringBuilder("0000000000000000000000000000000000000000000000000000000000000000");
            binary.setCharAt(i, '1');
            switch (board[i/8][i%8]){
                case "1": bitBoard += Board.binaryToLong(binary); break;
            }
        }
        for (int i = 0; i < Long.numberOfLeadingZeros(bitBoard); i++) {
            System.out.print("0");
        }
        System.out.print(Long.toBinaryString(bitBoard));
        System.out.println();
        Board.generateVisualBoard(bitBoard, 0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L);
    }

    public static void printBitboard(long bitboard) {
        String[][] board = new String[8][8];
        for (int i = 0; i < 64; i++) {
            board[i / 8][i % 8] = " ";
        }

        long highOrderBit = 1L << 63;

        for (int i = 0; i < 64; i++) {
            if (((bitboard << i) & highOrderBit) == highOrderBit) {
                board[i / 8][i % 8] = "1";
            }
        }
        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
        System.out.println();
    }

    public static void printEntireLongBinary(long value){
        for (int i = 0; i < Long.numberOfLeadingZeros(value); i++) {
            System.out.print('0');
        }
        System.out.println(Long.toBinaryString(value));
    }
}

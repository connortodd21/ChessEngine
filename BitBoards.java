import java.math.BigInteger;

/*
    A class containing various static bitboards used for determining location and value on a board

    This is made for more readability. Instead of doing all of this, you could just use
    raw long values as numbers (ex: 0x204081020408000L is a long value for one of the diagonals), but making
    many of these (especially the masks) is confusing and makes debugging more difficult. Using strings then converting to longs
    helps me visualize the bitboards to ensure I don't make mistakes with the static bitboards.
 */

public class BitBoards {
    /* bitboards  for different files, ranks, positions */

    /* bitboards for all files */
    private static String A_FILE_STRING = "1000000010000000100000001000000010000000100000001000000010000000";
    private static String B_FILE_STRING = "0100000001000000010000000100000001000000010000000100000001000000";
    private static String C_FILE_STRING = "0010000000100000001000000010000000100000001000000010000000100000";
    private static String D_FILE_STRING = "0001000000010000000100000001000000010000000100000001000000010000";
    private static String E_FILE_STRING = "0000100000001000000010000000100000001000000010000000100000001000";
    private static String F_FILE_STRING = "0000010000000100000001000000010000000100000001000000010000000100";
    private static String G_FILE_STRING = "0000001000000010000000100000001000000010000000100000001000000010";
    private static String H_FILE_STRING = "0000000100000001000000010000000100000001000000010000000100000001";
    static long A_FILE = new BigInteger(A_FILE_STRING, 2).longValue();
    static long B_FILE = new BigInteger(B_FILE_STRING, 2).longValue();
    static long C_FILE = new BigInteger(C_FILE_STRING, 2).longValue();
    static long D_FILE = new BigInteger(D_FILE_STRING, 2).longValue();
    static long E_FILE = new BigInteger(E_FILE_STRING, 2).longValue();
    static long F_FILE = new BigInteger(F_FILE_STRING, 2).longValue();
    static long G_FILE = new BigInteger(G_FILE_STRING, 2).longValue();
    static long H_FILE = new BigInteger(H_FILE_STRING, 2).longValue();
    // from A file to H file
    static long FILE_MASKS[] = { A_FILE, B_FILE, C_FILE, D_FILE, E_FILE, F_FILE, G_FILE, H_FILE };

    /* bitboards for all ranks */
    private static String RANK_8_STRING = "1111111100000000000000000000000000000000000000000000000000000000";
    private static String RANK_7_STRING = "0000000011111111000000000000000000000000000000000000000000000000";
    private static String RANK_6_STRING = "0000000000000000111111110000000000000000000000000000000000000000";
    private static String RANK_5_STRING = "0000000000000000000000001111111100000000000000000000000000000000";
    private static String RANK_4_STRING = "0000000000000000000000000000000011111111000000000000000000000000";
    private static String RANK_3_STRING = "0000000000000000000000000000000000000000111111110000000000000000";
    private static String RANK_2_STRING = "0000000000000000000000000000000000000000000000001111111100000000";
    private static String RANK_1_STRING = "0000000000000000000000000000000000000000000000000000000011111111";
    static long RANK_1=-new BigInteger(RANK_1_STRING, 2).longValue();
    static long RANK_2=-new BigInteger(RANK_2_STRING, 2).longValue();
    static long RANK_3=-new BigInteger(RANK_3_STRING, 2).longValue();
    static long RANK_4=new BigInteger(RANK_4_STRING, 2).longValue();
    static long RANK_5=new BigInteger(RANK_5_STRING, 2).longValue();
    static long RANK_6=-new BigInteger(RANK_6_STRING, 2).longValue();
    static long RANK_7=-new BigInteger(RANK_7_STRING, 2).longValue();
    static long RANK_8=new BigInteger(RANK_8_STRING, 2).longValue();
    // from rank 8 to rank 1
    static long RANK_MASKS[] = { RANK_1, RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7, RANK_8 };

    /* bitboards for all diagonals */
    // TOP LEFT TO BOTTOM RIGHT (A8 -> H1)
    private static String A1TOA1_STRING = "0000000000000000000000000000000000000000000000000000000010000000";
    private static String A2TOB1_STRING = "0000000000000000000000000000000000000000000000001000000001000000";
    private static String A3TOC1_STRING = "0000000000000000000000000000000000000000100000000100000000100000";
    private static String A4TOD1_STRING = "0000000000000000000000000000000010000000010000000010000000010000";
    private static String A5TOE1_STRING = "0000000000000000000000001000000001000000001000000001000000001000";
    private static String A6TOF1_STRING = "0000000000000000100000000100000000100000000100000000100000000100";
    private static String A7TOG1_STRING = "0000000010000000010000000010000000010000000010000000010000000010";
    private static String A8TOH1_STRING = "1000000001000000001000000001000000001000000001000000001000000001";
    private static String B8TOH2_STRING = "0100000000100000000100000000100000000100000000100000000100000000";
    private static String C8TOH3_STRING = "0010000000010000000010000000010000000010000000010000000000000000";
    private static String D8TOH4_STRING = "0001000000001000000001000000001000000001000000000000000000000000";
    private static String E8TOH5_STRING = "0000100000000100000000100000000100000000000000000000000000000000";
    private static String F8TOH6_STRING = "0000010000000010000000010000000000000000000000000000000000000000";
    private static String G8TOH7_STRING = "0000001000000001000000000000000000000000000000000000000000000000";
    private static String H8TOH8_STRING = "0000000100000000000000000000000000000000000000000000000000000000";
    static long A1TOA1 = new BigInteger(A1TOA1_STRING, 2).longValue();
    static long A2TOB2 = new BigInteger(A2TOB1_STRING, 2).longValue();
    static long A3TOC3 = new BigInteger(A3TOC1_STRING, 2).longValue();
    static long A4TOD4 = new BigInteger(A4TOD1_STRING, 2).longValue();
    static long A5TOE5 = new BigInteger(A5TOE1_STRING, 2).longValue();
    static long A6TOF6 = new BigInteger(A6TOF1_STRING, 2).longValue();
    static long A7TOG7 = new BigInteger(A7TOG1_STRING, 2).longValue();
    static long A8TOH8 = new BigInteger(A8TOH1_STRING, 2).longValue();
    static long B8TOH2 = new BigInteger(B8TOH2_STRING, 2).longValue();
    static long C8TOH3 = new BigInteger(C8TOH3_STRING, 2).longValue();
    static long D8TOH4 = new BigInteger(D8TOH4_STRING, 2).longValue();
    static long E8TOH5 = new BigInteger(E8TOH5_STRING, 2).longValue();
    static long F8TOH6 = new BigInteger(F8TOH6_STRING, 2).longValue();
    static long G8TOH7 = new BigInteger(G8TOH7_STRING, 2).longValue();
    static long H8TOH8 = new BigInteger(H8TOH8_STRING, 2).longValue();
    static long DIAGONAL_MASKS_RIGHT_TO_LEFT[] = { A1TOA1, A2TOB2, A3TOC3, A4TOD4, A5TOE5, A6TOF6, A7TOG7, A8TOH8,
                                                   B8TOH2, C8TOH3, D8TOH4, E8TOH5, F8TOH6, G8TOH7, H8TOH8 };

    // TOP RIGHT TO BOTTOM LEFT (H8 -> A1)
    private static String A8TOA8_STRING = "1000000000000000000000000000000000000000000000000000000000000000";
    private static String A7TOB8_STRING = "0100000010000000000000000000000000000000000000000000000000000000";
    private static String A6TOC8_STRING = "0010000001000000100000000000000000000000000000000000000000000000";
    private static String A5TOD8_STRING = "0001000000100000010000001000000000000000000000000000000000000000";
    private static String A4TOE8_STRING = "0000100000010000001000000100000010000000000000000000000000000000";
    private static String A3TOF8_STRING = "0000010000001000000100000010000001000000100000000000000000000000";
    private static String A2TOG8_STRING = "0000001000000100000010000001000000100000010000001000000000000000";
    private static String A1TOH8_STRING = "0000000100000010000001000000100000010000001000000100000010000000";
    private static String B1TOH7_STRING = "0000000000000001000000100000010000001000000100000010000001000000";
    private static String C1TOH6_STRING = "0000000000000000000000010000001000000100000010000001000000100000";
    private static String D1TOH5_STRING = "0000000000000000000000000000000100000010000001000000100000010000";
    private static String E1TOH4_STRING = "0000000000000000000000000000000000000001000000100000010000001000";
    private static String F1TOH3_STRING = "0000000000000000000000000000000000000000000000010000001000000100";
    private static String G1TOH2_STRING = "0000000000000000000000000000000000000000000000000000000100000010";
    private static String H1TOH1_STRING = "0000000000000000000000000000000000000000000000000000000000000001";
    static long A8TOA8 = new BigInteger(A8TOA8_STRING, 2).longValue();
    static long A7TOB8 = new BigInteger(A7TOB8_STRING, 2).longValue();
    static long A6TOC8 = new BigInteger(A6TOC8_STRING, 2).longValue();
    static long A5TOD8 = new BigInteger(A5TOD8_STRING, 2).longValue();
    static long A4TOE8 = new BigInteger(A4TOE8_STRING, 2).longValue();
    static long A3TOF8 = new BigInteger(A3TOF8_STRING, 2).longValue();
    static long A2TOG8 = new BigInteger(A2TOG8_STRING, 2).longValue();
    static long A1TOH8 = new BigInteger(A1TOH8_STRING, 2).longValue();
    static long B1TOH7 = new BigInteger(B1TOH7_STRING, 2).longValue();
    static long C1TOH6 = new BigInteger(C1TOH6_STRING, 2).longValue();
    static long D1TOH5 = new BigInteger(D1TOH5_STRING, 2).longValue();
    static long E1TOH4 = new BigInteger(E1TOH4_STRING, 2).longValue();
    static long F1TOH3 = new BigInteger(F1TOH3_STRING, 2).longValue();
    static long G1TOH2 = new BigInteger(G1TOH2_STRING, 2).longValue();
    static long H1TOH1 = new BigInteger(H1TOH1_STRING, 2).longValue();
    static long DIAGONAL_MASKS_LEFT_TO_RIGHT[] = { A8TOA8, A7TOB8, A6TOC8, A5TOD8, A4TOE8, A3TOF8, A2TOG8, A1TOH8,
                                                   B1TOH7, C1TOH6, D1TOH5, E1TOH4, F1TOH3, G1TOH2, H1TOH1 };
}

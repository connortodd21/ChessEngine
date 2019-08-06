import java.math.BigInteger;

public class MovePiece {
    /* bitboards for different files, ranks, positions */
    /* This is done for legibility, you can just use the raw long value instead */
    static String A_FILE_STRING = "1000000010000000100000001000000010000000100000001000000010000000";
    static String B_FILE_STRING = "0100000001000000010000000100000001000000010000000100000001000000";
    static String C_FILE_STRING = "0010000000100000001000000010000000100000001000000010000000100000";
    static String D_FILE_STRING = "0001000000010000000100000001000000010000000100000001000000010000";
    static String E_FILE_STRING = "0000100000001000000010000000100000001000000010000000100000001000";
    static String F_FILE_STRING = "0000010000000100000001000000010000000100000001000000010000000100";
    static String G_FILE_STRING = "0000001000000010000000100000001000000010000000100000001000000010";
    static String H_FILE_STRING = "0000000100000001000000010000000100000001000000010000000100000001";
    static String RANK_8_STRING = "1111111100000000000000000000000000000000000000000000000000000000";
    static String RANK_7_STRING = "0000000011111111000000000000000000000000000000000000000000000000";
    static String RANK_6_STRING = "0000000000000000111111110000000000000000000000000000000000000000";
    static String RANK_5_STRING = "0000000000000000000000001111111100000000000000000000000000000000";
    static String RANK_4_STRING = "0000000000000000000000000000000011111111000000000000000000000000";
    static String RANK_3_STRING = "0000000000000000000000000000000000000000111111110000000000000000";
    static String RANK_2_STRING = "0000000000000000000000000000000000000000000000001111111100000000";
    static String RANK_1_STRING = "0000000000000000000000000000000000000000000000000000000011111111";
    /* bitboards converted to long value for different files, ranks, positions */
    static long A_FILE = new BigInteger(A_FILE_STRING, 2).longValue();
    static long B_FILE = new BigInteger(B_FILE_STRING, 2).longValue();
    static long C_FILE = new BigInteger(C_FILE_STRING, 2).longValue();
    static long D_FILE = new BigInteger(D_FILE_STRING, 2).longValue();
    static long E_FILE = new BigInteger(E_FILE_STRING, 2).longValue();
    static long F_FILE = new BigInteger(F_FILE_STRING, 2).longValue();
    static long G_FILE = new BigInteger(G_FILE_STRING, 2).longValue();
    static long H_FILE = new BigInteger(H_FILE_STRING, 2).longValue();
    static long RANK_1=-new BigInteger(RANK_1_STRING, 2).longValue();
    static long RANK_2=-new BigInteger(RANK_2_STRING, 2).longValue();
    static long RANK_3=-new BigInteger(RANK_3_STRING, 2).longValue();
    static long RANK_4=new BigInteger(RANK_4_STRING, 2).longValue();
    static long RANK_5=new BigInteger(RANK_5_STRING, 2).longValue();
    static long RANK_6=-new BigInteger(RANK_6_STRING, 2).longValue();
    static long RANK_7=-new BigInteger(RANK_7_STRING, 2).longValue();
    static long RANK_8=new BigInteger(RANK_8_STRING, 2).longValue();
    static long WHTIE_CAN_CAPTURE;
    static long BLACK_PIECE;
    static long EMPTY;
    // from rank 8 to rank 1
    static long RANKMASKS[] = { RANK_1, RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7, RANK_8 };
    // from A file to H file
    static long FILEMASKS[] = { A_FILE, B_FILE, C_FILE, D_FILE, E_FILE, F_FILE, G_FILE, H_FILE };

    public String whitePossibleMoves(String history,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK){
        WHTIE_CAN_CAPTURE = BP | BN | BB | BR | BQ ;
        //NOT_WHITE_PIECES=~(WP|WN|WB|WR|WQ|WK|BK);
        BLACK_PIECE = BP | BN | BB | BR | BQ | BK;
        EMPTY = ~(WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK);
        String possibleMoves = WPPossibleMoves(history, WP);
        return possibleMoves;
    }

    public String WPPossibleMoves(String history, long WP){
        StringBuilder list = new StringBuilder(); // stored as a string: <white x> <white y> <black x> <black y>
        // CAPTURE RIGHT
        // Shifting the pawn over by 7 on the bitboard results in the square diagonal to the right of the pawn (i.e. the square to capture to the right)
        // White can only capture is there is a black piece on the right diagonal. Capturing to the right on the A file is impossible, and the 8th rank is for pawn promotion
        long possibleMove = (WP<<7) & BLACK_PIECE & ~RANK_8 & ~A_FILE;
        long singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append((int) Math.ceil((double) i/8)+""+Math.abs((i-2)%8)+""+(int) Math.floor((double) (i-1)/8)+""+Math.abs((i-1)%8)+",");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        /*
        LEGACY RIGHT CAPTURE. LESS EFFICIENT BUT MORE INTUITIVE
        for (int i = Long.numberOfLeadingZeros(possibleMove); i < 64; i++) {
            if (((possibleMove>>(64-i)) & 1) == 1) {
                System.out.println("value of i: " + i);
                list.append((int) Math.ceil((double) i/8)+""+Math.abs((i-2)%8)+""+(int) Math.floor((double) (i-1)/8)+""+Math.abs((i-1)%8));
            }
        }
        */
        // CAPTURE LEFT
        // same idea as capture right, just shift over by 9 (two more squares) and ignore black pawns on the H file
        possibleMove = (WP<<9) & BLACK_PIECE & ~RANK_8 & ~H_FILE;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append((int) Math.ceil((double) i/8)+""+Math.abs((i)%8)+""+(int) Math.floor((double) (i-1)/8)+""+Math.abs((i-1)%8)+",");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // MOVE 1 FORWARD
        // shift over by 8 (square in front of pawn) and make sure there is no piece there
        possibleMove = (WP<<8) & ~RANK_8 & EMPTY;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append((int) Math.ceil((double) (i)/8)+""+Math.abs((i-1)%8)+""+((int) Math.ceil((double) (i)/8) - 1)+""+Math.abs((i-1)%8)+",");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // MOVE 2 FORWARD
        // big thing is to check that both squares in front of the pawn are empty and that the destination is on rank 4
        // this avoids pawns jumping over pieces, and makes sure only starting pawns (on rank 2) can jump two squares
        possibleMove = (WP<<16) & EMPTY & (EMPTY << 8) & RANK_4;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove) + 8;
            list.append((int) Math.ceil((double) (i)/8)+""+Math.abs((i-1)%8)+""+((int) Math.ceil((double) (i)/8) - 2)+""+Math.abs((i-1)%8)+",");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // PROMOTE BY CAPTURE RIGHT
        // only difference from regular capture right is that this checks the pawn will be capturing on the 8th rank (and therefore promoting)
        // can promote to a queen, knight, rook, or bishop
        // since white pawn promotion always happens from the 7th to 8th rank, we really only need to track the file change and possible promotion pieces
        // promotions are stored as <old y> <new y> <Q|R|N|B> <P>
        possibleMove = (WP<<7) & BLACK_PIECE & RANK_8 & ~A_FILE;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(Math.abs((i-2)%8)+""+Math.abs((i-1)%8)+"QP," +Math.abs((i-2)%8)+""+Math.abs((i-1)%8)+"RP," + Math.abs((i-2)%8)+""+Math.abs((i-1)%8)+ "NP," + Math.abs((i-2)%8)+""+Math.abs((i-1)%8)+ "BP,");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // PROMOTE BY CAPTURE LEFT
        possibleMove = (WP<<9) & BLACK_PIECE & RANK_8 & ~A_FILE;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(Math.abs((i)%8)+""+Math.abs((i-1)%8)+"QP," +Math.abs((i)%8)+""+Math.abs((i-1)%8)+"RP," + Math.abs((i)%8)+""+Math.abs((i-1)%8)+ "NP," + Math.abs((i)%8)+""+Math.abs((i-1)%8)+ "BP,");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // PROMOTE BY PUSH
        possibleMove = (WP<<8) & BLACK_PIECE & RANK_8 & ~A_FILE;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(Math.abs((i-1)%8)+""+Math.abs((i-1)%8)+"QP," +Math.abs((i-1)%8)+""+Math.abs((i-1)%8)+"RP," + Math.abs((i-1)%8)+""+Math.abs((i-1)%8)+ "NP," + Math.abs((i-1)%8)+""+Math.abs((i-1)%8)+ "BP,");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // EN PASSANT
        // en passant moves are stored as <old x> <new x> <EN>
        if (history.length() >= 5){
            // history is stored as comma separated list, so here just grab the substring to ignore the comma
            String newHistory = history.substring(history.length()-5, history.length()-1);
            // if a pawn was moved up the board by two squares (just check the x
            if (newHistory.charAt(newHistory.length()-1) == newHistory.charAt(newHistory.length()-3) && Math.abs(newHistory.charAt(newHistory.length()-2)) - Math.abs(newHistory.charAt(newHistory.length()-4)) == 2){
                // grab the destination file on which the opposing pawn is located (we want to capture diagonally to this file)
                int pawnFile = newHistory.charAt(newHistory.length()-1) - '0';
                // EN PASSANT RIGHT
                // We already know that we have a situation where en passant is possible given the previous move in the history
                // So we want to check that there is a black piece on the 5th rank (where a black pawn would be after a forward push by two)
                // This bit operation will show the piece to remove, NOT the destination for the white pawn
                long enPassantPossibleMove = (WP >> 1) & BLACK_PIECE & RANK_5 & ~A_FILE & FILEMASKS[pawnFile];
                if (enPassantPossibleMove != 0){
                    int i = 64 - Long.numberOfTrailingZeros(enPassantPossibleMove);
                    list.append((i%8)-1 + "" + i/8 + "EN");
                }
                // EN PASSANT LEFT
                enPassantPossibleMove = (WP << 1) & BLACK_PIECE & RANK_5 & ~A_FILE & FILEMASKS[pawnFile];
                if (enPassantPossibleMove != 0){
                    int i = 64 - Long.numberOfTrailingZeros(enPassantPossibleMove);
                    list.append((i%8)+1 + "" + i/8 + "EN");
                }
            }
        }
        System.out.println(list);
        return list.toString();
    }
}

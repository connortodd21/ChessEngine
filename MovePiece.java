public class MovePiece {
    /* Bitboards to indicate different game states */
    static long WHTIE_CAN_CAPTURE;
    static long BLACK_PIECE;
    static long EMPTY;
    static long OCCUPIED;

    /*
        Returns all the possible moves that white can make, including captures
        //TODO: include castling

        @param
        history:                       the move history of the current game
        WP, WN, WB, WR, WQ, WK:        bitboards for all white pieces
        BP, BN, BB, BR, BQ, BK:        bitboards for all black pieces

        @return                        all possible moves and captures for white
     */
    public String whitePossibleMoves(String history,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK){
        WHTIE_CAN_CAPTURE = BP | BN | BB | BR | BQ ;
        //NOT_WHITE_PIECES=~(WP|WN|WB|WR|WQ|WK|BK);
        BLACK_PIECE = BP | BN | BB | BR | BQ | BK;
        EMPTY = ~(WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK);
        String possibleMoves = WPPossibleMoves(history, WP);
        return possibleMoves;
    }

    /*
        Returns all the possible moves that white pawns can make, including captures and en passant

        @param
        history:         move history of the current game
        WP:              bitboard for the white pawns on the board

        @return          all of the possible moves, captures, and promotions including en passant
     */
    public String WPPossibleMoves(String history, long WP){
        StringBuilder list = new StringBuilder(); // stored as a string: <white x> <white y> <black x> <black y>
        // CAPTURE RIGHT
        // Shifting the pawn over by 7 on the bitboard results in the square diagonal to the right of the pawn (i.e. the square to capture to the right)
        // White can only capture is there is a black piece on the right diagonal. Capturing to the right on the A file is impossible, and the 8th rank is for pawn promotion
        long possibleMove = (WP<<7) & BLACK_PIECE & ~BitBoards.RANK_8 & ~BitBoards.A_FILE;
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
        possibleMove = (WP<<9) & BLACK_PIECE & ~BitBoards.RANK_8 & ~BitBoards.H_FILE;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append((int) Math.ceil((double) i/8)+""+Math.abs((i)%8)+""+(int) Math.floor((double) (i-1)/8)+""+Math.abs((i-1)%8)+",");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // MOVE 1 FORWARD
        // shift over by 8 (square in front of pawn) and make sure there is no piece there
        possibleMove = (WP<<8) & ~BitBoards.RANK_8 & EMPTY;
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
        possibleMove = (WP<<16) & EMPTY & (EMPTY << 8) & BitBoards.RANK_4;
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
        possibleMove = (WP<<7) & BLACK_PIECE & BitBoards.RANK_8 & ~BitBoards.A_FILE;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(Math.abs((i-2)%8)+""+Math.abs((i-1)%8)+"QP," +Math.abs((i-2)%8)+""+Math.abs((i-1)%8)+"RP," + Math.abs((i-2)%8)+""+Math.abs((i-1)%8)+ "NP," + Math.abs((i-2)%8)+""+Math.abs((i-1)%8)+ "BP,");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // PROMOTE BY CAPTURE LEFT
        possibleMove = (WP<<9) & BLACK_PIECE & BitBoards.RANK_8 & ~BitBoards.A_FILE;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(Math.abs((i)%8)+""+Math.abs((i-1)%8)+"QP," +Math.abs((i)%8)+""+Math.abs((i-1)%8)+"RP," + Math.abs((i)%8)+""+Math.abs((i-1)%8)+ "NP," + Math.abs((i)%8)+""+Math.abs((i-1)%8)+ "BP,");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // PROMOTE BY PUSH
        possibleMove = (WP<<8) & BLACK_PIECE & BitBoards.RANK_8 & ~BitBoards.A_FILE;
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
                long enPassantPossibleMove = (WP >> 1) & BLACK_PIECE & BitBoards.RANK_5 & ~BitBoards.A_FILE & BitBoards.FILE_MASKS[pawnFile];
                if (enPassantPossibleMove != 0){
                    int i = 64 - Long.numberOfTrailingZeros(enPassantPossibleMove);
                    list.append((i%8)-1 + "" + i/8 + "EN");
                }
                // EN PASSANT LEFT
                enPassantPossibleMove = (WP << 1) & BLACK_PIECE & BitBoards.RANK_5 & ~BitBoards.A_FILE & BitBoards.FILE_MASKS[pawnFile];
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

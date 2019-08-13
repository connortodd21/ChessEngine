public class MovePiece {
    /* Bitboards to indicate different game states */
    static long WHTIE_CAN_MOVE;
    static long BLACK_PIECE;
    static long EMPTY;
    static long OCCUPIED;
    /*
        Returns a bitboard of all possible vertical and horizontal moves from the position specified

        @param
        index:          integer location of a piece on the board

        @return         all possible vertical and horizontal moves and captures for the piece
     */

    static long HorizontalAndVerticalMoves(int piece)
    {
        piece = 64-piece;
        long pieceBitboard=1L<<piece;
        // horizontal moves = (OCC - 2 * board) ^ (OCC' - 2 * board')'
        long horizontalMoves = (OCCUPIED - 2 * pieceBitboard) ^ Long.reverse(Long.reverse(OCCUPIED) - 2 * Long.reverse(pieceBitboard));
        // same as horizontal moves, just apply a mask to translate the row into a column
        long verticalMoves = ((OCCUPIED & BitBoards.FILE_MASKS[7-(piece % 8)]) - (2 * pieceBitboard)) ^ Long.reverse(Long.reverse(OCCUPIED & BitBoards.FILE_MASKS[7-(piece % 8)]) - (2 * Long.reverse(pieceBitboard)));
        return (horizontalMoves & BitBoards.RANK_MASKS[(piece / 8)]) | (verticalMoves & BitBoards.FILE_MASKS[7-(piece % 8)]);
    }

    /*
        Returns a bitboard of all possible diagonal moves from the position specified

        @param
        index:          integer location of a piece on the board

        @return         all possible diagonal moves and captures for the piece
     */

    static long DiagonalLeftAndRightMoves(int piece)
    {
        piece = 64-piece;
        long pieceBitboard = 1L << piece;
        long diagonalMoves = ((OCCUPIED & BitBoards.DIAGONAL_MASKS_LEFT_TO_RIGHT[(piece / 8) + 7 - (piece % 8)]) - (2 * pieceBitboard)) ^ Long.reverse(Long.reverse(OCCUPIED & BitBoards.DIAGONAL_MASKS_LEFT_TO_RIGHT[(piece / 8) + 7 - (piece % 8)]) - (2 * Long.reverse(pieceBitboard)));
        long antiDiagonalMoves = ((OCCUPIED & BitBoards.DIAGONAL_MASKS_RIGHT_TO_LEFT[14 - ((piece / 8) + (piece % 8))]) - (2 * pieceBitboard)) ^ Long.reverse(Long.reverse(OCCUPIED & BitBoards.DIAGONAL_MASKS_RIGHT_TO_LEFT[14 - ((piece / 8) + (piece % 8))]) - (2 * Long.reverse(pieceBitboard)));
        return (diagonalMoves & BitBoards.DIAGONAL_MASKS_LEFT_TO_RIGHT[(piece / 8) + 7 - (piece % 8)]) | (antiDiagonalMoves & BitBoards.DIAGONAL_MASKS_RIGHT_TO_LEFT[14 - ((piece / 8) + (piece % 8))]);
    }

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
        WHTIE_CAN_MOVE = ~(WP|WN|WB|WR|WQ|WK|BK);
        BLACK_PIECE = BP | BN | BB | BR | BQ | BK;
        EMPTY = ~(WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK);
        OCCUPIED = ~EMPTY;
        StringBuilder possibleMoves = new StringBuilder();
        possibleMoves.append(WPPossibleMoves(history, WP));
        possibleMoves.append(WBPossibleMoves(WB));
        possibleMoves.append(WRPossibleMoves(WR));
        possibleMoves.append(WQPossibleMoves(WQ));
        possibleMoves.append(WNPossibleMoves(WN));
        possibleMoves.append(WKPossibleMoves(WK));
        System.out.println(possibleMoves.toString());
        return possibleMoves.toString();
    }

    /*
        Returns all the possible moves that white pawns can make, including captures and en passant

        @param
        history:         move history of the current game
        WP:              bitboard for the white pawns on the board

        @return          string list of all of the possible moves, captures, and promotions including en passant
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
        return list.toString();
    }

    /*
        Returns all the possible moves that white knights can make, including captures

        @param
        WN:              bitboard for the white knights on the board

        @return          string list of all of the possible moves and captures
    */
    public String WNPossibleMoves(long WN){
        StringBuilder list = new StringBuilder();
        long i = WN & -WN; //find knights
        long possibleMove;
        while(i != 0)
        {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            // I check 46 because the KNIGHT_SPACES static is the possible knight moves on f3, which is position 46 on the bitboard
            if (boardIndex > 46){
                possibleMove = BitBoards.KNIGHT_SPACES >> (boardIndex - 46);
            }
            else {
                possibleMove = BitBoards.KNIGHT_SPACES << (46 - boardIndex);
            }
            if (boardIndex % 8 < 4){
                possibleMove &= ~BitBoards.FILE_GH & WHTIE_CAN_MOVE;
            }
            else {
                possibleMove &= ~BitBoards.FILE_AB & WHTIE_CAN_MOVE;
            }
            long j = possibleMove & -possibleMove;
            calculateMove(list, possibleMove, boardIndex, j);
            WN &= ~i;
            i = WN & -WN;
        }
        return list.toString();
    }

    /*
        Returns all the possible moves that white bishops can make, including captures

        @param
        WB:              bitboard for the white bishops on the board

        @return          string list of all of the possible moves and captures
     */
    public String WBPossibleMoves(long WB){
        StringBuilder list = new StringBuilder();
        long i = WB & -WB; //find bishops
        long possibleMove;
        while(i != 0)
        {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            possibleMove = DiagonalLeftAndRightMoves(boardIndex) & WHTIE_CAN_MOVE;
            long j = possibleMove & -possibleMove;
            calculateMove(list, possibleMove, boardIndex, j);
            WB &= ~i;
            i = WB & - WB;
        }
        return list.toString();
    }

    /*
        Returns all the possible moves that white rooks can make, including captures

        @param
        WR:              bitboard for the white rooks on the board

        @return          string list of all of the possible moves and captures
     */
    public String WRPossibleMoves(long WR){
        StringBuilder list = new StringBuilder();
        long i = WR & -WR; //find rooks
        long possibleMove;
        while(i != 0)
        {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            possibleMove = HorizontalAndVerticalMoves(boardIndex) & WHTIE_CAN_MOVE;
            long j = possibleMove & -possibleMove;
            calculateMove(list, possibleMove, boardIndex, j);
            WR &= ~i;
            i = WR & - WR;
        }
        return list.toString();
    }
    /*
        Returns all the possible moves that white queens can make, including captures

        @param
        WQ:              bitboard for the white queens on the board

        @return          string list of all of the possible moves and captures
    */
    public String WQPossibleMoves(long WQ){
        StringBuilder list = new StringBuilder();
        long i = WQ & -WQ; //find queens
        long possibleMove;
        while(i != 0)
        {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            possibleMove = (HorizontalAndVerticalMoves(boardIndex) | DiagonalLeftAndRightMoves(boardIndex)) & WHTIE_CAN_MOVE;
            long j = possibleMove & -possibleMove;
            calculateMove(list, possibleMove, boardIndex, j);
            WQ &= ~i;
            i = WQ & - WQ;
        }
        return list.toString();
    }

    /*
        Returns all the possible moves that white knights can make, including captures

        @param
        WN:              bitboard for the white knights on the board

        @return          string list of all of the possible moves and captures
    */
    public String WKPossibleMoves(long WK){
        StringBuilder list = new StringBuilder();
        long i = WK & -WK; //find king
        long possibleMove;
        while(i != 0)
        {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            // I check 46 because the KNIGHT_SPACES static is the possible knight moves on f3, which is position 46 on the bitboard
            if (boardIndex > 55){
                possibleMove = BitBoards.KING_SPACES >> (boardIndex - 55);
            }
            else {
                possibleMove = BitBoards.KING_SPACES << (55 - boardIndex);
            }
            if (boardIndex % 8 < 4){
                possibleMove &= ~BitBoards.FILE_GH & WHTIE_CAN_MOVE;
            }
            else {
                possibleMove &= ~BitBoards.FILE_AB & WHTIE_CAN_MOVE;
            }
            long j = possibleMove & -possibleMove;
            calculateMove(list, possibleMove, boardIndex, j);
            WK &= ~i;
            i = WK & -WK;
        }
        return list.toString();
    }

    /*
        Helper method for the finding moves methods.
     */
    private void calculateMove(StringBuilder list, long possibleMove, int boardIndex, long j) {
        while (j != 0)
        {
            int index = Long.numberOfLeadingZeros(j);
            list.append(((boardIndex-1)/8)+""+((boardIndex-1)%8)+""+((index)/8)+""+((index)%8)+",");
            possibleMove &= ~j;
            j = possibleMove & - possibleMove;
        }
    }
}

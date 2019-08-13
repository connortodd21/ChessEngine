public class MovePiece {
    /* Bitboards to indicate different game states */
    static long NOT_ENEMY_PIECES;
    static long ENEMY_PIECES;
    static long EMPTY;
    static long OCCUPIED;


    static long HorizontalAndVerticalMoves(int piece) {
        piece = 64-piece;
        long pieceBitboard=1L<<piece;
        // horizontal moves = (OCC - 2 * board) ^ (OCC' - 2 * board')'
        long horizontalMoves = (OCCUPIED - 2 * pieceBitboard) ^ Long.reverse(Long.reverse(OCCUPIED) - 2 * Long.reverse(pieceBitboard));
        // same as horizontal moves, just apply a mask to translate the row into a column
        long verticalMoves = ((OCCUPIED & BitBoards.FILE_MASKS[7-(piece % 8)]) - (2 * pieceBitboard)) ^ Long.reverse(Long.reverse(OCCUPIED & BitBoards.FILE_MASKS[7-(piece % 8)]) - (2 * Long.reverse(pieceBitboard)));
        return (horizontalMoves & BitBoards.RANK_MASKS[(piece / 8)]) | (verticalMoves & BitBoards.FILE_MASKS[7-(piece % 8)]);
    }


    static long DiagonalLeftAndRightMoves(int piece) {
        piece = 64-piece;
        long pieceBitboard = 1L << piece;
        long diagonalMoves = ((OCCUPIED & BitBoards.DIAGONAL_MASKS_LEFT_TO_RIGHT[(piece / 8) + 7 - (piece % 8)]) - (2 * pieceBitboard)) ^ Long.reverse(Long.reverse(OCCUPIED & BitBoards.DIAGONAL_MASKS_LEFT_TO_RIGHT[(piece / 8) + 7 - (piece % 8)]) - (2 * Long.reverse(pieceBitboard)));
        long antiDiagonalMoves = ((OCCUPIED & BitBoards.DIAGONAL_MASKS_RIGHT_TO_LEFT[14 - ((piece / 8) + (piece % 8))]) - (2 * pieceBitboard)) ^ Long.reverse(Long.reverse(OCCUPIED & BitBoards.DIAGONAL_MASKS_RIGHT_TO_LEFT[14 - ((piece / 8) + (piece % 8))]) - (2 * Long.reverse(pieceBitboard)));
        return (diagonalMoves & BitBoards.DIAGONAL_MASKS_LEFT_TO_RIGHT[(piece / 8) + 7 - (piece % 8)]) | (antiDiagonalMoves & BitBoards.DIAGONAL_MASKS_RIGHT_TO_LEFT[14 - ((piece / 8) + (piece % 8))]);
    }

    public String whitePossibleMoves(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK, long EP, boolean WQC, boolean WKC){
        NOT_ENEMY_PIECES = ~(WP|WN|WB|WR|WQ|WK|BK);
        ENEMY_PIECES = BP | BN | BB | BR | BQ ;
        EMPTY = ~(WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK);
        OCCUPIED = ~EMPTY;
        StringBuilder possibleMoves = new StringBuilder();
        possibleMoves.append(WPPossibleMoves(WP, EP, BP));
        possibleMoves.append(BPossibleMoves(WB));
        possibleMoves.append(RPossibleMoves(WR));
        possibleMoves.append(QPossibleMoves(WQ));
        possibleMoves.append(NPossibleMoves(WN));
        possibleMoves.append(KPossibleMoves(WK));
        possibleMoves.append(WKCastle(WR, WQC, WKC));
        System.out.println(possibleMoves.toString());
        return possibleMoves.toString();
    }

    public String blackPossibleMoves(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK, long EP, boolean BQC, boolean BKC){
        NOT_ENEMY_PIECES = ~(BP|BN|BB|BR|BQ|BK|WK);
        ENEMY_PIECES = WP | WN |WB | WR | WQ;
        EMPTY = ~(WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK);
        OCCUPIED = ~EMPTY;
        StringBuilder possibleMoves = new StringBuilder();
        possibleMoves.append(BPPossibleMoves(BP, EP, WP));
//        possibleMoves.append(BPossibleMoves(BB));
//        possibleMoves.append(RPossibleMoves(BR));
//        possibleMoves.append(QPossibleMoves(BQ));
//        possibleMoves.append(NPossibleMoves(BN));
//        possibleMoves.append(KPossibleMoves(BK));
//        possibleMoves.append(WKCastle(BR, BQC, BKC));
        System.out.println(possibleMoves.toString());
        return possibleMoves.toString();
    }


    public String WPPossibleMoves(long WP, long EP, long BP){
        StringBuilder list = new StringBuilder(); // stored as a string: <white x> <white y> <black x> <black y>
        // CAPTURE RIGHT
        // Shifting the pawn over by 7 on the bitboard results in the square diagonal to the right of the pawn (i.e. the square to capture to the right)
        // White can only capture is there is a black piece on the right diagonal. Capturing to the right on the A file is impossible, and the 8th rank is for pawn promotion
        long possibleMove = (WP<<7) & ENEMY_PIECES & ~BitBoards.RANK_8 & ~BitBoards.A_FILE;
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
        possibleMove = (WP<<9) & ENEMY_PIECES & ~BitBoards.RANK_8 & ~BitBoards.H_FILE;
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
        possibleMove = (WP<<7) & ENEMY_PIECES & BitBoards.RANK_8 & ~BitBoards.A_FILE;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(Math.abs((i-2)%8)+""+Math.abs((i-1)%8)+"QP," +Math.abs((i-2)%8)+""+Math.abs((i-1)%8)+"RP," + Math.abs((i-2)%8)+""+Math.abs((i-1)%8)+ "BP," + Math.abs((i-2)%8)+""+Math.abs((i-1)%8)+ "NP,");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // PROMOTE BY CAPTURE LEFT
        possibleMove = (WP<<9) & ENEMY_PIECES & BitBoards.RANK_8 & ~BitBoards.A_FILE;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(Math.abs((i)%8)+""+Math.abs((i-1)%8)+"QP," +Math.abs((i)%8)+""+Math.abs((i-1)%8)+"RP," + Math.abs((i)%8)+""+Math.abs((i-1)%8)+ "BP," + Math.abs((i)%8)+""+Math.abs((i-1)%8)+ "NP,");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // PROMOTE BY PUSH
        possibleMove = (WP<<8) & ENEMY_PIECES & BitBoards.RANK_8;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(Math.abs((i-1)%8)+""+Math.abs((i-1)%8)+"QP," +Math.abs((i-1)%8)+""+Math.abs((i-1)%8)+"RP," + Math.abs((i-1)%8)+""+Math.abs((i-1)%8)+ "BP," + Math.abs((i-1)%8)+""+Math.abs((i-1)%8)+ "NP,");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // EN PASSANT
        // en passant moves are stored as <old x> <new x> <EN>
        // capture right
        possibleMove =(WP >> 1) & BP & BitBoards.RANK_5 & ~BitBoards.A_FILE & EP;
        if (possibleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(possibleMove);
            list.append((i%8)-1 + "" + i/8 + "EN");
        }
        // capture left
        possibleMove =(WP << 1) & BP & BitBoards.RANK_5 & ~BitBoards.H_FILE & EP;
        if (possibleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(possibleMove);
            list.append((i%8)+1 + "" + i/8 + "EN");
        }
        return list.toString();
    }

    public String BPPossibleMoves(long BP, long EP, long WP){
        StringBuilder list = new StringBuilder(); // stored as a string: <white x> <white y> <black x> <black y>
        // CAPTURE RIGHT
        // Shifting the pawn over by 7 on the bitboard results in the square diagonal to the right of the pawn (i.e. the square to capture to the right)
        // White can only capture is there is a black piece on the right diagonal. Capturing to the right on the A file is impossible, and the 8th rank is for pawn promotion
        long possibleMove = (BP>>7) & ENEMY_PIECES & ~BitBoards.RANK_1 & ~BitBoards.H_FILE;
        long singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(i/8 -1 +""+Math.abs((i-2)%8)+""+(int) Math.floor((double) (i-1)/8)+""+Math.abs((i-1)%8)+",");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // CAPTURE LEFT
        // same idea as capture right, just shift over by 9 (two more squares) and ignore black pawns on the H file
        possibleMove = (BP >> 9) & ENEMY_PIECES & ~BitBoards.RANK_1 & ~BitBoards.A_FILE;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(i/8 - 1+""+i%8+""+(int) Math.floor((double) (i-1)/8)+""+Math.abs((i-1)%8)+",");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // MOVE 1 FORWARD
        // shift over by 8 (square in front of pawn) and make sure there is no piece there
        possibleMove = (BP >> 8) & ~BitBoards.RANK_1 & EMPTY;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(((i-1)/8-1)+""+((i-1)%8)+""+(i/8)+""+((i-1)%8)+",");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // MOVE 2 FORWARD
        // big thing is to check that both squares in front of the pawn are empty and that the destination is on rank 4
        // this avoids pawns jumping over pieces, and makes sure only starting pawns (on rank 2) can jump two squares
        possibleMove = (BP>>16) & EMPTY & (EMPTY << 8) & BitBoards.RANK_5;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove) + 8;
            list.append(((i-1)/8-3)+""+((i-1)%8)+""+((i-1)/8-1)+""+((i-1)%8)+",");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // PROMOTE BY CAPTURE RIGHT
        // only difference from regular capture right is that this checks the pawn will be capturing on the 8th rank (and therefore promoting)
        // can promote to a queen, knight, rook, or bishop
        // since white pawn promotion always happens from the 7th to 8th rank, we really only need to track the file change and possible promotion pieces
        // promotions are stored as <old y> <new y> <Q|R|N|B> <P>
        possibleMove = (BP >> 7) & ENEMY_PIECES & BitBoards.RANK_1 & ~BitBoards.H_FILE;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append((i%8)+""+((i-1)%8)+"QP,"+(i%8)+""+((i-1)%8)+"RP,"+(i%8)+""+((i-1)%8)+"BP,"+(i%8)+""+((i-1)%8)+"NP,");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // PROMOTE BY CAPTURE LEFT
        possibleMove = (BP >> 9) & ENEMY_PIECES & BitBoards.RANK_1 & ~BitBoards.A_FILE;
        ChessUtilities.printBitboard(possibleMove);
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            System.out.println(i);
            list.append(Math.abs((i-2)%8)+""+(i-1)%8+"QP,"+Math.abs((i-2)%8)+""+(i-1)%8+"RP,"+Math.abs((i-2)%8)+""+(i-1)%8+"BP,"+Math.abs((i-2)%8)+""+(i-1)%8+"NP,");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // PROMOTE BY PUSH
        possibleMove = (BP >> 8) & ENEMY_PIECES & BitBoards.RANK_1;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(Math.abs((i-1)%8)+""+Math.abs((i-1)%8)+"QP," +Math.abs((i-1)%8)+""+Math.abs((i-1)%8)+"RP," + Math.abs((i-1)%8)+""+Math.abs((i-1)%8)+ "NP," + Math.abs((i-1)%8)+""+Math.abs((i-1)%8)+ "BP,");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // EN PASSANT
        // en passant moves are stored as <old x> <new x> <EN>
        // capture right
        possibleMove =(BP >> 1) & WP & BitBoards.RANK_4 & ~BitBoards.H_FILE & EP;
        if (possibleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(possibleMove);
            list.append((i%8)-1 + "" + i/8 + "EN");
        }
        // capture left
        possibleMove =(BP << 1) & WP & BitBoards.RANK_4 & ~BitBoards.A_FILE & EP;
        if (possibleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(possibleMove);
            list.append((i%8)+1 + "" + i/8 + "EN");
        }
        return list.toString();
    }


    public String NPossibleMoves(long KNIGHT){
        StringBuilder list = new StringBuilder();
        long i = KNIGHT & -KNIGHT; //find knights
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
                possibleMove &= ~BitBoards.FILE_GH & NOT_ENEMY_PIECES;
            }
            else {
                possibleMove &= ~BitBoards.FILE_AB & NOT_ENEMY_PIECES;
            }
            long j = possibleMove & -possibleMove;
            addMovesToList(list, possibleMove, boardIndex, j);
            KNIGHT &= ~i;
            i = KNIGHT & -KNIGHT;
        }
        return list.toString();
    }


    public String BPossibleMoves(long BISHOP){
        StringBuilder list = new StringBuilder();
        long i = BISHOP & -BISHOP; //find bishops
        long possibleMove;
        while(i != 0)
        {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            possibleMove = DiagonalLeftAndRightMoves(boardIndex) & NOT_ENEMY_PIECES;
            long j = possibleMove & -possibleMove;
            addMovesToList(list, possibleMove, boardIndex, j);
            BISHOP &= ~i;
            i = BISHOP & - BISHOP;
        }
        return list.toString();
    }


    public String RPossibleMoves(long ROOK){
        StringBuilder list = new StringBuilder();
        long i = ROOK & -ROOK; //find rooks
        long possibleMove;
        while(i != 0)
        {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            possibleMove = HorizontalAndVerticalMoves(boardIndex) & NOT_ENEMY_PIECES;
            long j = possibleMove & -possibleMove;
            addMovesToList(list, possibleMove, boardIndex, j);
            ROOK &= ~i;
            i = ROOK & - ROOK;
        }
        return list.toString();
    }


    public String QPossibleMoves(long QUEEN){
        StringBuilder list = new StringBuilder();
        long i = QUEEN & -QUEEN; //find queens
        long possibleMove;
        while(i != 0)
        {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            possibleMove = (HorizontalAndVerticalMoves(boardIndex) | DiagonalLeftAndRightMoves(boardIndex)) & NOT_ENEMY_PIECES;
            long j = possibleMove & -possibleMove;
            addMovesToList(list, possibleMove, boardIndex, j);
            QUEEN &= ~i;
            i = QUEEN & - QUEEN;
        }
        return list.toString();
    }


    public String KPossibleMoves(long KING){
        StringBuilder list = new StringBuilder();
        long i = KING & -KING; //find king
        long possibleMove = 0;
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
                possibleMove &= ~BitBoards.FILE_GH & NOT_ENEMY_PIECES;
            }
            else {
                possibleMove &= ~BitBoards.FILE_AB & NOT_ENEMY_PIECES;
            }
            long j = possibleMove & -possibleMove;
            addMovesToList(list, possibleMove, boardIndex, j);
            KING &= ~i;
            i = KING & -KING;
        }
        return list.toString();
    }


    public String WKCastle(long WR, boolean WQC, boolean WKC){
        StringBuilder list = new StringBuilder();
        if (WKC && ((1L << BitBoards.ROOK_STARTING_LOCATIONS[0]) & WR) != 0){
            list.append("7476,");
        }
        if (WQC && ((1L << BitBoards.ROOK_STARTING_LOCATIONS[1]) & WR) != 0){
            list.append("7472,");
        }
        return list.toString();
    }


    public String BKCastle(long BR, boolean BQC, boolean BKC){
        StringBuilder list = new StringBuilder();
        if (BKC && ((1L << BitBoards.ROOK_STARTING_LOCATIONS[2]) & BR) != 0){
            list.append("0406,");
        }
        if (BQC && ((1L << BitBoards.ROOK_STARTING_LOCATIONS[3]) & BR) != 0){
            list.append("0402,");
        }
        return list.toString();
    }


    public long unsafeForBlack(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK){
        long unsafe;
        OCCUPIED = WP | WN | WB | WR | WQ | WK | BP | BN | BB | BR | BQ | BK;

        // white pawn unsafe squares
        unsafe = ((WP << 7) & ~BitBoards.A_FILE) | ((WP << 9) & ~BitBoards.H_FILE);

        long possibleMove;
        // white knight unsafe squares
        long i = WN & -WN;
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
                possibleMove &= ~BitBoards.FILE_GH;
            }
            else {
                possibleMove &= ~BitBoards.FILE_AB;
            }
            unsafe |= possibleMove;
            WN &= ~i;
            i = WN & -WN;
        }

        // white bishop/queen (diagonal) unsafe squares
        long WBQ = WB | WQ;
        i = WBQ & -WBQ;
        while(i != 0)
        {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            possibleMove = DiagonalLeftAndRightMoves(boardIndex);
            unsafe |= possibleMove;
            WBQ &= ~i;
            i = WBQ & - WBQ;
        }

        // white rook/queen (vertical + horizontal) unsafe squares
        long WRQ = WR | WQ;
        i = WRQ & -WRQ; //find rooks
        while(i != 0)
        {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            possibleMove = HorizontalAndVerticalMoves(boardIndex);
            unsafe |= possibleMove;
            WRQ &= ~i;
            i = WRQ & - WRQ;
        }

        // white king unsafe squares
        int boardIndex = 64 - Long.numberOfTrailingZeros(WK);
        // I check 55 because the KING_SPACES static is the possible king moves on g2, which is position 55 on the bitboard
        if (boardIndex > 55){
            possibleMove = BitBoards.KING_SPACES >> (boardIndex - 55);
        }
        else {
            possibleMove = BitBoards.KING_SPACES << (55 - boardIndex);
        }
        if (boardIndex % 8 < 4){
            possibleMove &= ~BitBoards.FILE_GH;
        }
        else {
            possibleMove &= ~BitBoards.FILE_AB;
        }
        unsafe |= possibleMove;
        return unsafe;
    }


    private void addMovesToList(StringBuilder list, long possibleMove, int boardIndex, long j) {
        while (j != 0)
        {
            int index = Long.numberOfLeadingZeros(j);
            list.append(((boardIndex-1)/8)+""+((boardIndex-1)%8)+""+((index)/8)+""+((index)%8)+",");
            possibleMove &= ~j;
            j = possibleMove & - possibleMove;
        }
    }
}

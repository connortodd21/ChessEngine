package java;

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


    public static String whitePossibleMoves(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK, long EP, boolean WCQ, boolean WCK){
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
        possibleMoves.append(WCKastle(WR, WCQ, WCK));
        return possibleMoves.toString();
    }


    public static String blackPossibleMoves(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK, long EP, boolean BCQ, boolean BCK){
        NOT_ENEMY_PIECES = ~(BP|BN|BB|BR|BQ|BK|WK);
        ENEMY_PIECES = WP | WN |WB | WR | WQ;
        EMPTY = ~(WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK);
        OCCUPIED = ~EMPTY;
        StringBuilder possibleMoves = new StringBuilder();
        possibleMoves.append(BPPossibleMoves(BP, EP, WP));
        possibleMoves.append(BPossibleMoves(BB));
        possibleMoves.append(RPossibleMoves(BR));
        possibleMoves.append(QPossibleMoves(BQ));
        possibleMoves.append(NPossibleMoves(BN));
        possibleMoves.append(KPossibleMoves(BK));
        possibleMoves.append(BCKastle(BR, BCQ, BCK));
//        System.out.println(BPPossibleMoves(BP, EP, WP));
//        System.out.println(possibleMoves.toString());
        return possibleMoves.toString();
    }


    public static String WPPossibleMoves(long WP, long EP, long BP){
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
            list.append(((i-1)%8)-1 + "" + (i-1)%8 + "EW,");
        }
        // capture left
        possibleMove =(WP << 1) & BP & BitBoards.RANK_5 & ~BitBoards.H_FILE & EP;
        if (possibleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(possibleMove);
            list.append((i%8) + "" + (i-1)%8 + "EW,");
        }
        return list.toString();
    }


    public static String BPPossibleMoves(long BP, long EP, long WP){
        StringBuilder list = new StringBuilder(); // stored as a string: <white x> <white y> <black x> <black y>
        // CAPTURE RIGHT
        // Shifting the pawn over by 7 on the bitboard results in the square diagonal to the right of the pawn (i.e. the square to capture to the right)
        // Black can only capture is there is a white piece on the right diagonal. Capturing to the right on the H file is impossible, and the 1st rank is for pawn promotion
        long possibleMove = (BP>>7) & ENEMY_PIECES & ~BitBoards.RANK_1 & ~BitBoards.H_FILE;
//        ChessUtilities.printBitboard(possibleMove);
        long singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
//            System.out.println(i);
            list.append((i/8-1) +""+i%8+""+(int) Math.floor((double) (i-1)/8)+""+Math.abs((i-1)%8)+",");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // CAPTURE LEFT
        // same idea as capture right, just shift over by 9 (two more squares) and ignore black pawns on the A file
        possibleMove = (BP >> 9) & ENEMY_PIECES & ~BitBoards.RANK_1 & ~BitBoards.A_FILE;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append((int) Math.ceil((double) i/8)-2+""+Math.abs((i-2)%8)+""+(int) Math.floor((double) (i-1)/8)+""+Math.abs((i-1)%8)+",");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // MOVE 1 FORWARD
        // shift over by 8 (square in front of pawn) and make sure there is no piece there
        possibleMove = (BP >> 8) & ~BitBoards.RANK_1 & EMPTY;
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
            list.append(((i-1)/8-1)+""+((i-1)%8)+""+((i-1)/8)+""+((i-1)%8)+",");
            possibleMove = possibleMove & ~singleMove;
            singleMove = possibleMove & -possibleMove;
        }
        // MOVE 2 FORWARD
        // big thing is to check that both squares in front of the pawn are empty and that the destination is on rank 4
        // this avoids pawns jumping over pieces, and makes sure only starting pawns (on rank 2) can jump two squares
        possibleMove = (BP>>16) & EMPTY & (EMPTY >> 8) & BitBoards.RANK_5;
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
        singleMove = possibleMove & -possibleMove;
        while (singleMove != 0){
            int i = 64 - Long.numberOfTrailingZeros(singleMove);
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
            if (i%8+1 > 7){
//                System.out.println(i);
//                ChessUtilities.printBitboard(possibleMove);
            }
            list.append((i%8)-1 + "" + i/8 + "EB,");
        }
        // capture left
        possibleMove =(BP << 1) & WP & BitBoards.RANK_4 & ~BitBoards.A_FILE & EP;
        if (possibleMove != 0){
            //TODO FIX THIS ERROR AT (1%8)+1
            int i = 64 - Long.numberOfTrailingZeros(possibleMove);
            if (i%8+1 > 7){
//                System.out.println(i);
//                ChessUtilities.printBitboard(possibleMove);
            }
            list.append((i%8) + "" + i/8 + "EB,");
        }
        return list.toString();
    }


    public static String NPossibleMoves(long KNIGHT){
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
            if ((64 - boardIndex) % 8 < 4){
                possibleMove &= ~BitBoards.FILE_AB & NOT_ENEMY_PIECES;
            }
            else {
                possibleMove &= ~BitBoards.FILE_GH & NOT_ENEMY_PIECES;
            }
            long j = possibleMove & -possibleMove;
            addMovesToList(list, possibleMove, boardIndex, j);
            KNIGHT &= ~i;
            i = KNIGHT & -KNIGHT;
        }
        return list.toString();
    }


    public static String BPossibleMoves(long BISHOP){
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


    public static String RPossibleMoves(long ROOK){
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


    public static String QPossibleMoves(long QUEEN){
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


    public static String KPossibleMoves(long KING){
        StringBuilder list = new StringBuilder();
        long i = KING & -KING; //find king
        long possibleMove = 0;
        while(i != 0)
        {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
//            System.out.println(boardIndex);
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
//            ChessUtilities.printBitboard(possibleMove);
            long j = possibleMove & -possibleMove;
            addMovesToList(list, possibleMove, boardIndex, j);
            KING &= ~i;
            i = KING & -KING;
        }
        return list.toString();
    }


    public static String WCKastle(long WR, boolean WCQ, boolean WCK){
        StringBuilder list = new StringBuilder();
        if (WCK && ((1L << BitBoards.ROOK_STARTING_LOCATIONS[0]) & WR) != 0){
            if (( OCCUPIED & ((1L<<2) | (1L<<1)) ) == 0){
                list.append("7476,");
            }
        }
        if (WCQ && ((1L << BitBoards.ROOK_STARTING_LOCATIONS[1]) & WR) != 0){
            if (( OCCUPIED & ((1L<<4) | (1L<<5) | (1L<<6)) ) == 0){
                list.append("7472,");
            }
        }
        return list.toString();
    }


    public static String BCKastle(long BR, boolean BCQ, boolean BCK){
        StringBuilder list = new StringBuilder();
        if (BCK && ((1L << BitBoards.ROOK_STARTING_LOCATIONS[2]) & BR) != 0){
            if ((OCCUPIED & ((1L << 57) | (1L<<58)) ) == 0) {
                list.append("0406,");
            }
        }
        if (BCQ && ((1L << BitBoards.ROOK_STARTING_LOCATIONS[3]) & BR) != 0){
            if ((OCCUPIED & ((1L<<60) | (1L<<61) |( 1L<<62)) ) == 0){
                list.append("0402,");
            }
        }
        return list.toString();
    }


    public static long unsafeForBlack(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK){
        long unsafe;
        OCCUPIED = WP | WN | WB | WR | WQ | WK | BP | BN | BB | BR | BQ | BK;

        // white pawn unsafe squares
        unsafe = ((WP << 7) & ~BitBoards.A_FILE) | ((WP << 9) & ~BitBoards.H_FILE);

        long possibleMove;
        // white knight unsafe squares
        long i = WN & -WN;
        while(i != 0) {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            // I check 46 because the KNIGHT_SPACES static is the possible knight moves on f3, which is position 46 on the bitboard
            if (boardIndex > 46){
                possibleMove = BitBoards.KNIGHT_SPACES >> (boardIndex - 46);
            }
            else {
                possibleMove = BitBoards.KNIGHT_SPACES << (46 - boardIndex);
            }
            if ((64 - boardIndex) % 8 < 4){
                possibleMove &= ~BitBoards.FILE_AB;
            }
            else {
                possibleMove &= ~BitBoards.FILE_GH;
            }
            unsafe |= possibleMove;
            WN &= ~i;
            i = WN & -WN;
        }

        // white bishop/queen (diagonal) unsafe squares
        long WBQ = WB | WQ;
        i = WBQ & -WBQ;
        while(i != 0) {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            possibleMove = DiagonalLeftAndRightMoves(boardIndex);
            unsafe |= possibleMove;
            WBQ &= ~i;
            i = WBQ & - WBQ;
        }

        // white rook/queen (vertical + horizontal) unsafe squares
        long WRQ = WR | WQ;
        i = WRQ & -WRQ; //find rooks
        while(i != 0) {
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


    public static long unsafeForWhite(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK){
        long unsafe;
        OCCUPIED = WP | WN | WB | WR | WQ | WK | BP | BN | BB | BR | BQ | BK;

        // white pawn unsafe squares
        unsafe = ((BP >> 7) & ~BitBoards.H_FILE) | ((BP >> 9) & ~BitBoards.A_FILE);

        long possibleMove;
        // white knight unsafe squares
        long i = BN & -BN;

        while(i != 0) {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            // I check 46 because the KNIGHT_SPACES static is the possible knight moves on f3, which is position 46 on the bitboard
            if (boardIndex > 46){
                possibleMove = BitBoards.KNIGHT_SPACES >> (boardIndex - 46);
            }
            else {
                possibleMove = BitBoards.KNIGHT_SPACES << (46 - boardIndex);
            }
            if ((64 - boardIndex) % 8 < 4){
                possibleMove &= ~BitBoards.FILE_AB;
            }
            else {
                possibleMove &= ~BitBoards.FILE_GH;
            }
            unsafe |= possibleMove;
            BN &= ~i;
            i = BN & -BN;
        }

        // white bishop/queen (diagonal) unsafe squares
        long BBQ = BB | BQ;
        i = BBQ & -BBQ;
        while(i != 0) {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            possibleMove = DiagonalLeftAndRightMoves(boardIndex);
            unsafe |= possibleMove;
            BBQ &= ~i;
            i = BBQ & - BBQ;
        }

        // white rook/queen (vertical + horizontal) unsafe squares
        long BRQ = BR | BQ;
        i = BRQ & -BRQ; //find rooks
        while(i != 0) {
            int boardIndex = 64 - Long.numberOfTrailingZeros(i);
            possibleMove = HorizontalAndVerticalMoves(boardIndex);
            unsafe |= possibleMove;
            BRQ &= ~i;
            i = BRQ & - BRQ;
        }

        // white king unsafe squares
        int boardIndex = 64 - Long.numberOfTrailingZeros(BK);
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


    private static void addMovesToList(StringBuilder list, long possibleMove, int boardIndex, long j) {
        while (j != 0)
        {
            int index = Long.numberOfLeadingZeros(j);
            list.append(((boardIndex-1)/8)+""+((boardIndex-1)%8)+""+((index)/8)+""+((index)%8)+",");
            possibleMove &= ~j;
            j = possibleMove & - possibleMove;
        }
    }


    public static long makeMove(long board, String move, char type){
        if (Character.isDigit(move.charAt(3))){
            // this means we have a move with the format x,y,x,y, not a promotion or en passant
            // find the starting index on the bitboard using the original x,y pair
            int start = (Character.getNumericValue(move.charAt(0))*8) + (Character.getNumericValue(move.charAt(1)));
            // find the ending index on the bitboard using the new x,y pair
            int end = (Character.getNumericValue(move.charAt(2))*8) + (Character.getNumericValue(move.charAt(3)));
            if (((board >> 63-start) & 1) == 1){
                board &= ~(1L << 63-start);
                board |= (1L << 63-end);
            }
            else{
                // this means there is a capture taking place
                board &= ~(1L << 63-end);
            }
        }
        else if(move.charAt(3) == 'P'){
            // pawn promotion
            int start, end;
            if (Character.isUpperCase(move.charAt(2))){
                // white piece, therefore it promotes from rank 7 to rank 8
                start = Long.numberOfTrailingZeros(BitBoards.FILE_MASKS[move.charAt(0) - '0'] & BitBoards.RANK_7);
                end = Long.numberOfTrailingZeros(BitBoards.FILE_MASKS[move.charAt(1) - '0'] & BitBoards.RANK_8);
            }
            else {
                // black piece, therefore it promotes from rank 2 to rank 1
                start = Long.numberOfTrailingZeros(BitBoards.FILE_MASKS[move.charAt(2) - '0'] & BitBoards.RANK_2);
                end = Long.numberOfTrailingZeros(BitBoards.FILE_MASKS[move.charAt(3) - '0'] & BitBoards.RANK_1);
            }
            if (type == move.charAt(2)){
                board &= ~(1L << 63 - start);
                board |= (1L << 63 - end);
            }
            else {
                board &= ~(1L << 63 - end);
            }
        }
        else if(move.charAt(2) == 'E'){
            // en passant
            int start, end;
            if (move.charAt(3) == 'W'){
                // white piece, therefore en passant capture would be from rank 5 to 6
                if (Character.getNumericValue(move.charAt(2)) > 7){
//                    System.out.println(move);
                }
                start = Long.numberOfTrailingZeros(BitBoards.FILE_MASKS[move.charAt(0) - '0'] & BitBoards.RANK_5);
                end = Long.numberOfTrailingZeros(BitBoards.FILE_MASKS[move.charAt(1) - '0'] & BitBoards.RANK_6);
            }
            else {
                // black piece, therefore en passant capture would be from rank 4 to 3
                start = Long.numberOfTrailingZeros(BitBoards.FILE_MASKS[move.charAt(0) - '0'] & BitBoards.RANK_4);
                end = Long.numberOfTrailingZeros(BitBoards.FILE_MASKS[move.charAt(1) - '0'] & BitBoards.RANK_3);

            }
            if (((board >> 63 - start) & 1) == 1){
                board &= ~(1L << 63 - start);
                board |= (1L << 63 - end);
            }
        }
        else {
            ChessUtilities.printBitboard(ENEMY_PIECES);
            ChessUtilities.printBitboard(OCCUPIED);
            ChessUtilities.printBitboard(board);
            System.out.println(type);
            System.out.println("Error: " + move + " is an invalid move");
            System.exit(0);
        }
        return board;
    }


    public static long makeMoveEP(long board,String move) {
        if (Character.isDigit(move.charAt(3))) {
            int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            // check for a double pawn push
            if ((Math.abs(move.charAt(0) - move.charAt(2)) == 2) && (((board>>(64-start)) & 1) == 1)) {
                return BitBoards.FILE_MASKS[move.charAt(1)-'0'];
            }
        }
        return 0;
    }
}

import java.util.InputMismatchException;
import java.util.Scanner;

public class UCI {

    public static void startUCI(){
        while (true){
            System.out.print("> ");
            Scanner s = new Scanner(System.in);
            String input = s.nextLine().trim();
            if (input.equals("uci")){
                UCI();
            }
            else if (input.startsWith("setoption")){
                UCISetOption();
            }
            else if (input.equals("isready")){
                UCIIsReady();
            }
            else if (input.equals("ucinewgame")){
                UCINewGame();
            }
            else if (input.startsWith("position")){
                UCIPosition(input);
            }
            else if (input.startsWith("go")){
                UCIGo(input);
            }
            else if (input.equals("print") || input.equals("d") || input.equals("p")){
                UCIPrint();
            }
            else if (input.equals("quit") || input.equals("exit")){
                UCIExit();
            }
            else {
                System.out.println("Unknown command " + input);
            }
        }
    }

    public static void UCI(){
        System.out.println("Connor Todd's Chess Engine ----- https://github.com/connortodd21/ChessEngine");
        System.out.println("id name Connor Todd's Chess Engine");
        System.out.println("id author Connor Todd");
        System.out.println("uciok");
    }

    public static void UCISetOption(){

    }

    public static void UCIIsReady(){
        System.out.println("readyok");
    }

    public static void UCINewGame(){
        Board.initializeBoard();
    }

    public static void UCIPosition(String input){
        input = input.substring("position ".length());
        if (input.contains("startpos")){
            input = input.substring("startpos".length());
            Board.importFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        }
        else if (input.contains("fen")){
            input = input.substring("fen ".length());
            try {
                Board.importFEN(input);
            } catch (Exception e){
                System.out.println("Invalid fen, try again");
            }
        }
        if (input.contains("moves")){
            input = input.substring(input.indexOf("moves") + "moves".length());
            while (input.length() > 0){
                String moves;
                if (Engine.WhiteToMove){
                    moves = MovePiece.whitePossibleMoves(Engine.WP, Engine.WN, Engine.WB, Engine.WR, Engine.WQ, Engine.WK, Engine.BP, Engine.BN, Engine.BB, Engine.BR, Engine.BQ, Engine.BK, Engine.EP, Engine.WCQ, Engine.WCK);
                }
                else {
                    moves = MovePiece.blackPossibleMoves(Engine.WP, Engine.WN, Engine.WB, Engine.WR, Engine.WQ, Engine.WK, Engine.BP, Engine.BN, Engine.BB, Engine.BR, Engine.BQ, Engine.BK, Engine.EP, Engine.BCQ, Engine.BCK);
                }

                input = input.substring(input.indexOf(' ')+1);
            }
        }
    }

    public static void UCIGo(String input){
        input = input.substring("go ".length());
        if (input.contains("perft ")){
            input = input.substring("perft ".length());
            try {
                Perft.perftRoot(Engine.WP, Engine.WN, Engine.WB, Engine.WR, Engine.WQ, Engine.WK, Engine.BP, Engine.BN, Engine.BB, Engine.BR, Engine.BQ, Engine.BK, Engine.EP, Engine.WCQ, Engine.WCK, Engine.BCQ, Engine.BCK, Engine.WhiteToMove, 0, Integer.parseInt(input));
            } catch (NumberFormatException n){
                System.out.println("Usage: go perft <depth>");
            } catch (InputMismatchException i){
                System.out.println("Error: depth must be a positive number");
            }
            catch (Exception e){
                System.out.println("System error: exiting now");
                System.exit(0);
            }
        }
    }

    public static void UCIPrint(){
        Board.printEngineBoard();
    }

    public static void UCIExit(){
        System.out.println("Goodbye");
        System.exit(0);
    }


}

package Model;

import Enum.*;
import java.util.Scanner;

public class TestLogic {

    public static void main(String[] args) throws InterruptedException {

//        Player one = new Player("Player 1", Side.BLACK);
//        Player two = new Player("Player 2", Side.WHITE);

        MinmaxAI one = new MinmaxAI(Side.BLACK, 3);
        MinmaxAI two = new MinmaxAI(Side.WHITE, 6);

        //one goes first if true;
        boolean turn = true;

        //System.out.println(board.toString());
        Scanner sc = new Scanner(System.in);

        Board board = new Board();

        Player current = one;
        if(!turn) current = two;

        int no_of_moves = 0;

        System.out.println(board.toString());
        while (no_of_moves < 1000)
        {
            no_of_moves++;
            System.out.print(current.toString() + "'s turn: ");

            Decision decision = null;

            if(current instanceof MinmaxAI) {
                decision = ((MinmaxAI)current).makeMove(board);
                System.out.println();
            }
            else {
                String text = sc.nextLine();
                if(text.equals("board"))
                {
                    System.out.println(board.toString());
                }
                else if (text.equals("rand"))
                {
                    decision = current.makeRandomMove(board);
                }
                else
                {
                    String[] split = text.split(" ");
                    Move move;
                    if(split.length == 1)
                    {
                        move = new Move(Integer.parseInt(text.charAt(0)+""), Integer.parseInt(text.charAt(1)+""),
                                Integer.parseInt(text.charAt(2)+""), Integer.parseInt(text.charAt(3)+""));
                    }
                    else
                    {
                        int[] s = new int[split.length];
                        for(int i = 0; i< split.length; i++)
                        {
                            s[i] = Integer.parseInt(split[i]);
                        }
                        move = new Move(s[0], s[1], s[2], s[3]);
                    }
                    decision = current.makeMove(move, board);
                }
            }

//            System.out.println("Decision: " + decision);
            if(decision == Decision.FAILED_INVALID_DESTINATION || decision == Decision.FAILED_MOVING_INVALID_PIECE)
            {
                System.out.println("Move Failed");
                // don't update anything
            }
            else if(decision == Decision.COMPLETED)
            {
                System.out.println(board.toString());
                if(board.getNumBlackPieces() == 0)
                {
                    System.out.println("White wins with " + board.getNumWhitePieces() + " pieces left");
                    break;
                }
                if(board.getNumWhitePieces() == 0)
                {
                    System.out.println("Black wins with " + board.getNumBlackPieces() + " pieces left");
                    break;
                }
                if(turn)
                    current = two;
                else
                    current = one;
                turn = !turn;
            }
            else if(decision == Decision.ADDITIONAL_MOVE)
            {
                System.out.println("Additional Move");
            }
            else if(decision == Decision.GAME_ENDED)
            {
                //current player cannot move
                if(current.getSide() == Side.BLACK)
                {
                    System.out.println("White wins");
                }
                else {
                    System.out.println("Black wins");
                }
                break;
            }
        }

        System.out.println("Game finished after: " + no_of_moves + " rounds");
    }
}
package Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Enum.*;

public class MinmaxAI extends Player{

    private Point skippingPoint;
    private int depth;

    public MinmaxAI(Side s, int depth)
    {
        super("MinmaxAI", s);
        this.depth = depth;
    }

    public void setSkippingPoint(Point skippingPoint) {
        this.skippingPoint = skippingPoint;
    }

    public int getDepth() {
        return depth;
    }

    public Decision makeMove(Board board)
    {
        Move m = minmaxStart(board, depth, getSide(), true);
        Decision decision = board.makeMove(m, getSide());
        if(decision == Decision.ADDITIONAL_MOVE) skippingPoint = m.getEnd();
        return decision;
    }


    public Move minmaxStart(Board board, int depth, Side side, boolean maximizingPlayer)
    {
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        List<Move> possibleMoves;
        if(skippingPoint == null) {
            if(!board.getAllValidSkipMoves(side).isEmpty())
                possibleMoves = board.getAllValidSkipMoves(side);
            else
                possibleMoves = board.getAllValidMoves(side);
        }
        else
        {
            possibleMoves = board.getValidSkipMoves(skippingPoint.x, skippingPoint.y, side);
            skippingPoint = null;
        }
        //System.out.println("side: " + side + " " + possibleMoves.size());

        List<Double> heuristics = new ArrayList<>();
        if(possibleMoves.isEmpty())
            return null;
        Board tempBoard;
        for (Move possibleMove : possibleMoves) {
            tempBoard = board.clone();
            tempBoard.makeMove(possibleMove, side);
            heuristics.add(minmax(tempBoard, depth - 1, flipSide(side), !maximizingPlayer, alpha, beta));
        }
        //System.out.println("\nMinimax at depth: " + depth + "\n" + heuristics);

        double maxHeuristics = Double.NEGATIVE_INFINITY;

        Random rand = new Random();
        for(int i = heuristics.size() - 1; i >= 0; i--) {
            if (heuristics.get(i) >= maxHeuristics) {
                maxHeuristics = heuristics.get(i);
            }
        }
        //Main.println("Unfiltered heuristics: " + heuristics);
        for(int i = 0; i < heuristics.size(); i++)
        {
            if(heuristics.get(i) < maxHeuristics)
            {
                heuristics.remove(i);
                possibleMoves.remove(i);
                i--;
            }
        }
        //Main.println("Filtered/max heuristics: " + heuristics);
        return possibleMoves.get(rand.nextInt(possibleMoves.size()));
    }

    private double minmax(Board board, int depth, Side side, boolean maximizingPlayer, double alpha, double beta)
    {
        if(depth == 0) {
            return getHeuristic(board);
        }
        List<Move> possibleMoves = board.getAllValidMoves(side);

        double initial;
        Board tempBoard;
        if(maximizingPlayer)
        {
            initial = Double.NEGATIVE_INFINITY;
            for (Move possibleMove : possibleMoves) {
                tempBoard = board.clone();
                tempBoard.makeMove(possibleMove, side);

                double result = minmax(tempBoard, depth - 1, flipSide(side), !maximizingPlayer, alpha, beta);

                initial = Math.max(result, initial);
                alpha = Math.max(alpha, initial);

                if (alpha >= beta)
                    break;
            }
        }
        //minimizing
        else
        {
            initial = Double.POSITIVE_INFINITY;
            for (Move possibleMove : possibleMoves) {
                tempBoard = board.clone();
                tempBoard.makeMove(possibleMove, side);

                double result = minmax(tempBoard, depth - 1, flipSide(side), !maximizingPlayer, alpha, beta);

                initial = Math.min(result, initial);
                alpha = Math.min(alpha, initial);

                if (alpha >= beta)
                    break;
            }
        }

        return initial;
    }

    private double getHeuristic(Board b)
    {
//        naive implementation

//        if(getSide() == Side.WHITE)
//            return b.getNumWhitePieces() - b.getNumBlackPieces();
//        return b.getNumBlackPieces() - b.getNumWhitePieces();

        double kingWeight = 1.2;
        double result;
        if(getSide() == Side.WHITE)
            result = b.getNumWhiteKingPieces() * kingWeight + b.getNumWhiteNormalPieces() - b.getNumBlackKingPieces() *
                    kingWeight - b.getNumBlackNormalPieces();
        else
            result = b.getNumBlackKingPieces() * kingWeight + b.getNumBlackNormalPieces() - b.getNumWhiteKingPieces() *
                    kingWeight - b.getNumWhiteNormalPieces();
        return result;

    }

    private Side flipSide(Side side)
    {
        if(side == Side.BLACK)
            return Side.WHITE;
        return Side.BLACK;
    }
}
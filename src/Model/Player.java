package Model;

import java.util.List;
import java.util.Random;
import Enum.*;

public class Player {
    private Side side;
    private String name;

    public Player(String name, Side side)
    {
        this.name = name;
        this.side = side;
    }

    public Side getSide()
    {
        return side;
    }

    public Decision makeMove(Move m, Board b)
    {
        if(!b.getAllValidSkipMoves(side).isEmpty() && !b.getAllValidSkipMoves(side).contains(m))
            return Decision.FAILED_INVALID_DESTINATION;
        else
            return b.makeMove(m,side);
    }

    public Decision makeRandomMove(Board b)
    {
        List<Move> moves = b.getAllValidMoves(side);
        Random rand = new Random();
        return b.makeMove(moves.get(rand.nextInt(moves.size())), side);
    }
    public String toString()
    {
        return name + "/" + side;
    }
}
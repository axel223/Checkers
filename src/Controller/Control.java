package Controller;

import View.Piece;
import View.Tile;
import Model.Board;
import Model.MinmaxAI;
import Model.Move;
import Model.Player;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

import Enum.*;
import javafx.scene.layout.Pane;

public class Control {

    private boolean turn = true; //one goes first if true

    private Player one;
    private Player two;
    private Player current;
    private Board logicBoard;

    public static final int TILE_SIZE = 80;
    public static final int SIZE = 8;

    private Tile[][] board = new Tile[SIZE][SIZE];
    private Group pieceGroup;

    private int toBoard(double pixel) {
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    public Parent createContent(){

        Group tileGroup = new Group();
        pieceGroup = new Group();
        logicBoard = new Board();
        setBoard(tileGroup,pieceGroup);

        Pane root = new Pane();
        root.setPrefSize(SIZE * TILE_SIZE, SIZE * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pieceGroup);

        //options
        showOptions();

        return root;
    }

    public void setBoard(Group tileGroup,Group pieceGroup) {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {

                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);
                Piece piece = null;

                if (y <= 2 && (x + y) % 2 != 0) {
                    piece = makePiece(Type.WHITE, x, y);
                }

                if (y >= 5 && (x + y) % 2 != 0) {
                    piece = makePiece(Type.BLACK, x, y);
                }

                if (piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }
            }
        }
    }

    private Piece makePiece(Type type,int x,int y) {

        Piece piece = new Piece(type,x,y);

        piece.setOnMouseReleased(e ->{
            int newX = toBoard(piece.getLayoutX());
            int newY = toBoard(piece.getLayoutY());

            int oldX = toBoard(piece.getOldX());
            int oldY = toBoard(piece.getOldY());

            Decision result;

            //start row , start col , end Row , end col
            Move move = new Move(oldY,oldX,newY,newX);

            if (newX < 0 || newY < 0 || newX >= SIZE || newY >= SIZE) {
                result = Decision.FAILED_INVALID_DESTINATION;
            } else {
                result = current.makeMove(move, logicBoard);
            }
            makeMove(result,piece,move);
        });
        return piece;
    }

    private void isChange_in_piece(int startRow, int startCol, int endRow, int endCol) {
        if(startRow + 1 != endRow && startRow - 1 != endRow) {
            int midX = (endCol + startCol)/2;
            int midY = (endRow + startRow)/2;
            pieceGroup.getChildren().remove(board[midX][midY].getPiece());
            board[midX][midY].setPiece(null);
        }

        if(endRow == 0 && current.getSide() == Side.BLACK) {
            board[endCol][endRow].getPiece().setType(Type.BLACK_KING);
        }else if(endRow == SIZE-1 && current.getSide() == Side.WHITE){
            board[endCol][endRow].getPiece().setType(Type.WHITE_KING);
        }
    }


    public void showOptions() {
        ButtonType vs_ai = new ButtonType("Vs AI", ButtonBar.ButtonData.OK_DONE);
        ButtonType vs_human = new ButtonType("Vs HUMAN", ButtonBar.ButtonData.OK_DONE);

        ButtonType black = new ButtonType("RED", ButtonBar.ButtonData.OK_DONE);
        ButtonType white = new ButtonType("WHITE", ButtonBar.ButtonData.OK_DONE);

        Alert side = new Alert(Alert.AlertType.CONFIRMATION, "\n" + "Choose on of the sides", black, white, ButtonType.CLOSE);
        Alert versus = new Alert(Alert.AlertType.CONFIRMATION, "\n" + "Choose one of the Option", vs_ai, vs_human, ButtonType.CLOSE);

        Optional<ButtonType> result_type = versus.showAndWait();
        if(result_type.isPresent() && result_type.get() == vs_ai){
            Optional<ButtonType> result_side = side.showAndWait();
            if (result_side.isPresent() && result_side.get() == black) {
//                System.out.println("Player 1");
                one = new Player("Player", Side.BLACK);
                two = new MinmaxAI(Side.WHITE,6);
            }
            if (result_side.isPresent() && result_side.get() == white) {
//                System.out.println("Player 2");
                one = new MinmaxAI(Side.BLACK,6);
                two = new Player("Player", Side.WHITE);
            }
        }

        if(result_type.isPresent() && result_type.get() == vs_human) {
            one = new Player("Player", Side.BLACK);
            two = new Player("Player", Side.WHITE);
//            one = new MinmaxAI(Side.BLACK,6);
//            two = new MinmaxAI(Side.WHITE,5);
        }

        if(result_type.isPresent() && result_type.get() == ButtonType.CLOSE) {
            Platform.exit();
            System.exit(0);
        }

        current = one;
        if(!turn) current = two;
//        changeTile(current);

        //if current is AI then we dont have to wait for piece to drag
        if(current instanceof MinmaxAI) {
            Move newMove = ((MinmaxAI) current).minmaxStart(
                    logicBoard,
                    ((MinmaxAI) current).getDepth(),
                    current.getSide(),
                    true
            );
            Decision decision = logicBoard.makeMove(newMove, current.getSide());
            makeMove(decision,
                    board[newMove.getStart().y][newMove.getStart().x].getPiece(),
                    newMove
            );
        }
    }

    public void makeMove(Decision result, Piece piece, Move move) {
        int startRow = move.getStart().x;
        int startCol = move.getStart().y;
        int endRow = move.getEnd().x;
        int endCol = move.getEnd().y;

        switch (result){
            case COMPLETED:
                piece.move(endCol, endRow);
                board[startCol][startRow].setPiece(null);
                board[endCol][endRow].setPiece(piece);

                isChange_in_piece(startRow, startCol, endRow, endCol);

                if(turn)
                    current = two;
                else
                    current = one;
                turn = !turn;

//                changeTile(current);

                if(logicBoard.getNumBlackPieces() == 0 || (logicBoard.getAllValidMoves(current.getSide()).isEmpty() && current.getSide() == Side.BLACK)) {
                    System.out.println("White wins with " + logicBoard.getNumWhitePieces() + " pieces left");
                    winPrompt("White wins with " + logicBoard.getNumWhitePieces() + " pieces left");
                    return;
                }
                if(logicBoard.getNumWhitePieces() == 0 || (logicBoard.getAllValidMoves(current.getSide()).isEmpty() && current.getSide() == Side.WHITE)) {
                    System.out.println("Black wins with " + logicBoard.getNumBlackPieces() + " pieces left");
                    winPrompt("Black wins with " + logicBoard.getNumBlackPieces() + " pieces left");
                    return;
                }
                break;

            case ADDITIONAL_MOVE:

                piece.move(endCol, endRow);
                board[startCol][startRow].setPiece(null);
                board[endCol][endRow].setPiece(piece);

                isChange_in_piece(startRow, startCol, endRow, endCol);

                if(current instanceof MinmaxAI){
                    ((MinmaxAI) current).setSkippingPoint(move.getEnd());
                }

                if(logicBoard.getNumBlackPieces() == 0) {
                    System.out.println("White wins with " + logicBoard.getNumWhitePieces() + " pieces left");
                    return;
                }
                if(logicBoard.getNumWhitePieces() == 0) {
                    System.out.println("Black wins with " + logicBoard.getNumBlackPieces() + " pieces left");
                    return;
                }
                break;
            case FAILED_INVALID_DESTINATION:
            case FAILED_MOVING_INVALID_PIECE:
                piece.abortMove();
                break;
            case GAME_ENDED:
                if(current.getSide() == Side.BLACK)
                {
                    System.out.println("White wins");
                }
                else {
                    System.out.println("Black wins");
                }
                break;
        }

        if(current instanceof MinmaxAI && result!=Decision.GAME_ENDED){
            Move newMove = ((MinmaxAI) current).minmaxStart(
                    logicBoard,
                    ((MinmaxAI) current).getDepth(),
                    current.getSide(),
                    true
            );
            Decision decision = logicBoard.makeMove(newMove,current.getSide());
            makeMove(decision,
                    board[newMove.getStart().y][newMove.getStart().x].getPiece(),
                    newMove
            );
        }
    }

    public void winPrompt(String string) {
        Alert win = new Alert(Alert.AlertType.INFORMATION,string,ButtonType.CLOSE);
        Optional<ButtonType> res = win.showAndWait();
        if(res.isPresent() && res.get() == ButtonType.CLOSE)
        {
            Platform.exit();
            System.exit(0);
        }
    }
}

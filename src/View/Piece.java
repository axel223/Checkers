package View;
import Enum.*;


import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static Controller.Control.TILE_SIZE;

public class Piece extends StackPane{

    private double mouseX,mouseY;
    private double oldX, oldY;
    Ellipse ellipse;

    public void setType(Type type) {
        Color setColor = null;
        if(type == Type.WHITE){
            setColor = Color.valueOf("#ffffff");
        }
        else if(type == Type.WHITE_KING){
            setColor = Color.valueOf("#0c1d8c");
        }
        else if(type == Type.BLACK){
            setColor =Color.valueOf("#050505");
        }
        else if(type == Type.BLACK_KING){
            setColor =Color.valueOf("#e8ef09");
        }
        changeColor(setColor);
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public void move(int x, int y) {
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;
        relocate(oldX,oldY);
    }

    public void abortMove() {
        relocate(oldX, oldY);
    }

    public void changeColor(Color setColor){
        ellipse.setFill(setColor);
    }

    public Piece(Type type,int x,int y){

        move(x, y);

        Ellipse bg = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        bg.setFill(Color.BLACK);

        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(TILE_SIZE * 0.03);

        bg.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        bg.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2 + TILE_SIZE * 0.07);

        ellipse = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);

        Color setColor = Color.valueOf("#12edda");
        if(type == Type.WHITE){
            setColor = Color.valueOf("#fff9f4");
        }
        else if(type == Type.WHITE_KING){
            setColor = Color.valueOf("#0c1d8c");
        }
        else if(type == Type.BLACK){
            setColor =Color.valueOf("#c40003");
        }
        else if(type == Type.BLACK_KING){
            setColor =Color.valueOf("#e8ef09");
        }
        ellipse.setFill(setColor);

        //  ellipse.setFill(type == PieceType.WHITE || type == PieceType.KING
        //        ? Color.valueOf("#c40003") : Color.valueOf("#fff9f4"));

        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(TILE_SIZE * 0.03);

        ellipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        ellipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);

        getChildren().addAll(bg,ellipse);
        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        setOnMouseDragged(e -> relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY));
    }
}
